package com.boot.controller;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.indices.DeleteIndexResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.bo.WordPlan;
import com.boot.common.Exception.CustomException;
import com.boot.common.Hutool.IdUtils;
import com.boot.common.result.Result;
import com.boot.dto.WordModuleSearchDto;
import com.boot.entity.Plan;
import com.boot.entity.User;
import com.boot.entity.WordModule;
import com.boot.service.PlanService;
import com.boot.service.UserService;
import com.boot.service.WordModuleService;
import com.boot.utils.MinIOUtils;
import com.boot.utils.SnowFlakeUtil;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
//@RequiresAuthentication
public class WordModuleController {

    @Resource
    private MinIOUtils minioUtils;

    @Resource
    private WordModuleService wordModuleService;

    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Resource
    private MinIOUtils minIOUtils;


    @Resource
    private UserService userService;


    @Resource
    private PlanService planService;


    /**
     * @param file:       词源和模块头像文件
     * @param wordModule: 单词模块dto
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 上传单词模块 已测试
     * @Date: 2023/2/9 10:41
     */
    @PostMapping("wordModule")
    @Transactional
    public Result wordModule(@RequestParam MultipartFile file, WordModule wordModule) throws IOException {
        String bucketName = "word";
        Long moduleId = SnowFlakeUtil.getNextId();
        String wordFileType = "application/json";
        String imageFileType = "image/jpeg";
        String word = "";
        int wordCount = 0;
        AtomicInteger id = new AtomicInteger(1);
        boolean jpgFlag = false;
        boolean jsonFlag = false;
        String esIndex = "word_module_" + IdUtils.getSnowFlakeInstance().nextIdStr();
        /*判断文件类型*/
        if (wordFileType.equals(file.getContentType())) {
            jsonFlag = true;
        } else if (imageFileType.equals(file.getContentType())) {
            jpgFlag = true;
        }
        BufferedReader bufferReader = null;
        try {

            if (jsonFlag) {
                /*存入ES*/
                InputStream inputStream = file.getInputStream();
                bufferReader = new BufferedReader(new InputStreamReader(inputStream));
                while (word != null) {
                    word = bufferReader.readLine();
                    if (word != null) {
                        StringReader sr = new StringReader(word);
                        IndexResponse response = elasticsearchClient.index(
                                i -> i
                                        .index(esIndex)
                                        .withJson(sr)
                                        .id(String.valueOf(id.getAndIncrement())));
                        wordCount++;
                    }
                }
                log.info("存入Es正常");
            }

            if (jpgFlag) {
                minioUtils.putObject(bucketName, file, wordModule.getModuleImagePath());
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (jpgFlag) {
//                minioUtils.removeObject(bucketName, wordModule.getModuleImagePath());
            }
            if (jsonFlag) {
//                DeleteIndexResponse delete = elasticsearchClient.indices().delete(f ->
//                        f.index(wordModule.getModuleName()));
                log.info("删除Es索引");
            }
            return Result.error("导入错误");
        } finally {
            if (jsonFlag) {
                bufferReader.close();
            }
        }
        //        存入Mysql
        if (jsonFlag) {
            wordModule.setModuleId(moduleId)
                    .setWordPath("")
                    .setModuleImagePath(wordModule.getModuleImagePath())
                    .setWordModuleCreateTime(LocalDateTime.now())
                    .setEsIndex(esIndex)
                    .setWordCount(wordCount);
            wordModuleService.save(wordModule);
            log.info("存入mysql正常");
            return Result.success(201, "存入ES正常");
        }
        return Result.success("图片和Mysql正常");
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 更换词源
     * @Date: 2023/4/11 23:50
     */
    @PostMapping("changeEsWordModule")
    public Result changeEsWordModule(@RequestParam MultipartFile file, WordModule module) throws IOException {

        System.out.println(module);
        Long moduleId = module.getModuleId();
        String esIndex = "word_module_" + IdUtils.getSnowFlakeInstance().nextIdStr();
        WordModule wordModule = wordModuleService.getById(moduleId);
        BufferedReader bufferReader = null;
        AtomicInteger id = new AtomicInteger(1);
        String word = "";
        int wordCount = 0;
        if (wordModule == null) {
            return Result.error("单词模块不存在");
        }
        String oldEsIndex = wordModule.getEsIndex();
//        上传词源
        /*存入ES*/
        try {
            InputStream inputStream = file.getInputStream();
            bufferReader = new BufferedReader(new InputStreamReader(inputStream));
            while (word != null) {
                word = bufferReader.readLine();
                if (word != null) {
                    StringReader sr = new StringReader(word);
                    IndexResponse response = elasticsearchClient.index(
                            i -> i
                                    .index(esIndex)
                                    .withJson(sr)
                                    .id(String.valueOf(id.getAndIncrement())));
                    wordCount++;
                }
            }
            log.info("存入Es正常");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bufferReader != null) {
                bufferReader.close();
            }
        }
//        Mysql处理,更新词块
        wordModule.setEsIndex(esIndex).setWordCount(wordCount).setWordModuleCreateTime(LocalDateTime.now());
        wordModuleService.updateById(wordModule);
//        更新计划
        LambdaUpdateWrapper<Plan> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(Plan::getModuleId, moduleId)
                .set(Plan::getAllWord, wordCount);
        planService.update(updateWrapper);

//        删除索引
        try {
            boolean value = elasticsearchClient.exists(builder -> builder.index(oldEsIndex)).value();
            if (value) {
                DeleteIndexResponse delete = elasticsearchClient.indices().delete(f ->
                        f.index(oldEsIndex));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Result.success("更换成功");
    }

    /**
     * @param wordModule:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 更新单词模块
     * @Date: 2023/3/31 17:29
     */
    @PutMapping("wordModule")
    public Result<String> wordModule(@RequestBody WordModule wordModule) {
        if (wordModuleService.updateById(wordModule)) {
            return Result.success("更新成功");
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * @param wordModuleSearchDto:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 分页条件搜索
     * @Date: 2023/3/31 18:00
     */
    @PostMapping("wordModuleSearch")
    public Result wordModuleSearch(@RequestBody WordModuleSearchDto wordModuleSearchDto) {
        Page<WordModule> pageInfo = new Page<>(wordModuleSearchDto.getPageNum(), wordModuleSearchDto.getPageSize());
        LambdaQueryWrapper<WordModule> wrapper = new LambdaQueryWrapper<>();
        boolean flag = null != wordModuleSearchDto.getStudyNumberOrderByAsc();
        wrapper
                .ge(null != wordModuleSearchDto.getBeginTime(), WordModule::getWordModuleCreateTime, wordModuleSearchDto.getBeginTime())
                .le(null != wordModuleSearchDto.getEndTime(), WordModule::getWordModuleCreateTime, wordModuleSearchDto.getEndTime())
                .like(!wordModuleSearchDto.getModuleName().isEmpty(), WordModule::getModuleName, wordModuleSearchDto.getModuleName())
                .eq(!wordModuleSearchDto.getSuperiorModule().isEmpty(), WordModule::getSuperiorModule, wordModuleSearchDto.getSuperiorModule())
                .orderBy(flag, flag && wordModuleSearchDto.getStudyNumberOrderByAsc(), WordModule::getStudyNumber)
                .orderByDesc(WordModule::getWordModuleCreateTime);
        wordModuleService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 通过id获取模块详细信息
     * @Date: 2023/4/10 16:43
     */
    @GetMapping("wordModuleById/{moduleId}")
    public Result wordModuleById(@PathVariable Long moduleId) {
        WordModule wordModule = wordModuleService.getById(moduleId);
        Map<String, Object> map = new HashMap<>(1);
        LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<Plan>();
        queryWrapper.eq(Plan::getModuleId, moduleId);
        Boolean planExist = false;
        long count = planService.count();
        if (count > 0) {
            planExist = true;
        }
        map.put("wordModule", wordModule);
        map.put("planExist", planExist);
        return Result.success(map);
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 查询词汇模块列表By sueperName
     * @Date: 2023/4/10 10:44
     */
    @GetMapping("wordModuleBySuperior/{superiorModule}")
    public Result wordModuleBySuperior(@PathVariable String superiorModule) {
        LambdaQueryWrapper<WordModule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WordModule::getSuperiorModule, superiorModule);
        List<WordModule> list = wordModuleService.list(queryWrapper);
        return Result.success(list);
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 轮播图和 最新词汇的list
     * @Date: 2023/4/10 11:52
     */
    @GetMapping("newWordModule")
    public Result newWordModule() {
        Page<WordModule> pageInfo = new Page<>(1, 10);
        LambdaQueryWrapper<WordModule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(WordModule::getWordModuleStatus, "0")
                .orderByDesc(WordModule::getWordModuleCreateTime);
        wordModuleService.page(pageInfo, queryWrapper);
        List<WordModule> list = pageInfo.getRecords();
        return Result.success(list);
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 最热门词汇的list
     * @Date: 2023/4/10 11:52
     */
    @GetMapping("hotWordModule")
    public Result hotWordModule() {
        Page<WordModule> pageInfo = new Page<>(1, 10);
        LambdaQueryWrapper<WordModule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(WordModule::getWordModuleStatus, "0")
                .orderByDesc(WordModule::getStudyNumber);
        wordModuleService.page(pageInfo, queryWrapper);
        List<WordModule> list = pageInfo.getRecords();
        return Result.success(list);
    }

    /**
     * @param file:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 上传单词模块的图片
     * @Date: 2023/4/1 9:21
     */
    @PostMapping("uploadImage")
    public Result uploadImage(MultipartFile file) {

        String fileName = "module_image_" + IdUtils.getSnowFlakeInstance().nextIdStr() + "_"
                + String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))
                + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
        try {
            if (0 != file.getSize() && "image/jpeg".equals(file.getContentType())) {
                // 上传用户头像
                minIOUtils.putObject("word", file, fileName);
            } else {
                return Result.error("文件格式不是jpg或内容为空");
            }
        } catch (Exception e) {
//            log.error("上传头像失败");
            minIOUtils.removeObject("word", fileName);
            return Result.error("上传头像失败");
        }
        return Result.success(fileName);
    }

    /**
     * @param moduleId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 删除一个单词模块
     * @Date: 2023/4/1 9:25
     */
    @DeleteMapping("wordModule/{moduleId}")
    public Result wordModule(@PathVariable Long moduleId) {

        return Result.success();
    }


    /**
     * @param map:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 锁定和解锁模块
     * @Date: 2023/3/28 9:43
     */
    @PutMapping("lockOrUnLockModule")
    public Result<String> lockOrUnLockModule(@RequestBody Map<String, String> map) {
        System.out.println(map.toString());
        String lockStatus = "lock";
        String lockType = null;
        Long moduleId = null;
        try {
            lockType = map.get("lockType");
            moduleId = Long.valueOf(map.get("moduleId"));
            if (lockType == null || moduleId == null) {
                return Result.error("参数错误");
            }
        } catch (NumberFormatException e) {
            return Result.error("参数转换错误");
        }
        UpdateWrapper<WordModule> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("module_id", moduleId)
                .set("lock_time", LocalDateTime.now());
        if (lockStatus.equals(lockType)) {
            updateWrapper.set("word_module_status", "1");
        } else {
            updateWrapper.set("word_module_status", "0");
        }
        if (wordModuleService.update(updateWrapper)) {
            return Result.success("锁定/解锁成功");
        } else {
            return Result.error("锁定/解锁失败");
        }
    }

    /**
     * @param userId: 用户id
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 查询完成一次计划任务所需要的单词信息 已测试
     * @Date: 2023/2/12 12:37
     */
    @GetMapping("word/{userId}")
    public Result<Map> wordFromPlan(@PathVariable Long userId) throws IOException {
        Map map = new HashMap();
        List word = new ArrayList<ObjectNode>();
//        单词计划和单词模块信息
        WordPlan wordPlan = wordModuleService.selectWordPlan(userId);
//        词源
        SearchRequest request = new SearchRequest.Builder()
                .index(wordPlan.getEsIndex())
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


    /**
     * @param userId: 用户id
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 查询完成一个模块所需要的单词信息 已测试
     * @Date: 2023/2/12 12:37
     */
    @GetMapping("word/{moduleId}/{num}")
    public Result<Map> wordFromModule(@PathVariable Long moduleId, @PathVariable int num) throws IOException {
        Map map = new HashMap();
        List word = new ArrayList<ObjectNode>();
        /*todo size要可配置*/
        int size = 20;
        int from = (num - 1) * 20;
//        单词计划和单词模块信息
        WordModule wordModule = wordModuleService.getById(moduleId);
//        词源
        SearchRequest request = new SearchRequest.Builder()
                .index(wordModule.getEsIndex())
                .query(QueryBuilders.matchAll().build()._toQuery())
                .from(from)
                .size(size)
                .build();
        List<Hit<ObjectNode>> hits = elasticsearchClient.search(request, ObjectNode.class).hits().hits();
        for (Hit<ObjectNode> Hit : hits) {
            word.add(Hit.source());
        }
        WordPlan wordPlan = new WordPlan();
        wordPlan.setModuleName(wordModule.getModuleName());
        map.put("wordPlan", wordPlan);
        map.put("word", word);
        return Result.success(map);
    }


    /**
     * @param userId: 用户id
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 查询完成一个模块所需要的单词信息 已测试
     * @Date: 2023/2/12 12:37
     */
    @GetMapping("word/bookId{bookId}")
    public Result<List> wordFromBook(@PathVariable Long bookId) throws IOException {
//        List word = new ArrayList<ObjectNode>();
//        /*todo size要可配置*/
//        int size = 20;
//        int from = (num-1) * 20;
////        单词计划和单词模块信息
//        WordModule wordModule = wordModuleService.getById(moduleId);
////        词源
//        SearchRequest request = new SearchRequest.Builder()
//                .index(wordModule.get())
//                .query(QueryBuilders.matchAll().build()._toQuery())
//                .from(from)
//                .size(size)
//                .build();
//        List<Hit<ObjectNode>> hits = elasticsearchClient.search(request, ObjectNode.class).hits().hits();
//        for (Hit<ObjectNode> Hit : hits) {
//            word.add(Hit.source());
//        }
        return Result.success();
    }


    /**
     * @param wordModuleId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 获取单词模块头像, 已测试
     * @Date: 2023/2/9 11:44
     */
    @GetMapping("wordModuleImage/{wordModuleId}")
    public void wordImage(@PathVariable("wordModuleId") String wordModuleId, HttpServletResponse response) throws IOException, CustomException {

        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            //输出流，通过输出流将文件写回浏览器
            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            //从MinIo中获取用户头像
            String bucketName = "word";
            String objectName = wordModuleService.getById(wordModuleId).getModuleImagePath();
            inputStream = minIOUtils.getObject(bucketName, objectName);

            if (inputStream != null) {
                int len;
                byte[] bytes = new byte[1024 * 4];
                while ((len = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                    outputStream.flush();
                }
            } else {
                throw new CustomException("头像获取失败");
            }
        } catch (IOException e) {
            throw new CustomException("头像获取失败");
        } catch (CustomException e) {
            throw new CustomException("头像获取失败");
        } finally {
            //关闭资源
            if (inputStream != null) {
                inputStream.close();
            }
            if (inputStream != null) {
                outputStream.close();
            }
        }

    }

    /**
     * @param userId:
     * @param planId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 完成日常单词后
     * @Date: 2023/2/14 18:05
     */
    @PutMapping("dayWord/{userId}/{planId}")
    @Transactional
    public Result dayWord(@PathVariable Long userId, @PathVariable Long planId) {
//        更新计划表
        Plan plan = planService.getById(planId);
        plan.setFinishedWord(plan.getFinishedWord() + plan.getDayWord());
        if (plan.getFinishedWord() >= plan.getAllWord()) {
            plan.setPlanStatus("2");
        }
        log.info(plan.toString());
        Boolean flag = planService.updateById(plan);
//更新用户积分
        User user = userService.getById(userId);
        user.setIntegration(user.getIntegration() + plan.getDayWord());
        Boolean flag2 = userService.updateById(user);
        if (flag2 && flag) {
            return Result.success("完成计划后续更改成功");
        } else {
            return Result.error("后续更改失败");
        }
    }


}


