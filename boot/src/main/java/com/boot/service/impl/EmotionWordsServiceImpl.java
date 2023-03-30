package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.EmotionWordsDao;
import com.boot.entity.EmotionWords;
import com.boot.service.EmotionWordsService;
import org.springframework.stereotype.Service;

/**
 * 激励语(EmotionWords)表服务实现类
 *
 * @author makejava
 * @since 2023-03-29 16:56:57
 */
@Service("emotionWordsService")
public class EmotionWordsServiceImpl extends ServiceImpl<EmotionWordsDao, EmotionWords> implements EmotionWordsService {

}

