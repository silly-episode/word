package com.boot;

import com.boot.entity.Plan;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/15 14:02
 * @FileName: CommonTest
 * @Description: 普通test
 */
@Slf4j
public class CommonTest {


    @Test
    public void test1() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Plan plan = (Plan) Class.forName("com.boot.entity.Plan").getDeclaredConstructor().newInstance();
        Method[] methods = plan.getClass().getMethods();

        log.info(Arrays.toString(methods));

    }


}
