package com.boot.common.Exception;

import com.boot.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.management.RuntimeErrorException;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/3 11:29
 * @FileName: GlobalExceptionHandler
 * @Description: 全局异常类
 */
@Slf4j
@RestControllerAdvice
@SuppressWarnings("all")
public class GlobalExceptionHandler {


    /**
     * @param :
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 捕捉UnauthorizedException
     * @Date: 2023/2/12 21:10
     */
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public Result handle401() {
        return Result.error(401, "Unauthorized");
    }

//    /**
//     * @param e:
//     * @Return: Result
//     * @Author: DengYinzhe
//     * @Description: shiro 异常
//     * @Date: 2023/2/12 21:10
//     */
//    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(value = ShiroException.class)
//    public Result handler(ShiroException e) {
//        log.error("运行时shiro异常:", e);
//        return Result.error(4003, e.getMessage());
//    }

    /**
     * @param e:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 运行时异常
     * @Date: 2023/2/12 21:10
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeErrorException.class)
    public Result handler(RuntimeException e) {
        log.error("运行时异常:", e);
        return Result.error(40008, e.getMessage());
    }

    /**
     * @param e:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 实体的异常
     * @Date: 2023/2/12 21:10
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e) {
        log.error("实体校验异常:}", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.error(4007, objectError.getDefaultMessage());
    }

    /**
     * @param e:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: Assert 断言的异常
     * @Date: 2023/2/12 21:11
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e) {
        log.error("Assert 断言异常:", e);
        return Result.error(e.getMessage());
    }

    /**
     * @param ex:
     * @Return: Result<String>
     * @Author: DengYinzhe
     * @Description: 自定义异常
     * @Date: 2023/2/12 21:11
     */
    @ExceptionHandler(CustomException.class)
    public Result<String> exceptionHandler(CustomException ex) {
        return Result.error(ex.getMessage());
    }

}
