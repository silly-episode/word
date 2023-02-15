package com.boot.common.Realm;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.boot.common.Jwt.JwtToken;
import com.boot.entity.User;
import com.boot.service.UserService;
import com.boot.utils.JwtUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/3 17:27
 * @FileName: CommonUserRealm
 * @Description:
 */
@Service
public class CommonUserRealm extends AuthorizingRealm {


    @Resource
    private JwtUtils jwtUtils;


    @Resource
    private UserService userService;


    /**
     * 大坑！，必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("MyRealm doGetAuthorizationInfo() 方法授权 ");
        String token = principals.toString();
        String username = jwtUtils.getAccount(token);
        User user = userService.getUserByAccount(username);
        if (StringUtils.isBlank(username)) {
            throw new AuthenticationException("token认证失败");
        }
        //查询当前
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

        //查询数据库来获取用户的角色
//        info.addRole(String.valueOf(user.getType()));
        //查询数据库来获取用户的权限
        //info.addStringPermission(String.valueOf(user.getType()));
        return info;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得username，用于和数据库进行对比
        String username = jwtUtils.getAccount(token);
        if (username == null) {
            throw new AuthenticationException("token invalid");
        }

        User userBean = userService.getUserByAccount(username);
        if (userBean == null) {
            throw new AuthenticationException("User didn't existed!");
        }

        if (!jwtUtils.verify(token, userBean.getAccount())) {
            throw new AuthenticationException("Username or password error");
        }

        return new SimpleAuthenticationInfo(token, token, "my_realm");
    }
}
