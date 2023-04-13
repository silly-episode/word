package com.boot.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.dto.ActionLogSearchDto;
import com.boot.dto.AdminSearchDto;
import com.boot.dto.LoginLogSearchDto;
import com.boot.dto.UserSearchDto;
import com.boot.entity.ActionLog;
import com.boot.entity.Admin;
import com.boot.entity.LoginLog;
import com.boot.entity.User;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 管理员表(Admin)表服务接口
 *
 * @author makejava
 * @since 2023-04-03 11:36:22
 */
public interface AdminService extends IService<Admin> {

    <V> void importExcel(HttpServletResponse response, String fileName, Class<V> v, List<V> list) throws IOException;

    LambdaQueryWrapper<User> getUserQueryWrapper(UserSearchDto userSearchDto);

    LambdaQueryWrapper<LoginLog> getLoginLogQueryWrapper(LoginLogSearchDto logSearch);

    LambdaQueryWrapper<ActionLog> getActionLogQueryWrapper(ActionLogSearchDto logSearch);

    LambdaQueryWrapper<Admin> getAdminQueryWrapper(AdminSearchDto adminSearchDto);

    List<User> translateUserStatus(List<User> userList);

    List<LoginLog> translateLogUserStatus(List<LoginLog> logList);

    List<ActionLog> translateActionLogStatus(List<ActionLog> logList);

    List<Admin> translateAdminStatus(List<Admin> adminList);
}

