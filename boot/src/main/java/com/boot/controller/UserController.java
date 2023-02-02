package com.boot.controller;




import com.boot.dto.LoginMessage;

import com.boot.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;


/**
 * 用户表(User)表控制层
 *
 * @author makejava
 * @since 2023-02-01 11:31:13
 */
@RestController
@RequestMapping("user")
public class UserController  {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    @PostMapping("loginPassword")
    public String loginPassword(@RequestBody LoginMessage loginMessage) {
        // 获取当前登录用户
        Subject subject = SecurityUtils.getSubject();

        try {
            // 执行登录操作
            subject.login(new UsernamePasswordToken(loginMessage.getLoginAccount(), loginMessage.getLoginPassword()));
            // 认证通过后直接跳转到index.jsp
            return "redirect:/index.jsp";
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误~");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误~");
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 如果认证失败仍然回到登录页面
        return "redirect:/login.jsp";
    }

    @PostMapping("loginSms")
    public String loginSms(@RequestBody LoginMessage loginMessage) {
        return "";
    }



}

