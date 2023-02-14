package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.bo.WordPlan;
import com.boot.entity.WordModule;
import org.apache.ibatis.annotations.Param;


/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/8 12:55
 * @FileName: WordModuleService
 * @Description: 单词模块接口
 */
public interface WordModuleService extends IService<WordModule> {

    WordPlan selectWordPlan( Long userId);

}
