package com.boot.controller;

import com.boot.common.result.Result;
import com.boot.entity.User;
import com.boot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/16 17:58
 * @FileName: AdminController
 * @Description:
 */
@RestController
@RequestMapping("admin")
public class AdminController {

    @Resource
    private UserService userService;


    @GetMapping("userListExcel/{userList}")
    public Result userListExcel(@PathVariable("userList")List<User> userList) {

       return Result.success();
    }

}
