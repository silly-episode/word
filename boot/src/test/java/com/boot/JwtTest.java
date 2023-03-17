package com.boot;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.junit.jupiter.api.Test;

import java.util.Date;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/5 14:07
 * @FileName: JwtTest
 * @Description:
 */

public class JwtTest {

    private final String secret = "123";
    private final long expire = 10000000;
    private final String header = "Authorization";

    @Test
    public void signJwt() {

        System.out.println(sign("jack"));
    }

    @Test
    public void accountTest() {
        System.out.println(getAccount(""));
    }

    @Test
    public void verifyJwt() {

        System.out.println(verify("", ""));
    }


    public boolean verify(String token, String account) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        JWTVerifier verifier = JWT.require(algorithm)
                .withClaim("account", account)
                .build();
        DecodedJWT decodeJwt = verifier.verify(token);
        // verify account
        String usernameInToken = decodeJwt.getClaim("account").asString();
        return usernameInToken.equals(account);
    }

    public String getAccount(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("account").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    public String sign(String account) {
        Date currentDate = new Date(System.currentTimeMillis());
        Date expireDate = new Date(System.currentTimeMillis() + expire * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("account", account)
                .withIssuedAt(currentDate)
                .withExpiresAt(expireDate)
                .sign(algorithm);

    }
}
