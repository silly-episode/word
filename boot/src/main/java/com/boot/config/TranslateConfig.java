package com.boot.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/22 21:41
 * @FileName: TranslateConfig
 * @Description:
 */
@Component
public class TranslateConfig implements InitializingBean {


    public static String SECRET_ID;
    public static String SECRET_KEY;
    public static String END_POINT;
    public static Long PROJECT_ID;
    public static String REGION;
    @Value("${tencent.translate.id}")
    private String secretId;
    @Value("${tencent.translate.secret}")
    private String secretKey;
    @Value("${tencent.translate.endPoint}")
    private String endPoint;
    @Value("${tencent.translate.projectId}")
    private Long projectId;
    @Value("${tencent.translate.region}")
    private String region;

    @Override
    public void afterPropertiesSet() throws Exception {
        SECRET_ID = secretId;
        SECRET_KEY = secretKey;
        END_POINT = endPoint;
        PROJECT_ID = projectId;
        REGION = region;
    }
}
