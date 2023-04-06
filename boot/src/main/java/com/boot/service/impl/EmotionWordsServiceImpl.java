package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.EmotionWordsDao;
import com.boot.entity.EmotionWords;
import com.boot.service.EmotionWordsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 激励语(EmotionWords)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 16:56:57
 */
@Service("emotionWordsService")
public class EmotionWordsServiceImpl extends ServiceImpl<EmotionWordsDao, EmotionWords> implements EmotionWordsService {

    @Resource
    private EmotionWordsDao emotionWordsDao;

    @Override
    public EmotionWords selectOneByRand(String frequency) {
        System.out.println("123");
        return emotionWordsDao.selectOneByRand(frequency);
    }
}

