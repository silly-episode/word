package com.boot.common.Aop;

import com.boot.entity.LoginLog;
import com.boot.service.LoginLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Field;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/27 13:31
 * @FileName: LogAspect
 * @Description: 日志的Aop
 */
@Aspect
@Component
public class LogAspect {


    @Resource
    private LoginLogService loginLogService;

    /**
     * @param joinPoint:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 登录后录入登录日志
     * @Date: 2023/3/28 8:59
     */
    @After("execution(* com.boot.controller.UserController.login(..))")
    public void saveCommonUserLoginLog(JoinPoint joinPoint) {
        try {
            // 获取目标方法所在的类的 Class 对象
            Class<?> clazz = joinPoint.getTarget().getClass();
            // 获取目标方法所在类中声明的所有成员变量
            Field[] fields = clazz.getDeclaredFields();
            // 遍历成员变量数组，查找目标方法中定义的变量
            for (Field field : fields) {
                if ("loginLog".equals(field.getName()) && field.getType().equals(LoginLog.class)) {
                    // 找到目标方法中定义的变量，使用反射获取变量的值
                    field.setAccessible(true);
                    LoginLog loginLog = (LoginLog) field.get(joinPoint.getTarget());
                    loginLogService.save(loginLog);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
