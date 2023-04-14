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
 * @since 2023-04-14 13:35:35
 */
@Service("bookOfWordsService")
public class BookOfWordsServiceImpl extends ServiceImpl<BookOfWordsDao, BookOfWords> implements BookOfWordsService {

}

