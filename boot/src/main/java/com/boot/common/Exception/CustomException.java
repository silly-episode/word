package com.boot.common.Exception;


/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/7 14:49
 * @FileName: CustomException
 * @Description: 自定义异常类
 */
public class CustomException extends RuntimeException {
    public CustomException(String message) {
        super(message);
    }

}
