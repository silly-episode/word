package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.BookOfWordsDao;
import com.boot.entity.BookOfWords;
import com.boot.service.BookOfWordsService;
import org.springframework.stereotype.Service;

/**
 * (BookOfWords)表服务实现类
 *
 * @author makejava
 * @since 2023-02-25 10:40:36
 */
@Service("bookOfWordsService")
public class BookOfWordsServiceImpl extends ServiceImpl<BookOfWordsDao, BookOfWords> implements BookOfWordsService {

}

