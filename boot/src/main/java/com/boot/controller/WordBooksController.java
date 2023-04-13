package com.boot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.boot.common.result.Result;
import com.boot.entity.WordBooks;
import com.boot.service.BookOfWordsService;
import com.boot.service.WordBooksService;
import com.boot.utils.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * (WordBooks)表控制层
 *
 * @author makejava
 * @since 2023-02-25 10:39:57
 */
@RestController
@RequestMapping("wordBooks")
@RequiresAuthentication
public class WordBooksController {
    /**
     * 服务对象
     */
    @Resource
    private WordBooksService wordBooksService;

    @Resource
    private BookOfWordsService bookOfWordsService;
    @Resource
    private JwtUtils jwtUtils;

    /**
     * @param bookName:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 新建单词本
     * @Date: 2023/2/25 10:56
     */
    @PostMapping("book")
    public Result wordBooks(@RequestParam String bookName, HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        assert userId != null;
        WordBooks wordBooks = new WordBooks()
                .setBookName(bookName)
                .setBookCreateTime(LocalDateTime.now())
                .setUserId(userId);

        if (wordBooksService.save(wordBooks)) {
            return Result.success("创建单词本成功");
        } else {
            return Result.error("创建单词本失败");
        }
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 根据Id获取所有的单词本
     * @Date: 2023/4/13 11:36
     */
    @GetMapping("allBook")
    public Result allBook(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        assert userId != null;
        LambdaQueryWrapper<WordBooks> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(WordBooks::getUserId, userId);
        List<WordBooks> wordBooks = wordBooksService.list(queryWrapper);
        return Result.success(wordBooks);
    }

    /**
     * @param bookId:
     * @param bookName:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 修改单词本名称
     * @Date: 2023/2/25 11:04
     */
    @PutMapping("book")
    public Result wordBooks(@RequestParam Long bookId, @RequestParam String bookName) {
        UpdateWrapper<WordBooks> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("book_id", bookId)
                .set("book_name", bookName)
                .set("book_update_time", LocalDateTime.now());
        if (wordBooksService.update(updateWrapper)) {
            return Result.success("修改单词本名称成功");
        } else {
            return Result.error("修改单词本名称失败");
        }
    }

    /**
     * @param bookId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 删除对应的单词和单词本
     * @Date: 2023/2/25 11:10
     */
    @DeleteMapping("book/{bookId}")
    @Transactional
    public Result wordBooks(@PathVariable Long bookId) {
        Map<String, Object> map = new HashMap<>();
        map.put("book_id", bookId);
        Boolean flag = wordBooksService.removeByMap(map);
        Boolean flag2 = bookOfWordsService.removeByMap(map);
        if (flag && flag2) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }


}

