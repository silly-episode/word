package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.WordModuleDao;
import com.boot.entity.WordModule;
import com.boot.service.WordModuleService;
import org.springframework.stereotype.Service;

/**
 * (WordModule)表服务实现类
 *
 * @author makejava
 * @since 2023-02-08 15:30:50
 */
@Service("wordModuleService")
public class WordModuleServiceImpl extends ServiceImpl<WordModuleDao, WordModule> implements WordModuleService {

}

