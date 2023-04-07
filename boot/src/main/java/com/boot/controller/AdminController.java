package com.boot.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.bo.LoginLogExcel;
import com.boot.bo.UserExcel;
import com.boot.common.result.Result;
import com.boot.dto.LoginLogSearchDto;
import com.boot.dto.LoginMessage;
import com.boot.dto.UserMsgDto2;
import com.boot.dto.UserSearchDto;
import com.boot.entity.LoginLog;
import com.boot.entity.User;
import com.boot.service.AdminService;
import com.boot.service.LoginLogService;
import com.boot.service.UserService;
import com.boot.utils.BeanDtoVoUtils;
import com.boot.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;
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

        return Result.success();
    }

    /**
     * @param map:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 管理员重置密码
     * @Date: 2023/3/28 9:28
     */
    @PutMapping("password")
    public Result<String> password(@RequestBody Map<String, Long> map) {
        // todo 这里的密码要从数据库中查询出来
        String userResetPassword = "123456";
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("user_id", map.get("userId"))
                .set("password", userResetPassword);
        if (userService.update(updateWrapper)) {
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
    public Result<String> user(@PathVariable Long userId) {

        return Result.success("删除用户成功");
    }

    /**
     * @param map:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: TODO 管理员修改描述
     * @Date: 2023/3/28 9:29
     */
    @PutMapping("remark")
    public Result<String> remark(@RequestBody Map<String, String> map) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper
                .eq("user_id", map.get("userId"))
                .set("remark", map.get("remark"));
        if (userService.update(updateWrapper)) {
            return Result.success("修改描述成功");
        } else {
            return Result.error("修改描述失败");
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
    public Result<String> lockUser(@RequestBody Map<String, String> map) {
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
        Page<UserMsgDto2> pageInfoDto = null;
        try {
            Page<User> pageInfo = new Page<>(userSearchDto.getPageNum(), userSearchDto.getPageSize());
            LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
            String oftenParam = userSearchDto.getAccountOrTelOrNickNameOrUserId();
            boolean flag = (null != userSearchDto.getIntegrationOrderByAsc());
            String userStatus;
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
            userService.page(pageInfo, wrapper);
            for (User record : pageInfo.getRecords()) {
                userStatus = record.getUserStatus();
                if ("0".equals(userStatus)) {
                    record.setUserStatus("正常");
                } else if ("1".equals(userStatus)) {
                    record.setUserStatus("锁定");
                } else if ("2".equals(userStatus)) {
                    record.setUserStatus("待删除");
                }
            }
            pageInfoDto = BeanDtoVoUtils.pageVo(pageInfo, UserMsgDto2.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.success(pageInfoDto);
    }

    /**
     * @param response:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 用户信息的导出（excel）
     * @Date: 2023/3/17 14:13
     */
    @GetMapping("userListExcel")
    public void userListExcel(HttpServletResponse response) throws IOException {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("register_time");
        List<User> userList = userService.list(queryWrapper);
        List<UserExcel> userExcelList = new ArrayList<>(userList.size());
        for (User user : userList) {
            if ("0".equals(user.getUserStatus())) {
                user.setUserStatus("正常");
            } else if ("1".equals(user.getUserStatus())) {
                user.setUserStatus("锁定");
            } else if ("2".equals(user.getUserStatus())) {
                user.setUserStatus("待删除");
            }
            userExcelList.add(BeanDtoVoUtils.convert(user, UserExcel.class));
        }
        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("word-用户信息表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), UserExcel.class).autoCloseStream(Boolean.FALSE).sheet()
                    .doWrite(userExcelList);
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

    /**
     * @param logSearch:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 登录日志分页查询和日志导出
     * @Date: 2023/3/27 10:03
     */
    @PostMapping("commonUserLog")
    public Result<Page<LoginLog>> commonUserLog(@RequestBody LoginLogSearchDto logSearch, HttpServletResponse response) throws IOException {
        /*查询信息*/
        Page<LoginLog> pageInfo = new Page<>(logSearch.getPageNum(), logSearch.getPageSize());
        LambdaQueryWrapper<LoginLog> wrapper = new LambdaQueryWrapper<>();
        String oftenParam = logSearch.getAccountOrTelOrNickNameOrUserId();
        String userStatus;
        wrapper
                .ge(null != logSearch.getBeginTime(), LoginLog::getLoginTime, logSearch.getBeginTime())
                .le(null != logSearch.getEndTime(), LoginLog::getLoginTime, logSearch.getEndTime())
                .eq(null != logSearch.getLoginType(), LoginLog::getLoginType, logSearch.getLoginType())
                .eq(null != logSearch.getResult(), LoginLog::getResult, logSearch.getResult())
                .and(null != oftenParam,
                        e -> e.like(LoginLog::getNickName, oftenParam)
                                .or().eq(LoginLog::getAccount, oftenParam)
                                .or().eq(LoginLog::getTel, oftenParam)
                                .or().eq(LoginLog::getUserId, oftenParam)
                )
                .orderByDesc(LoginLog::getLoginTime);
        loginLogService.page(pageInfo, wrapper);

        for (LoginLog record : pageInfo.getRecords()) {
            userStatus = record.getUserStatus();
            if ("0".equals(userStatus)) {
                record.setUserStatus("正常");
            } else if ("1".equals(userStatus)) {
                record.setUserStatus("锁定");
            } else if ("2".equals(userStatus)) {
                record.setUserStatus("待删除");
            }
        }
        /*是否导出的逻辑*/
        if (logSearch.getExport() != null && logSearch.getExport()) {
            List<LoginLogExcel> list = BeanDtoVoUtils.convertList(pageInfo.getRecords(), LoginLogExcel.class);
            try {
                response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                response.setCharacterEncoding("utf-8");
                // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
                String fileName = URLEncoder.encode("word-用户登录日志表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
                response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
                // 这里需要设置不关闭流
                EasyExcel.write(response.getOutputStream(), LoginLogExcel.class).autoCloseStream(Boolean.FALSE).sheet()
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
            return null;
        } else {
            return Result.success(pageInfo);
        }
    }


}
