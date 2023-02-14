package com.boot.controller;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.boot.bo.WordPlan;
import com.boot.common.result.CodeMsg;
import com.boot.common.result.Result;
import com.boot.entity.WordModule;
import com.boot.service.PlanService;
import com.boot.service.WordModuleService;
import com.boot.utils.MinIOUtils;
import com.boot.utils.SnowFlakeUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.*;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * (WordModule)表控制层
 *
 * @author makejava
 * @since 2023-01-28 23:53:07
 */

@RestController
@Slf4j
@RequestMapping("word")
@SuppressWarnings("all")
public class WordModuleController {

    @Resource
    private MinIOUtils minioUtils;

    @Resource
    private WordModuleService wordModuleService;

    @Resource
    private ElasticsearchClient elasticsearchClient;


    @Resource
    private PlanService planService;

    /**
     * @param file: 词源和模块头像文件
     * @param wordModule: 单词模块dto
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 上传单词模块 已测试
     * @Date: 2023/2/9 10:41
     */
    @PostMapping("wordModule")
    @Transactional
    @SuppressWarnings("all")
    public Result wordModule(@RequestParam MultipartFile[] file,@Valid WordModule wordModule) throws IOException {

        MultipartFile imageFile = null;
        String imageFileName = "";
        MultipartFile wordFile=null;
        String wordFileName = "";
        String bucketName = "word";
        Long moduleId = SnowFlakeUtil.getNextId();
        String wordFileType = "application/json";
        String imageFileType = "image/jpeg";
        String word = "";
        AtomicInteger id = new AtomicInteger(1);
//        判断文件数量
        if (file.length < 2) {
            log.error("文件数量不是2个");
            return Result.error(CodeMsg.NEED_TWO_FILES);
        }
//        判断空文件
        for (MultipartFile multipartFile : file) {
            if (0 == multipartFile.getSize()) {
                log.error("空文件");
                return Result.error(CodeMsg.NULL_FILE);
            }
            if (wordFileType.equals(multipartFile.getContentType())) {
                wordFile = multipartFile;
            } else if(imageFileType.equals(multipartFile.getContentType())){
                imageFile = multipartFile;
            }
        }
//        判断文件格式
        if (0==Objects.requireNonNull(imageFile).getSize()||0== Objects.requireNonNull(wordFile).getSize()){
            log.error("文件格式错误");
            return Result.error(CodeMsg.FILE_FORMAT_ERROR);
        }




        try {
            //上传文件到minIo
            imageFileName = (bucketName + "_" + wordModule.getModuleName() + "_image_" + moduleId.toString()  + imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")));
            wordFileName = (bucketName + "_" + wordModule.getModuleName() + "_word_" + moduleId.toString() + ".json");
            minioUtils.putObject(bucketName, imageFile, imageFileName);
            minioUtils.putObject(bucketName, wordFile, wordFileName);
            log.info("存入minIO正常");
//        存入ES
            InputStream inputStream = wordFile.getInputStream();
            BufferedReader bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            while (word!=null) {
                word = bufferReader.readLine();
                if (word!=null){
                    StringReader sr = new StringReader(word);
                    IndexResponse response = elasticsearchClient.index(i -> i.index(wordModule.getModuleName()).withJson(sr).id(String.valueOf(id.getAndIncrement())));
                }
            }
            log.info("存入Es正常");


        } catch (Exception e) {
            log.error("上传文件失败：{}", e.getMessage());
//            删除文件
//            判断文件是否存在
            minioUtils.removeObject(bucketName, imageFileName);
            minioUtils.removeObject(bucketName, wordFileName);
            log.info("移除文件");

            //删除一个索引
            DeleteIndexResponse delete = elasticsearchClient.indices().delete(f ->
                    f.index(wordModule.getModuleName()));
            log.info("删除Es索引");
            return Result.error("error");

        }
        //        存入Mysql
        wordModule.setModuleId(moduleId);
        wordModule.setModuleImagePath(imageFileName);
        wordModule.setWordPath(wordFileName);
        wordModule.setWordModuleCreateTime(LocalDateTime.now());
        wordModuleService.save(wordModule);
        log.info("存入mysql正常");
        return Result.success();


    }


    /**
     * @param userId: 用户id
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 查询完成一次计划任务所需要的单词信息 已测试
     * @Date: 2023/2/12 12:37
     */
    @GetMapping("word/{userId}")
    public Result<Map> word(@PathVariable Long userId) throws IOException {
        Map map = new HashMap();
        List word = new ArrayList<ObjectNode>();
//        单词计划和单词模块信息
        WordPlan wordPlan=wordModuleService.selectWordPlan(userId);
//        词源
        SearchRequest request = new SearchRequest.Builder()
                .index(wordPlan.getModuleName())
                .query(QueryBuilders.matchAll().build()._toQuery())
                .from(wordPlan.getFinishedWord())
                .size(wordPlan.getDayWord())
                .build();
        List<Hit<ObjectNode>> hits = elasticsearchClient.search(request, ObjectNode.class).hits().hits();
        for (Hit<ObjectNode> Hit : hits) {
            word.add(Hit.source());
        }
        map.put("wordPlan", wordPlan);
        map.put("word", word);
        return Result.success(map);
    }



}


