package com.boot.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.UserDao;
import com.boot.entity.LoginLog;
import com.boot.entity.User;
import com.boot.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2023-02-01 11:31:13
 */
@Service("userService")
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Resource
    private UserDao userDao;

    @Override
    public User getUserByAccount(String account) {
        QueryWrapper<User> query = new QueryWrapper<User>();
        query.eq("account", account);
        return userDao.selectOne(query);
    }

    @Override
    public User getUserByTel(String phone) {
        QueryWrapper<User> query = new QueryWrapper<User>();
        query.eq("tel", phone);
        return userDao.selectOne(query);
    }


    @Override
    public LoginLog userToLoginLog(User userBean, LoginLog loginLog) {
        loginLog
                .setTel(userBean.getTel())
                .setUserId(userBean.getUserId())
                .setAccount(userBean.getAccount())
                .setNickName(userBean.getNickName())
                .setUserStatus(userBean.getUserStatus());
        return loginLog;
    }
}

