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
     * 多重写一个support
     * 标识这个Realm是专门用来验证JwtToken，不负责验证其他的token（UsernamePasswordToken）
     * 必须重写此方法，不然Shiro会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        System.out.println("MyRealm doGetAuthorizationInfo() 方法授权 ");
        String token = principals.toString();
        String userId = jwtUtils.getUserId(token);
//        User user = userService.getById(Long.valueOf(userId));
        if (StringUtils.isBlank(userId)) {
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


    /**
     * 默认使用此方法进行用户名正确与否验证, 如果没有权限注解的话就不会去走上面的方法只会走这个方法
     * 其实就是 过滤器传过来的token 然后进行 验证 authenticationToken.toString() 获取的就是
     * 你的token字符串,然后你在里面做逻辑验证就好了,没通过的话直接抛出异常就可以了
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken auth) throws AuthenticationException {
        String token = (String) auth.getCredentials();
        // 解密获得userId，用于和数据库进行对比
        String userId = jwtUtils.getUserId(token);
//        解密获取的userId为空，则表示token不是我的
        if (userId == null) {
            throw new AuthenticationException("token invalid");
        }
//        根据userId获取user
        User userBean = userService.getById(Long.valueOf(userId));
//        查询不到则用户不存在
        if (userBean == null) {
            throw new AuthenticationException("用户不存在");
        }
//        将解密从数据库查询的userId与token进行校验，不一样则表示不安全
        if (!jwtUtils.verify(token, String.valueOf(userBean.getUserId()))) {

            throw new AuthenticationException("Username or password error");
        }
// ？？？？？？？？？？？？？？？
        return new SimpleAuthenticationInfo(token, token, "CommonUserRealm");
    }
}
