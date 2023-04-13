package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.SwearDao;
import com.boot.entity.Swear;
import com.boot.service.SwearService;
import org.springframework.stereotype.Service;

/**
 * 发誓表(Swear)表服务实现类
 *
 * @author makejava
 * @since 2023-04-13 15:50:03
 */
@Service("swearService")
public class SwearServiceImpl extends ServiceImpl<SwearDao, Swear> implements SwearService {

}

