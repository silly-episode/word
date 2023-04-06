package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.AdminDao;
import com.boot.entity.Admin;
import com.boot.service.AdminService;
import org.springframework.stereotype.Service;

/**
 * 管理员表(Admin)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 11:36:23
 */
@Service("adminService")
public class AdminServiceImpl extends ServiceImpl<AdminDao, Admin> implements AdminService {

}

