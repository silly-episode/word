package com.boot.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/3 12:40
 * @FileName: JwtUtils
 * @Description:
 */
@Slf4j
@Data
@Component
@ConfigurationProperties(prefix = "word.jwt")
public class JwtUtils {

    private String secret;
    private long expire;
    private String header;

    /**
     * @param token:
     * @param userId:
     * @Return: boolean
     * @Author: DengYinzhe
     * @Description: TODO
     * @Date: 2023/2/3 16:37
     */
    public boolean verify(String token, String userId) {

        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim("userId", userId)
                    .build();
            DecodedJWT decodeJwt = verifier.verify(token);
            // verify account
            String usernameInToken = decodeJwt.getClaim("userId").asString();
            return usernameInToken.equals(userId);
        } catch (TokenExpiredException e) {
//
            return false;
        } catch (IllegalArgumentException | JWTVerificationException e) {
            return false;
        }


    }

    /**
     * @param token:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 获得token中的信息无需secret解密也能获得
     * @Date: 2023/2/3 16:37
     */
    public String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }

    /**
     * @param userId:用户id
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 根据payload信息生成JSON WEB TOKEN
     * @Date: 2023/2/3 16:34
     */
    public String sign(String userId) {
        Date currentDate = new Date(System.currentTimeMillis());
        Date expireDate = new Date(System.currentTimeMillis() + expire * 1000);
        Algorithm algorithm = Algorithm.HMAC256(secret);

        return JWT.create()
                .withClaim("userId", userId)
                .withIssuedAt(currentDate)
                .withExpiresAt(expireDate)
                .sign(algorithm);

    }
}
