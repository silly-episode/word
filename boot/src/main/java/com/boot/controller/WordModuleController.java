package com.boot.controller;



import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.boot.common.result.CodeMsg;
import com.boot.common.result.Result;
import com.boot.dto.WordModuleDto;
import com.boot.entity.WordModule;
import com.boot.service.WordModuleService;
import com.boot.utils.BeanDtoVoUtils;
import com.boot.utils.MinIOUtils;

import com.boot.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;


import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.io.*;
import java.time.LocalDateTime;
import java.util.Objects;
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
public class WordModuleController {

    @Resource
    private MinIOUtils minioUtils;

    @Resource
    private WordModuleService wordModuleService;

    @Resource
    private ElasticsearchClient elasticsearchClient;


    /**
     * @param file: 词源和模块头像文件
     * @param wordModuleDto: 单词模块dto
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 上传单词模块
     * @Date: 2023/2/9 10:41
     */
    @PostMapping("wordModule")
    @Transactional
    @SuppressWarnings("all")
    public Result wordModule(@RequestParam MultipartFile[] file,@Valid WordModuleDto wordModuleDto) throws IOException {

        MultipartFile imageFile = null;
        String imageFileName = "";
        MultipartFile wordFile=null;
        String wordFileName = "";
        String bucketName = "word";
        Long moduleId = SnowFlakeUtil.getNextId();
        String wordFileType = "application/json";
        String imageFileType = "image/jpeg";
        String word = "";
        WordModule wordModule;
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
            imageFileName = (bucketName + "_" + wordModuleDto.getModuleName() + "_image_" + moduleId.toString()  + imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")));
            wordFileName = (bucketName + "_" + wordModuleDto.getModuleName() + "_word_" + moduleId.toString() + ".json");
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
                    IndexResponse response = elasticsearchClient.index(i -> i.index(wordModuleDto.getModuleName()).withJson(sr).id(String.valueOf(id.getAndIncrement())));
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
                    f.index(wordModuleDto.getModuleName()));
            log.info("删除Es索引");
            return Result.error("error");

        }
        //        存入Mysql
        wordModule = BeanDtoVoUtils.convert(wordModuleDto, WordModule.class);
        wordModule.setModuleId(moduleId);
        wordModule.setModuleImagePath(imageFileName);
        wordModule.setWordPath(wordFileName);
        wordModule.setCreateTime(LocalDateTime.now());
        wordModuleService.save(wordModule);
        log.info("存入mysql正常");
        return Result.success();


    }



}


