package com.boot.controller;

import com.boot.common.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/25 9:22
 * @FileName: TestController
 * @Description: 测试的controller
 */
@RestController
@Slf4j
@RequestMapping("test")
@SuppressWarnings("all")
public class TestController {

    @GetMapping("test/{param}")
    @RequiresAuthentication
    public Result test(@PathVariable("param") String param) {
        System.out.println("Get: " + param);
        return Result.success();
    }

    @PostMapping("test")
    @RequiresGuest
    public Result test(@RequestBody Map<String, String> map) {
        System.out.println("Post: " + map.get("param"));
        return Result.success();
    }

    @PutMapping("test")
    public Result testt(@RequestBody Map<String, String> map) {
        System.out.println("Put: " + map.get("param"));
        return Result.success();
    }

    @DeleteMapping("test/{param}")
    public Result testt(@PathVariable("param") String param) {
        System.out.println("Delete: " + param);
        return Result.success();
    }

    @GetMapping("a")
    public Result a(@RequestBody Map<String, String> map) {
        return Result.success(map);
    }
}
