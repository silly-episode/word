package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.LoginLogDao;
import com.boot.entity.LoginLog;
import com.boot.service.LoginLogService;
import org.springframework.stereotype.Service;

/**
 * (LoginLog)表服务实现类
 *
 * @author makejava
 * @since 2023-03-25 19:18:19
 */
@Service("loginLogService")
public class LoginLogServiceImpl extends ServiceImpl<LoginLogDao, LoginLog> implements LoginLogService {

}

