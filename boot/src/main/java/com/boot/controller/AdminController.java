package com.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.bo.ActionLogExcel;
import com.boot.bo.AdminExcel;
import com.boot.bo.LoginLogExcel;
import com.boot.bo.UserExcel;
import com.boot.common.result.Result;
import com.boot.dto.*;
import com.boot.entity.ActionLog;
import com.boot.entity.Admin;
import com.boot.entity.LoginLog;
import com.boot.entity.User;
import com.boot.service.ActionLogService;
import com.boot.service.AdminService;
import com.boot.service.LoginLogService;
import com.boot.service.UserService;
import com.boot.utils.ActionLogUtils;
import com.boot.utils.BeanDtoVoUtils;
import com.boot.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/16 17:58
 * @FileName: AdminController
 * @Description:
 */
@RestController
@RequestMapping("admin")
@Slf4j
public class AdminController {

    @Resource
    private UserService userService;
    @Resource
    private LoginLogService loginLogService;
    @Resource
    private AdminService adminService;
    @Resource
    private ActionLogService actionLogService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private ActionLogUtils actionLogUtils;
    /*重置的密码*/

    /**
     * @param loginMessage:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 管理员登录
     * @Date: 2023/3/26 17:36
     */
    @PostMapping("login")
    public Result adminLogin(@RequestBody LoginMessage loginMessage) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAccount, loginMessage.getLoginAccount());
        Admin admin = adminService.getOne(queryWrapper);
        if (admin == null) {
            return Result.error("用户不存在");
        }
        if (admin.getPassword().equals(loginMessage.getLoginPassword())) {
            String token = jwtUtils.sign(String.valueOf(admin.getAdminId()));
            return Result.success("登录成功", token);
        } else {
            return Result.error("密码不正确");
        }
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 添加管理员
     * @Date: 2023/4/7 19:48
     */
    @PostMapping("commonAdmin")
    public Result commonAdmin(@RequestBody Admin admin, HttpServletRequest request) {
        if ("".equals(admin.getAccount())) {
            return Result.error("参数缺失,请填写账户号");
        }
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getAccount, admin.getAccount());
        Admin adminFromDb = adminService.getOne(queryWrapper);
        if (adminFromDb != null) {
            return Result.error("该账号已存在");
        }
