package com.boot.common.Exception;

import com.boot.common.result.Result;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/7 14:49
 * @FileName: CustomException
 * @Description: 自定义异常类
 */
public class CustomException extends Exception{

    private final Result result;

    public CustomException(Result result) {
        this.result = result;
    }


}
