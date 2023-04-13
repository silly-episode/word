package com.boot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.result.Result;
import com.boot.dto.BookOfWordSearchDto;
import com.boot.entity.BookOfWords;
import com.boot.entity.WordBooks;
import com.boot.service.BookOfWordsService;
import com.boot.service.WordBooksService;
import com.boot.utils.PdfUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
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
@RequiresAuthentication
public class BookOfWordsController {
    /**
     * 服务对象
     */
    @Resource
    private BookOfWordsService bookOfWordsService;
    @Resource
    private WordBooksService wordBooksService;
    @Resource
    private PdfUtils pdfUtils;

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
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 获取练习的单词
     * @Date: 2023/2/25 15:20
     */
    @PostMapping("wordSearch")
    public Result wordss(@RequestBody BookOfWordSearchDto dto) {
        Page<BookOfWords> pageInfo = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<BookOfWords> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BookOfWords::getBookId, dto.getBookId());
        bookOfWordsService.page(pageInfo, queryWrapper);

        WordBooks wordBooks = wordBooksService.getById(dto.getBookId());

        Map<String, Object> map = new HashMap<>(2);
        map.put("word", pageInfo);
        map.put("book", wordBooks);
        return Result.success(map);
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
        } else {
            return Result.error("插入失败");
        }
    }

    /**
     * @param bookId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 单词本导出为pdf
     * @Date: 2023/2/25 15:30
     */
    @GetMapping("book/{bookId}")
    public byte[] book(@PathVariable Long bookId) throws Exception {
        Map<String, Object> map = new HashMap<>(10);
        map.put("book_id", bookId);
        List<BookOfWords> bookOfWords = bookOfWordsService.listByMap(map);
        QueryWrapper<WordBooks> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("book_name").eq("book_id", bookId);
        String title = wordBooksService.getOne(queryWrapper).getBookName();
        return pdfUtils.pdfExport(bookOfWords, title);
    }


}

