package com.boot.controller;


import com.boot.service.BookOfWordsService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

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

}

