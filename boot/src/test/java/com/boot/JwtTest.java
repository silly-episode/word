package com.boot;

import com.boot.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/5 14:07
 * @FileName: JwtTest
 * @Description:
 */

@SpringBootTest
public class JwtTest {

    @Resource
    private JwtUtils jwtUtils;

    private String token = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjI1NDMzNzgyODEsInVzZXJJZCI6IjEyMyIsImlhdCI6MTY3OTM3ODI4MX0.eE98TOhDDEdCe61zbvQZUdAB7ua1ThwA2U1DFepia1M";

    @Test
    public void signJwt() {
        this.token = jwtUtils.sign("123");
        System.out.println(token);

    }

    @Test
    public void accountTest() {
        System.out.println(jwtUtils.getUserId(token));
    }

    @Test
    public void verifyJwt() {
        System.out.println(jwtUtils.verify(this.token,
                "123"));
        System.out.println(jwtUtils.verify(this.token,
                "456"));
    }





}