//        todo 默认密码可以配置
        admin
                .setPassword("123456")
                .setAddCreateTime(LocalDateTime.now());

        if (adminService.save(admin)) {
            actionLogUtils.saveActionLog(request, actionLogUtils.INSERT, "添加了持有人为 " + admin.getKeepName() + " 的管理员");
            return Result.success("添加管理员成功");
        } else {
            return Result.error("添加管理员失败");
        }
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 管理员搜索
     * @Date: 2023/4/7 20:06
     */
    @PostMapping("adminSearch")
    public Result adminSearch(@RequestBody AdminSearchDto adminSearchDto) {
        /*分页*/
        Page<Admin> pageInfo = new Page<>(adminSearchDto.getPageNum(), adminSearchDto.getPageSize());
        /*条件*/
        LambdaQueryWrapper<Admin> adminQueryWrapper = adminService.getAdminQueryWrapper(adminSearchDto);
        /*查询*/
        adminService.page(pageInfo, adminQueryWrapper);
        /*admin状态转换*/
        pageInfo.setRecords(adminService.translateAdminStatus(pageInfo.getRecords()));
        /*转换*/
        Page<AdminMsgDto> pageInfoDto = BeanDtoVoUtils.pageVo(pageInfo, AdminMsgDto.class);

        return Result.success(pageInfoDto);
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 管理员导出
     * @Date: 2023/4/8 10:11
     */
    @PostMapping("adminListExcel")
    public void adminListExcel(@RequestBody AdminSearchDto adminSearchDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*记录日志*/
        actionLogUtils.saveActionLog(request, actionLogUtils.EXPORT, "导出管理员信息表");
        /*构造条件*/
        LambdaQueryWrapper<Admin> queryWrapper = adminService.getAdminQueryWrapper(adminSearchDto);
        /*查询数据*/
        List<Admin> adminList = adminService.list(queryWrapper);
        /*状态转换*/
        adminList = adminService.translateAdminStatus(adminList);
        /*转换*/
        List<AdminExcel> adminExcelList = BeanDtoVoUtils.convertList(adminList, AdminExcel.class);
        adminService.importExcel(response, "Word-管理员", AdminExcel.class, adminExcelList);
    }


    /**
     * @param map:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 管理员重置密码
     * @Date: 2023/3/28 9:28
     */
    @PutMapping("password")
    public Result<String> password(@RequestBody Map<String, Long> map, HttpServletRequest request) {
        Long userId = map.get("userId");
        // todo 这里的密码要从数据库中查询出来
        String userResetPassword = "123456";
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("user_id", userId)
                .set("password", userResetPassword);
        if (userService.update(updateWrapper)) {
            User user = userService.getById(userId);
            actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "重置用户名为 " + user.getNickName() + " 的用户密码为 " + userResetPassword);
            return Result.success("重置密码成功");
        } else {
            return Result.error("重置密码失败");
        }
    }


    /**
     * @param map:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 重置管理员密码
     * @Date: 2023/3/28 9:28
     */
    @PutMapping("adminPassword")
    public Result<String> adminPassword(@RequestBody Map<String, Long> map, HttpServletRequest request) {
        long adminId = map.get("adminId");
        // todo 这里的密码要从数据库中查询出来
        String userResetPassword = "123456";
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("admin_id", adminId)
                .set("password", userResetPassword);
        if (adminService.update(updateWrapper)) {
            Admin admin = adminService.getById(adminId);
            actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "重置了持有人为 " + admin.getKeepName() + " 的管理员密码为 " + userResetPassword);
            return Result.success("重置密码成功");
        } else {
            return Result.error("重置密码失败");
        }
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 删除用户
     * @Date: 2023/4/6 20:22
     */
    @DeleteMapping("user/{userId}")
    public Result<String> user(@PathVariable Long userId, HttpServletRequest request) {
        User user = userService.getById(userId);
        if (user == null) {
            return Result.error("该用户不存在");
        } else {


            actionLogUtils.saveActionLog(request, actionLogUtils.DELETE, "删除了用户名为 " + user.getNickName() + " 的用户");
            return Result.success("删除用户成功");
        }


    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 删除管理员
     * @Date: 2023/4/6 20:22
     */
    @DeleteMapping("admin/{adminId}")
    public Result<String> admin(@PathVariable Long adminId, HttpServletRequest request) {
        Admin admin = adminService.getById(adminId);
        if (admin == null) {
            return Result.error("管理员不存在");
        }
        if (adminService.removeById(adminId)) {
            actionLogUtils.saveActionLog(request, actionLogUtils.DELETE, "删除了持有人为 " + admin.getKeepName() + " 的管理员");
            return Result.success("删除管理员成功");
        } else {
            return Result.error("删除管理员失败");
        }


    }

    /**
     * @param map:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 管理员修改描述
     * @Date: 2023/3/28 9:29
     */
    @PutMapping("remark")
    public Result<String> remark(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Long userId = Long.valueOf(map.get("userId"));
        assert userId != null;
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("user_id", userId)
                .set("remark", map.get("remark"));
        if (userService.update(updateWrapper)) {
            User user = userService.getById(userId);
            actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "修改了用户名为 " + user.getNickName() + " 的用户描述");
            return Result.success("修改描述成功");
        } else {
            return Result.error("修改描述失败");
        }
    }


    /**
     * @param map:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 修改管理员描述
     * @Date: 2023/3/28 9:29
     */
    @PutMapping("adminRemark")
    public Result<String> adminRemark(@RequestBody Map<String, String> map, HttpServletRequest request) {
        Long userId = Long.valueOf(map.get("adminId"));
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("admin_id", userId)
                .set("remark", map.get("remark"));
        if (adminService.update(updateWrapper)) {
            Admin admin = adminService.getById(userId);
            actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "修改了持有人为 " + admin.getKeepName() + " 的管理员描述");
            return Result.success("修改描述成功");
        } else {
            return Result.error("修改描述失败");
        }
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 管理员修改用户信息
     * @Date: 2023/4/7 11:17
     */
    @PutMapping("user")
    public Result<String> user(@RequestBody User user, HttpServletRequest request) {
        String delete = "待删除";
        String lock = "锁定";
        String userStatus = user.getUserStatus();
        User userFromDb = userService.getById(user.getUserId());
        user
                .setHeadImage(userFromDb.getHeadImage())
                .setPassword(userFromDb.getPassword())
                .setSalt(userFromDb.getSalt());
        if (delete.equals(userStatus)) {
            user.setUserStatus("2");
        } else if (lock.equals(userStatus)) {
            user.setUserStatus("1");
        } else {
            user.setUserStatus("0");
        }
        if (userService.updateById(user)) {
            actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "修改了用户名为 " + user.getNickName() + " 的用户基础信息");
            return Result.success("修改用户信息成功");
        } else {
            return Result.error("修改用户信息失败");
        }
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 超级管理员修改管理员信息
     * @Date: 2023/4/7 11:17
     */
    @PutMapping("admin")
    public Result<String> admin(@RequestBody Admin admin, HttpServletRequest request) {
        String lock = "锁定";
        String superRole = "0";
        String adminStatus = admin.getUserStatus();
        Admin adminFromDb = adminService.getById(admin.getAdminId());
        if (superRole.equals(adminFromDb.getRole())) {
            return Result.error("不能修改超级管理员信息");
        }
        admin
                .setPassword(adminFromDb.getPassword())
                .setSalt(adminFromDb.getSalt());
        if (lock.equals(adminStatus)) {
            admin.setUserStatus("1");
        } else {
            admin.setUserStatus("0");
        }
        if (adminService.updateById(admin)) {
            actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "修改了持有人为 " + admin.getKeepName() + " 的管理员基础信息");
            return Result.success("修改管理员信息成功");
        } else {
            return Result.error("修改管理员信息失败");
        }
    }

    /**
     * @param map:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 锁定和解锁用户
     * @Date: 2023/3/28 9:43
     */
    @PutMapping("lockOrUnLockUser")
    public Result<String> lockOrUnLockUser(@RequestBody Map<String, String> map, HttpServletRequest request) {
        System.out.println(map.toString());
        String lockStatus = "lock";
        String lockType = null;
        Long userId = null;
        try {
            lockType = map.get("lockType");
            userId = Long.valueOf(map.get("userId"));
            if (lockType == null || userId == null) {
                return Result.error("参数错误");
            }
        } catch (NumberFormatException e) {
            return Result.error("参数转换错误");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("user_id", userId)
                .set("lock_time", LocalDate.now());
        if (lockStatus.equals(lockType)) {
            updateWrapper.set("user_status", "1");
        } else {
            updateWrapper.set("user_status", "0");
        }
        if (userService.update(updateWrapper)) {
            User user = userService.getById(userId);
            if (lockStatus.equals(lockType)) {
                actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "锁定了用户名为 " + user.getNickName() + " 的用户");
            } else {
                actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "解锁了用户名为 " + user.getNickName() + " 的用户");
            }
            return Result.success("锁定/解锁成功");
        } else {
            return Result.error("锁定/解锁失败");
        }
    }


    /**
     * @param map:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 锁定和解锁管理员
     * @Date: 2023/3/28 9:43
     */
    @PutMapping("lockOrUnLockAdmin")
    public Result<String> lockOrUnLockAdmin(@RequestBody Map<String, String> map, HttpServletRequest request) {
        String lockStatus = "lock";
        String lockType = null;
        Long adminId = null;
        try {
            lockType = map.get("lockType");
            adminId = Long.valueOf(map.get("adminId"));
            if (lockType == null || adminId == null) {
                return Result.error("参数错误");
            }
        } catch (NumberFormatException e) {
            return Result.error("参数转换错误");
        }
        UpdateWrapper<Admin> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("admin_id", adminId)
                .set("lock_time", LocalDate.now());
        if (lockStatus.equals(lockType)) {
            updateWrapper.set("user_status", "1");
        } else {
            updateWrapper.set("user_status", "0");
        }
        if (adminService.update(updateWrapper)) {
            Admin admin = adminService.getById(adminId);
            if (lockStatus.equals(lockType)) {
                actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "锁定了持有人为 " + admin.getKeepName() + " 的用户");
            } else {
                actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "解锁了持有人为 " + admin.getKeepName() + " 的用户");
            }
            return Result.success("锁定/解锁成功");
        } else {
            return Result.error("锁定/解锁失败");
        }
    }


    /**
     * @param userSearchDto:
     * @Return: Result<Page < UserMsgDto2>>
     * @Author: DengYinzhe
     * @Description: 多条件查询用户列表
     * @Date: 2023/3/28 9:02
     */
    @PostMapping("userSearch")
