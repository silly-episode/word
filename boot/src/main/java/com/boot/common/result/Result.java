package com.boot.common.result;

import org.springframework.http.HttpStatus;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/30 10:01
 * @FileName: Result
 * @Description: 响应返回结果
 */
public class Result<T> {
    /** 结果状态 ,正常响应200，其他状态码都为失败*/
    private int code;
    private String msg;
    private T data;

    // Static methods
    /**
     * 成功时候的调用
     */
    public static <T> Result<T> success(T data) {
        return new Result<T>(data, CodeMsg.SUCCESS);
    }
    public static <T> Result<T> success() {
        return new Result<T>(CodeMsg.SUCCESS);
    }

    /**
     * 失败时候的调用
     */
    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<T>(code, msg);
    }
    public static <T> Result<T> error(CodeMsg codeMsg) {
        return new Result<T>(codeMsg);
    }
    public static <T> Result<T> error(String msg) {
        CodeMsg codeMsg = new CodeMsg(HttpStatus.INTERNAL_SERVER_ERROR.value(), msg);
        return new Result<T>(codeMsg);
    }

    // Constructor
    private Result(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(T data, CodeMsg codeMsg) {
        this.data = data;
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }
    private Result(CodeMsg codeMsg) {
        if (codeMsg != null) {
            this.code = codeMsg.getCode();
            this.msg = codeMsg.getMsg();
        }
    }
}
