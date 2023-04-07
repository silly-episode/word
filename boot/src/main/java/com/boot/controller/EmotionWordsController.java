package com.boot.controller;


import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.ExcelImport.EmotionWordsImport;
import com.boot.common.result.Result;
import com.boot.dto.EmotionWordsSearchDto;
import com.boot.entity.EmotionWords;
import com.boot.service.EmotionWordsService;
import com.boot.utils.ThreadLocalUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;


/**
 * 激励语(EmotionWords)表控制层
 *
 * @author makejava
 * @since 2023-03-29 16:56:57
 */
@RestController
@RequestMapping("emotionWords")
public class EmotionWordsController {
    /**
     * 服务对象
     */
    @Resource
    private EmotionWordsService emotionWordsService;
    @Resource
    private EmotionWordsImport emotionWordsImport;

    /**
     * @param emotionWords:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 录入一条激励语
     * @Date: 2023/3/29 17:10
     */
    @PostMapping("emotionWords")
    public Result<String> emotionWords(@RequestBody EmotionWords emotionWords) {
        emotionWords.setEmoCreateTime(LocalDateTime.now());
        if (emotionWordsService.save(emotionWords)) {
            return Result.success("插入成功");
        } else {
            return Result.success("插入失败");
        }
    }

    /**
     * @param file:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: Excel导入
     * @Date: 2023/3/31 14:07
     */
    @PostMapping("emotionWordsExcel")
    public Result emotionWordsExcel(MultipartFile file) throws IOException {
        try {
            EasyExcel.read(
                            file.getInputStream(),
                            EmotionWords.class,
                            emotionWordsImport)
                    .sheet().doRead();
            List<String> errorList = (List<String>) ThreadLocalUtils.get("errorMsg");
            if (errorList.size() > 0) {
                return Result.result(4077, "部分导入成功", errorList);
            } else {
                return Result.success("导入成功");
            }
        } catch (IOException e) {
            return Result.error("导入失败");
        } finally {
            ThreadLocalUtils.remove("errorMsg");
        }
    }

    /**
     * @param emotionWords:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 更新一条激励语
     * @Date: 2023/3/29 17:17
     */
    @PutMapping("emotionWords")
    public Result<String> emotionWord(@RequestBody EmotionWords emotionWords) {
        if (emotionWordsService.updateById(emotionWords)) {
            return Result.success("更新成功");
        } else {
            return Result.success("更新失败");
        }
    }

    /**
     * @param emotionWordsList:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 批量删除
     * @Date: 2023/3/29 17:20
     */
    @DeleteMapping("emotionWords")
    public Result<String> emotionWord(@RequestBody List<EmotionWords> emotionWordsList) {
        if (emotionWordsService.removeBatchByIds(emotionWordsList)) {
            return Result.success("批量删除成功");
        } else {
            return Result.success("批量删除失败");
        }
    }

    /**
     * @param emotionWordsSearchDto:
     * @Return: Result<Page < EmotionWords>>
     * @Author: DengYinzhe
     * @Description: TODO 分页条件搜索
     * @Date: 2023/3/29 19:06
     */
    @PostMapping("emotionWordsSearch")
    public Result<Page<EmotionWords>> emotionWordsSearch(@RequestBody EmotionWordsSearchDto emotionWordsSearchDto) {
        Page<EmotionWords> pageInfo = new Page<>(emotionWordsSearchDto.getPageNum(), emotionWordsSearchDto.getPageSize());
        LambdaQueryWrapper<EmotionWords> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .ge(null != emotionWordsSearchDto.getBeginTime(), EmotionWords::getEmoCreateTime, emotionWordsSearchDto.getBeginTime())
                .le(null != emotionWordsSearchDto.getEndTime(), EmotionWords::getEmoCreateTime, emotionWordsSearchDto.getEndTime())
                .like(null != emotionWordsSearchDto.getAuthor(), EmotionWords::getEmoAuthor, emotionWordsSearchDto.getAuthor())
                .orderBy(null != emotionWordsSearchDto.getFrequencyOrderByAsc(), emotionWordsSearchDto.getFrequencyOrderByAsc(), EmotionWords::getFrequency)
                .orderByDesc(EmotionWords::getEmoCreateTime);
        emotionWordsService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    /**
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: 频率随机获取一条激励语言
     * @Date: 2023/3/31 15:13
     */
    @GetMapping("emotionWords")
    public Result<EmotionWords> emotionWords() {
        Random random = new Random();
        int num = random.nextInt(1000) + 1;
        String frequency = "中";
        if (num <= 200) {
            frequency = "低";
        } else if (num > 500) {
            frequency = "高";
        }
        EmotionWords emotionWords = emotionWordsService.selectOneByRand(frequency);
        return Result.success(emotionWords);
    }
}