//    @RequiresAuthentication
    public Result<Page<UserMsgDto2>> userSearch(@RequestBody UserSearchDto userSearchDto) {
        /*分页*/
        Page<User> pageInfo = new Page<>(userSearchDto.getPageNum(), userSearchDto.getPageSize());
        /*条件*/
        LambdaQueryWrapper<User> wrapper = adminService.getUserQueryWrapper(userSearchDto);
        /*查询*/
        userService.page(pageInfo, wrapper);
        /*用户状态转换*/
        pageInfo.setRecords(adminService.translateUserStatus(pageInfo.getRecords()));
        /*转换*/
        Page<UserMsgDto2> pageInfoDto = BeanDtoVoUtils.pageVo(pageInfo, UserMsgDto2.class);
        return Result.success(pageInfoDto);
    }

    /**
     * @param response:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 用户信息的导出（excel）
     * @Date: 2023/3/17 14:13
     */
    @PostMapping("userListExcel")
    public void userListExcel(@RequestBody UserSearchDto userSearchDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*记录日志*/
        actionLogUtils.saveActionLog(request, actionLogUtils.EXPORT, "导出了用户信息表");
        /*查询条件*/
        LambdaQueryWrapper<User> userQueryWrapper = adminService.getUserQueryWrapper(userSearchDto);
        /*查询数据*/
        List<User> userList = userService.list(userQueryWrapper);
        /*转换*/
        List<UserExcel> userExcelList = BeanDtoVoUtils.convertList(userList, UserExcel.class);
        /*导出*/
        adminService.importExcel(response, "Word-用户", UserExcel.class, userExcelList);
    }

    /**
     * @param logSearch:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 登录日志分页查询
     * @Date: 2023/3/27 10:03
     */
    @PostMapping("commonUserLog")
    public Result<Page<LoginLog>> commonUserLog(@RequestBody LoginLogSearchDto logSearch) {
        /*分页*/
        Page<LoginLog> pageInfo = new Page<>(logSearch.getPageNum(), logSearch.getPageSize());
        /*构造查询条件*/
        LambdaQueryWrapper<LoginLog> queryWrapper = adminService.getLoginLogQueryWrapper(logSearch);
        /*分页查询数据*/
        loginLogService.page(pageInfo, queryWrapper);
        /*转换用户状态*/
        pageInfo.setRecords(adminService.translateLogUserStatus(pageInfo.getRecords()));
        return Result.success(pageInfo);
    }


    /**
     * @param logSearch:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 登录日志导出
     * @Date: 2023/3/27 10:03
     */
    @PostMapping("logExcelImport")
    public void logExcelImport(@RequestBody LoginLogSearchDto logSearch, HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*记录操作日志*/
        actionLogUtils.saveActionLog(request, actionLogUtils.EXPORT, "导出用户登录日志");
        /*获取查询条件*/
        LambdaQueryWrapper<LoginLog> queryWrapper = adminService.getLoginLogQueryWrapper(logSearch);
        /*查询数据*/
        List<LoginLog> logList = loginLogService.list(queryWrapper);
        /*类型转换，方便Excel导出*/
        List<LoginLogExcel> list = BeanDtoVoUtils.convertList(logList, LoginLogExcel.class);
        /*开始导出，并放入response*/
        adminService.importExcel(response, "Word-登录日志", LoginLogExcel.class, list);
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 分页获取操作日志
     * @Date: 2023/4/13 11:54
     */
    @PostMapping("actionLogSearch")
    public Result selectAll(@RequestBody ActionLogSearchDto searchDto) {
//        分页
        Page<ActionLog> pageInfo = new Page<>(searchDto.getPageNum(), searchDto.getPageSize());
//        构造条件
        LambdaQueryWrapper<ActionLog> wrapper = adminService.getActionLogQueryWrapper(searchDto);
//        查询数据
        actionLogService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    /**
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 操作日志导出
     * @Date: 2023/3/27 10:03
     */
    @PostMapping("actionLogExcelImport")
    public void actionLogExcelImport(@RequestBody ActionLogSearchDto searchDto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*记录操作日志*/
        actionLogUtils.saveActionLog(request, actionLogUtils.EXPORT, "导出管理员操作日志");
        /*获取查询条件*/
        LambdaQueryWrapper<ActionLog> queryWrapper = adminService.getActionLogQueryWrapper(searchDto);
        /*查询数据*/
        List<ActionLog> logList = actionLogService.list(queryWrapper);
        /*转换*/
        logList = adminService.translateActionLogStatus(logList);
        /*类型转换，方便Excel导出*/
        List<ActionLogExcel> list = BeanDtoVoUtils.convertList(logList, ActionLogExcel.class);
        /*开始导出，并放入response*/
        adminService.importExcel(response, "Word-操作日志", ActionLogExcel.class, list);


    }
}


