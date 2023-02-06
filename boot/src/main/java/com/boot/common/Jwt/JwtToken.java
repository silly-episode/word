package com.boot.common.Jwt;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/3 17:22
 * @FileName: JwtToken
 * @Description:
 */
public class JwtToken implements AuthenticationToken {


    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}
