package com.boot.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.util.MapUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.bo.UserExcel;
import com.boot.entity.User;
import com.boot.service.UserService;
import com.boot.utils.BeanDtoVoUtils;
import com.boot.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
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
//        log.info(JsonUtils.getBeanToJson(userExcelList));

        try {
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
            String fileName = URLEncoder.encode("word-用户信息表", StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
            // 这里需要设置不关闭流
            EasyExcel.write(response.getOutputStream(), UserExcel.class).autoCloseStream(Boolean.FALSE).sheet("模板")
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

//       return Result.success();
    }
}
