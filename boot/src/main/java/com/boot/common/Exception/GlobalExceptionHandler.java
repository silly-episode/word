package com.boot.common.Exception;

import com.boot.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
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
public class GlobalExceptionHandler {

    // shiro 异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    public String handler(ShiroException e){
        log.error("运行时shiro异常:------------------{}",e);
        return Result.error(4003,e.getMessage());
    }

    // 捕捉UnauthorizedException
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnauthorizedException.class)
    public String handle401() {
        return  Result.error(401, "Unauthorized");
    }

    // 业务大的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeErrorException.class)
    public String handler(RuntimeException e){
        log.error("运行时异常:------------------{}",e);
        return Result.error(40008,e.getMessage());
    }

    // 实体的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String handler(MethodArgumentNotValidException e){
        log.error("实体校验异常:------------------{}",e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError =  bindingResult.getAllErrors().stream().findFirst().get();

//        System.out.println(objectError.getDefaultMessage());
        return Result.error(4007,objectError.getDefaultMessage());
    }

    // Assert 断言的异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public String handler(IllegalArgumentException e){
        log.error("Assert 断言异常:------------------{}",e);
        return Result.error(e.getMessage());
    }

}
