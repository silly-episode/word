package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.bo.WordPlan;
import com.boot.entity.WordModule;


/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/8 12:55
 * @FileName: WordModuleService
 * @Description: 单词模块接口
 */
public interface WordModuleService extends IService<WordModule> {

    /**
     * @param userId:
     * @Return: WordPlan
     * @Author: DengYinzhe
     * @Description: 查询正在执行的单词计划
     * @Date: 2023/2/14 15:48
     */
    WordPlan selectWordPlan(Long userId);

}
