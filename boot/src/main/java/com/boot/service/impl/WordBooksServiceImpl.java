package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.WordBooksDao;
import com.boot.entity.WordBooks;
import com.boot.service.WordBooksService;
import org.springframework.stereotype.Service;

/**
 * (WordBooks)表服务实现类
 *
 * @author makejava
 * @since 2023-02-25 10:39:58
 */
@Service("wordBooksService")
public class WordBooksServiceImpl extends ServiceImpl<WordBooksDao, WordBooks> implements WordBooksService {

}

