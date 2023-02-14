package com.boot;

import com.boot.dao.UserDao;
import com.boot.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/1 11:44
 * @FileName: MpTest
 * @Description: 测试mp和mysql
 */
@SpringBootTest
@Slf4j
public class MpTest{

    @Autowired
    private UserDao userDao;

    @Test
    public void mpTest1() {

        User user = userDao.selectById(1);
        log.info(user.toString());

    }
}
