package com.boot.controller;


import com.boot.common.result.Result;
import com.boot.entity.BookOfWords;
import com.boot.service.BookOfWordsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (BookOfWords)表控制层
 *
 * @author makejava
 * @since 2023-02-25 10:40:36
 */
@RestController
@RequestMapping("bookOfWords")
public class BookOfWordsController {
    /**
     * 服务对象
     */
    @Resource
    private BookOfWordsService bookOfWordsService;


    /**
     * @param wordId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 取消收藏
     * @Date: 2023/2/25 15:13
     */
    @DeleteMapping("word/{wordId}")
    public Result words(@PathVariable Long wordId) {
        if (bookOfWordsService.removeById(wordId)) {
            return Result.success("删除成功");
        }else {
            return Result.error("删除失败");
        }
    }

    /**
     * @param bookId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 获取练习的单词
     * @Date: 2023/2/25 15:20
     */
    @GetMapping("word/{bookId}")
    public Result wordss(@PathVariable Long bookId) {
        Map<String, Object> map = new HashMap<>();
        map.put("book_id", bookId);
        List<BookOfWords> list = bookOfWordsService.listByMap(map);
        return Result.success(list);
    }

    /**
     * @param bookOfWords:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 收藏单词
     * @Date: 2023/2/27 19:28
     */
    @PostMapping("word")
    public Result word(@RequestBody BookOfWords bookOfWords) {
        bookOfWords.setWordInsertTime(LocalDateTime.now());
        if (bookOfWordsService.save(bookOfWords)) {
            return Result.success("插入成功");
        }else {
            return Result.error("插入失败");
        }
    }

    /**
     * @param bookId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 单词本导出为pdf
     * @Date: 2023/2/25 15:30
     */
    @GetMapping("book/{bookId}")
    public Result book (@PathVariable Long bookId){

        return Result.success();
    }


}

