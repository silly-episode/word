package com.boot.controller;


import com.boot.service.WordBooksService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (WordBooks)表控制层
 *
 * @author makejava
 * @since 2023-02-25 10:39:57
 */
@RestController
@RequestMapping("wordBooks")
public class WordBooksController {
    /**
     * 服务对象
     */
    @Resource
    private WordBooksService wordBooksService;


}

