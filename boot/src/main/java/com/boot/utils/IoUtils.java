package com.boot.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/9 14:07
 * @FileName: IoUtils
 * @Description:
 */
public class IoUtils {

    /**
     * @param input:
     * @Return: byte
     * @Author: DengYinzhe
     * @Description: TODO
     * @Date: 2023/2/9 14:08
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int n = 0;
        while (-1 != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }
}
