package com.boot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.result.Result;
import com.boot.dto.BookOfWordSearchDto;
import com.boot.dto.CollectWordsDto;
import com.boot.entity.BookOfWords;
import com.boot.entity.WordBooks;
import com.boot.service.BookOfWordsService;
import com.boot.service.WordBooksService;
import com.boot.utils.PdfUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;

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
    @RequiresAuthentication
    public Result words(@PathVariable Long wordId) {

        BookOfWords word = bookOfWordsService.getById(wordId);
        if (word == null) {
            return Result.error("单词不存在");
        }
        if (bookOfWordsService.removeById(wordId)) {
            /*修改单词本*/
            WordBooks wordBooks = wordBooksService.getById(word.getBookId());
            wordBooks
                    .setWordCount(wordBooks.getWordCount() - 1)
                    .setBookUpdateTime(LocalDateTime.now());
            wordBooksService.updateById(wordBooks);
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
    @RequiresAuthentication
    public Result wordss(@RequestBody BookOfWordSearchDto dto) {
        Page<BookOfWords> pageInfo = new Page<>(dto.getPageNum(), dto.getPageSize());
        LambdaQueryWrapper<BookOfWords> queryWrapper = new LambdaQueryWrapper<>();
        String oftenParam = dto.getSearch();
        queryWrapper
                .eq(BookOfWords::getBookId, dto.getBookId())
                .and(!oftenParam.isEmpty(),
                        e -> e.like(BookOfWords::getTrans, oftenParam)
                                .or().like(BookOfWords::getWord, oftenParam)
                )
                .orderByDesc(BookOfWords::getWordInsertTime);
        bookOfWordsService.page(pageInfo, queryWrapper);

        WordBooks wordBooks = wordBooksService.getById(dto.getBookId());

        Map<String, Object> map = new HashMap<>(2);
        map.put("word", pageInfo);
        map.put("book", wordBooks);
        return Result.success(map);
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 修改单词
     * @Date: 2023/5/3 16:32
     */
    @PutMapping("word")
    @RequiresAuthentication
    public Result word(@RequestBody BookOfWords bookOfWords) {
        if (bookOfWordsService.updateById(bookOfWords)) {
            return Result.success("提交成功");
        } else {
            return Result.error("未修改该单词");
        }
    }

    /**
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 收藏单词
     * @Date: 2023/2/27 19:28
     */
    @PostMapping("word")
    @RequiresAuthentication
    public Result word(@RequestBody CollectWordsDto collectWordsDto) {

        if (collectWordsDto.getBookId().size() == 0) {
            return Result.error("未选择单词本");
        }
        /*去重和计数初始化*/
        List<Long> bookIdList = new ArrayList<>(new LinkedHashSet<>(collectWordsDto.getBookId()));
        Map<Long, Integer> map = new HashMap<>(bookIdList.size());
        for (Long aLong : bookIdList) {
            map.put(aLong, 0);
        }
        /*计数和拼装数据*/
        List<BookOfWords> wordList = new ArrayList<>(collectWordsDto.getBookId().size());
        for (Long aLong : collectWordsDto.getBookId()) {
            BookOfWords word = new BookOfWords()
                    .setWord(collectWordsDto.getWord())
                    .setWordInsertTime(LocalDateTime.now())
                    .setBookId(aLong)
                    .setPos(collectWordsDto.getPos())
                    .setSentenceEn(collectWordsDto.getSentenceEn())
                    .setSentenceZh(collectWordsDto.getSentenceZh())
                    .setTrans(collectWordsDto.getTrans());
            wordList.add(word);
            map.put(aLong, map.get(aLong) + 1);
        }

        /*拼装数据*/
        List<WordBooks> booksList = new ArrayList<>(bookIdList.size());
        map.forEach((aLong, integer) -> {
            WordBooks wordBooks = wordBooksService.getById(aLong);
            wordBooks
                    .setBookUpdateTime(LocalDateTime.now())
                    .setWordCount(wordBooks.getWordCount() + integer);
            booksList.add(wordBooks);
        });
        /*存数*/
        if (bookOfWordsService.saveBatch(wordList) && wordBooksService.updateBatchById(booksList)) {
            return Result.success("收藏成功");
        } else {
            return Result.error("收藏失败");
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
    @RequiresAuthentication
    public void book(@PathVariable Long bookId, HttpServletResponse response) throws Exception {
        Map<String, Object> map = new HashMap<>(10);
        map.put("book_id", bookId);
        List<BookOfWords> bookOfWords = bookOfWordsService.listByMap(map);
        QueryWrapper<WordBooks> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("book_name").eq("book_id", bookId);
        String title = wordBooksService.getOne(queryWrapper).getBookName();
        pdfUtils.pdfExport(bookOfWords, title, response);
    }


}

