package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.MattersDao;
import com.boot.entity.Matters;
import com.boot.service.MattersService;
import org.springframework.stereotype.Service;

/**
 * 待办事项表(Matters)表服务实现类
 *
 * @author makejava
 * @since 2023-05-07 10:58:44
 */
@Service("mattersService")
public class MattersServiceImpl extends ServiceImpl<MattersDao, Matters> implements MattersService {

}

