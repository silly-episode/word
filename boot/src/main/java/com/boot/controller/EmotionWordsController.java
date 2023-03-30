package com.boot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.result.Result;
import com.boot.dto.EmotionWordsSearch;
import com.boot.entity.EmotionWords;
import com.boot.service.EmotionWordsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;


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
     * @param emotionWordsSearch:
     * @Return: Result<Page < EmotionWords>>
     * @Author: DengYinzhe
     * @Description: TODO 分页条件搜索
     * @Date: 2023/3/29 19:06
     */
    @PostMapping("emotionWordsSearch")
    public Result<Page<EmotionWords>> emotionWordsSearch(@RequestBody EmotionWordsSearch emotionWordsSearch) {


        return Result.success();
    }
}

