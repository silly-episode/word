package com.boot;

import com.boot.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/1 11:44
 * @FileName: RedisTest
 * @Description: 测试Redis连接
 */
@SpringBootTest
@Slf4j
public class RedisTest {

    @Autowired
    private RedisUtils redisUtils;

    @Test
    public void insertTest() {
        redisUtils.add("key", "value");
        boolean exists = redisUtils.hasKey("key");
        log.info(String.valueOf(exists));
    }
}
