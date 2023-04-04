package com.boot.common.Hutool;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/4 18:10
 * @FileName: IdUtils
 * @Description: 单例模式-饿汉式
 */
public class IdUtils {

    private static final Snowflake INSTANCE = IdUtil.createSnowflake(1, 1);


    private IdUtils() {

    }


    public static Snowflake getSnowFlakeInstance() {
        return INSTANCE;
    }
}
