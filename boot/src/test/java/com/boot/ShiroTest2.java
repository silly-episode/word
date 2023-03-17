package com.boot;

import com.boot.common.MyRealm;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/2 14:10
 * @FileName: CustomerRealmAuthenticatorTest
 * @Description:
 */
public class ShiroTest2 {
    @Test
    public static void main(String[] args) {

//        得到最终的subject 需要账号、密码、随机盐、散列次数
        // 创建SecurityManager
        DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
        // 设置自定义realm
        MyRealm realm = new MyRealm();
        // 为realm设置凭证匹配器
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher();
        // 设置加密算法
        credentialsMatcher.setHashAlgorithmName("md5");
        // 设置hash次数
        credentialsMatcher.setHashIterations(1024);
        realm.setCredentialsMatcher(credentialsMatcher);
        defaultSecurityManager.setRealm(realm);
        // 设置安全工具类
        SecurityUtils.setSecurityManager(defaultSecurityManager);
        // 通过安全工具类获取subject
        Subject subject = SecurityUtils.getSubject();
        // 创建token
        UsernamePasswordToken token = new UsernamePasswordToken("christy", "123456");
        try {
            // 登录认证
            subject.login(token);
            System.out.println("认证成功");
        } catch (UnknownAccountException e) {
            e.printStackTrace();
            System.out.println("用户名错误");
        } catch (IncorrectCredentialsException e) {
            e.printStackTrace();
            System.out.println("密码错误");
        }

//        认证的几种状态
//
//        UnknownAccountException：用户名错误
//
//        IncorrectCredentialsException：密码错误
//
//        DisabledAccountException：账号被禁用
//
//        LockedAccountException：账号被锁定
//
//        ExcessiveAttemptsException：登录失败次数过多
//
//        ExpiredCredentialsException：凭证过期


        System.out.println("==============================================");
        //授权
        if (subject.isAuthenticated()) {
            //基于角色权限控制
            System.out.println(subject.hasRole("super"));
            System.out.println("==============================================");
            //基于多角色权限控制(同时具有)
            System.out.println(subject.hasAllRoles(Arrays.asList("admin", "super")));
            System.out.println("==============================================");
            //是否具有其中一个角色
            boolean[] booleans = subject.hasRoles(Arrays.asList("admin", "super", "user"));
            for (boolean aBoolean : booleans) {
                System.out.println(aBoolean);
            }

            System.out.println("==============================================");

            //基于权限字符串的访问控制  资源标识符:操作:资源类型
            System.out.println("权限:" + subject.isPermitted("user:update:01"));
            System.out.println("权限:" + subject.isPermitted("product:create:02"));

            //分别具有那些权限
            boolean[] permitted = subject.isPermitted("user:*:01", "order:*:10");
            for (boolean b : permitted) {
                System.out.println(b);
            }

            //同时具有哪些权限
            boolean permittedAll = subject.isPermittedAll("user:*:01", "product:create:01");
            System.out.println(permittedAll);
        }
    }
}
