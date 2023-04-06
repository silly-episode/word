package com.boot.utils;

import java.util.Random;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/1 17:10
 * @FileName: SaltUtil
 * @Description: 生成随机盐工具
 */
public class SaltUtil {
    public static String getSalt(int n) {
        char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890!@#$%^&*()".toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            char c = chars[new Random().nextInt(chars.length)];
            sb.append(c);
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        System.out.println(getSalt(4));
    }
}
