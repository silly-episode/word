package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.entity.EmotionWords;

/**
 * 激励语(EmotionWords)表服务接口
 *
 * @author makejava
 * @since 2023-03-29 16:56:57
 */
public interface EmotionWordsService extends IService<EmotionWords> {


    EmotionWords selectOneByRand(String frequency);
}

