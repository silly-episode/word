package com.boot.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.AdminDao;
import com.boot.dto.ActionLogSearchDto;
import com.boot.dto.AdminSearchDto;
import com.boot.dto.LoginLogSearchDto;
import com.boot.dto.UserSearchDto;
import com.boot.entity.ActionLog;
import com.boot.entity.Admin;
import com.boot.entity.LoginLog;
import com.boot.entity.User;
import com.boot.service.AdminService;
import com.boot.utils.JsonUtils;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * 管理员表(Admin)表服务实现类
 *
 * @author makejava
 * @since 2023-04-03 11:36:23
 */
@Service("adminService")
public class AdminServiceImpl extends ServiceImpl<AdminDao, Admin> implements AdminService {

    @Override
    public <V> void importExcel(HttpServletResponse response, String fileName, Class<V> v, List<V> list) throws IOException {
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String finalFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + finalFileName + ".xlsx");
            // 这里需要设置不关闭流
            System.out.println(v == v.getDeclaredConstructor().newInstance().getClass());
            EasyExcel.write(response.getOutputStream(), v).autoCloseStream(Boolean.FALSE).sheet()
                    .doWrite(list);
        } catch (Exception e) {
            // 重置response
            response.reset();
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            Map<String, String> map = MapUtils.newHashMap();
            map.put("status", "failure");
            map.put("message", "下载文件失败" + e.getMessage());
            response.getWriter().println(JsonUtils.getBeanToJson(map));
        }
    }

    @Override
    public LambdaQueryWrapper<User> getUserQueryWrapper(UserSearchDto userSearchDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        String oftenParam = userSearchDto.getAccountOrTelOrNickNameOrUserId();
        boolean flag = (null != userSearchDto.getIntegrationOrderByAsc());
        wrapper
                .ge(null != userSearchDto.getBeginTime(), User::getRegisterTime, userSearchDto.getBeginTime())
                .le(null != userSearchDto.getEndTime(), User::getRegisterTime, userSearchDto.getEndTime())
                .eq(!userSearchDto.getUserStatus().isEmpty(), User::getUserStatus, userSearchDto.getUserStatus())
                .and(!oftenParam.isEmpty(),
                        e -> e.like(User::getNickName, oftenParam)
                                .or().eq(User::getAccount, oftenParam)
                                .or().eq(User::getTel, oftenParam)
                                .or().eq(User::getUserId, oftenParam)
                )
                .orderBy(flag, flag && userSearchDto.getIntegrationOrderByAsc(), User::getIntegration)
                .orderByDesc(User::getRegisterTime);
        return wrapper;
    }

    @Override
    public LambdaQueryWrapper<LoginLog> getLoginLogQueryWrapper(LoginLogSearchDto logSearch) {
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        String oftenParam = logSearch.getAccountOrTelOrNickNameOrUserId();
        wrapper
                .ge(null != logSearch.getBeginTime(), LoginLog::getLoginTime, logSearch.getBeginTime())
                .le(null != logSearch.getEndTime(), LoginLog::getLoginTime, logSearch.getEndTime())
                .eq(!logSearch.getLoginType().isEmpty(), LoginLog::getLoginType, logSearch.getLoginType())
                .eq(!logSearch.getResult().isEmpty(), LoginLog::getResult, logSearch.getResult())
                .and(!oftenParam.isEmpty(),
                        e -> e.like(LoginLog::getNickName, oftenParam)
                                .or().eq(LoginLog::getAccount, oftenParam)
                                .or().eq(LoginLog::getTel, oftenParam)
                                .or().eq(LoginLog::getUserId, oftenParam)
                )
                .orderByDesc(LoginLog::getLoginTime);
        return wrapper;
    }


    @Override
    public LambdaQueryWrapper<ActionLog> getActionLogQueryWrapper(ActionLogSearchDto searchDto) {
        LambdaQueryWrapper<ActionLog> wrapper = new LambdaQueryWrapper<>();
        String oftenParam = searchDto.getSearch();
        wrapper
                .ge(null != searchDto.getBeginTime(), ActionLog::getActionTime, searchDto.getBeginTime())
                .le(null != searchDto.getEndTime(), ActionLog::getActionTime, searchDto.getEndTime())
                .eq(!searchDto.getActionType().isEmpty(), ActionLog::getActionKind, searchDto.getActionType())
                .and(!oftenParam.isEmpty(),
                        e -> e.like(ActionLog::getKeepName, oftenParam)
                                .or().like(ActionLog::getRemark, oftenParam)
                                .or().eq(ActionLog::getAdminId, oftenParam)
                )
                .orderByDesc(ActionLog::getActionTime);
        return wrapper;
    }

    @Override
    public LambdaQueryWrapper<Admin> getAdminQueryWrapper(AdminSearchDto adminSearchDto) {
        LambdaQueryWrapper<Admin> wrapper = new LambdaQueryWrapper<>();
        String oftenParam = adminSearchDto.getAccountOrTelOrKeepNameOrAdminId();
        wrapper
                .ge(null != adminSearchDto.getBeginTime(), Admin::getAddCreateTime, adminSearchDto.getBeginTime())
                .le(null != adminSearchDto.getEndTime(), Admin::getAddCreateTime, adminSearchDto.getEndTime())
                .eq(!adminSearchDto.getUserStatus().isEmpty(), Admin::getUserStatus, adminSearchDto.getUserStatus())
                .and(!oftenParam.isEmpty(),
                        e -> e.like(Admin::getKeepName, oftenParam)
                                .or().eq(Admin::getAccount, oftenParam)
                                .or().eq(Admin::getTel, oftenParam)
                                .or().eq(Admin::getAdminId, oftenParam)
                )
                .orderByDesc(Admin::getAddCreateTime);
        return wrapper;
    }

    @Override
    public List<User> translateUserStatus(List<User> userList) {
        for (User user : userList) {
            String status = user.getUserStatus();
            if ("0".equals(status)) {
                user.setUserStatus("正常");
            } else if ("1".equals(status)) {
                user.setUserStatus("锁定");
            } else if ("2".equals(status)) {
                user.setUserStatus("待删除");
            }
        }
        return userList;
    }


    @Override
    public List<LoginLog> translateLogUserStatus(List<LoginLog> logList) {
        for (LoginLog log : logList) {
            String status = log.getUserStatus();
            if ("0".equals(status)) {
                log.setUserStatus("正常");
            } else if ("1".equals(status)) {
                log.setUserStatus("锁定");
            } else if ("2".equals(status)) {
                log.setUserStatus("待删除");
            }
        }
        return logList;
    }


    @Override
    public List<ActionLog> translateActionLogStatus(List<ActionLog> logList) {

        /*
        INSERT: '插入',
        INSERT_BATCH: '批量插入',
        DELETE: '删除',
        DELETE_BATCH: '批量删除',
        UPDATE: '修改',
        EXPORT:'导出'
        */

        for (ActionLog log : logList) {
            String role = log.getRole();
            String actionKind = log.getActionKind();
            if ("0".equals(role)) {
                log.setRole("超级管理员");
            } else if ("1".equals(role)) {
                log.setRole("普通管理员");
            }

            if ("INSERT".equals(actionKind)) {
                log.setActionKind("插入");
            } else if ("INSERT_BATCH".equals(actionKind)) {
                log.setActionKind("批量插入");
            } else if ("DELETE".equals(actionKind)) {
                log.setActionKind("删除");
            } else if ("DELETE_BATCH".equals(actionKind)) {
                log.setActionKind("批量删除");
            } else if ("UPDATE".equals(actionKind)) {
                log.setActionKind("修改");
            } else if ("EXPORT".equals(actionKind)) {
                log.setActionKind("导出");
            }

        }
        return logList;
    }

    @Override
    public List<Admin> translateAdminStatus(List<Admin> adminList) {
        for (Admin admin : adminList) {
            String userStatus = admin.getUserStatus();
            String role = admin.getRole();
            /*用户状态*/
            if ("0".equals(userStatus)) {
                admin.setUserStatus("正常");
            } else if ("1".equals(userStatus)) {
                admin.setUserStatus("锁定");
            }
            /*管理员身份*/
            if ("0".equals(role)) {
                admin.setRole("超级管理员");
            } else if ("1".equals(role)) {
                admin.setRole("普通管理员");
            }

        }
        return adminList;
    }
}

