package com.boot.controller;




import com.boot.common.result.CodeMsg;
import com.boot.common.result.Result;
import com.boot.dto.LoginMessage;

import com.boot.entity.User;
import com.boot.service.UserService;
import com.boot.utils.JsonUtils;
import com.boot.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.UnauthorizedException;
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
@Slf4j
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;

    @Resource
    private JwtUtils jwtUtils;


    @GetMapping("sms")
    public String getCode() {


    }


    @PostMapping("loginPassword")
    public String loginPassword(@RequestBody LoginMessage loginMessage) {
        System.out.println("123123123");
        log.info("123213123");


        User userBean = userService.getUserByAccount(loginMessage.getLoginAccount());
        if (null == userBean) {
            return JsonUtils.getBeanToJson(Result.error(CodeMsg.BAD_CREDENTIAL));
        } else if (userBean.getPassword().equals(loginMessage.getLoginPassword())) {
            return JsonUtils.getBeanToJson(Result.success(jwtUtils.sign(loginMessage.getLoginAccount())));
        } else {
            throw new UnauthorizedException();
        }
    }

    @PostMapping("loginSms")
    public String loginSms(@RequestBody LoginMessage loginMessage) {

        User userBean = userService.getUserByAccount(loginMessage.getLoginAccount());
        if (null == userBean) {
            return JsonUtils.getBeanToJson(Result.error(CodeMsg.BAD_CREDENTIAL));
        } else if (userBean.getPassword().equals(loginMessage.getLoginPassword())) {
            return JsonUtils.getBeanToJson(Result.success(jwtUtils.sign(loginMessage.getLoginAccount())));
        } else {
            throw new UnauthorizedException();
        }
    }

    @GetMapping("test")
    public String test() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.isAuthenticated()) {
            return "You are already logged in";
        } else {
            return "You are guest";
        }
    }
}

