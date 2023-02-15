package com.boot.common;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/1 13:53
 * @FileName: MyRealm
 * @Description:
 */
public class MyRealm extends AuthorizingRealm {

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
//        授权逻辑
        // 从系统返回的身份信息集合中获取主身份信息（用户名）
        String primaryPrincipal = (String) principalCollection.getPrimaryPrincipal();
        System.out.println("1用户名: " + primaryPrincipal);

        //根据用户名获取当前用户的角色信息,以及权限信息
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        //将数据库中查询角色信息赋值给权限对象
        simpleAuthorizationInfo.addRole("admin");
        simpleAuthorizationInfo.addRole("user");

        //将数据库中查询权限信息赋值个权限对象
        simpleAuthorizationInfo.addStringPermission("user:*:01");
        simpleAuthorizationInfo.addStringPermission("product:create");

        return simpleAuthorizationInfo;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        //        认证逻辑

        // 在token中获取用户名
        String principal = (String) authenticationToken.getPrincipal();
        System.out.println(principal);
        // 模拟根据身份信息从数据库查询
        if ("christy".equals(principal)) {
            /**
             * 用户名
             * 加密后的密码
             * 随机盐
             * 当前realm的名称
             */
            return new SimpleAuthenticationInfo(principal,
                    "41a4e25bcf1272844e38b19047dd68a0",
                    ByteSource.Util.bytes("1q2w3e"),
                    this.getName());
        }

        return null;
    }
}
