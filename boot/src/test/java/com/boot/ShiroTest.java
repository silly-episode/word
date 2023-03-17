package com.boot;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/2 13:46
 * @FileName: ShiroTest
 * @Description: shiroTest
 */

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.text.IniRealm;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

public class ShiroTest {


    @Test
    public static void main(String[] args) {
        // 1、创建安全管理器对象
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        // 2、给安全管理器设置realm
        securityManager.setRealm(new IniRealm("classpath:shiro.ini"));
        // 3、给全局安全工具类SecurityUtils设置安全管理器
        SecurityUtils.setSecurityManager(securityManager);
        // 4、拿到当前的subject
        Subject subject = SecurityUtils.getSubject();
        // 5、创建令牌
        AuthenticationToken token = new UsernamePasswordToken("christy", "123456");

//        认证的几种状态
//        UnknownAccountException：用户名错误
//        IncorrectCredentialsException：密码错误
//        DisabledAccountException：账号被禁用
//        LockedAccountException：账号被锁定
//        ExcessiveAttemptsException：登录失败次数过多
//        ExpiredCredentialsException：凭证过期

        try {
            // 6、用户认证
            System.out.println("认证状态：" + subject.isAuthenticated());
            subject.login(token);
            System.out.println("认证状态：" + subject.isAuthenticated());
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("认证失败：用户不存在！");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("认证失败：密码不正确！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("测试完毕");
    }
}

