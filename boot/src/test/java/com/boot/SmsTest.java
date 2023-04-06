package com.boot;


import com.boot.utils.RandomUtils;
import com.boot.utils.SmsUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/26 14:30
 * @FileName: test2
 * @Description:
 */
@SpringBootTest
public class SmsTest {


    @Autowired
    private SmsUtils smsUtils;


    @Test
    public void smsTest() {
        boolean result = smsUtils.sendMessage("13142397682", RandomUtils.getSixBitRandom());
        System.out.println(result);
    }


}
