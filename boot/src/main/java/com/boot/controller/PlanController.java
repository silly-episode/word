package com.boot.controller;


import com.boot.common.result.Result;
import com.boot.entity.Plan;
import com.boot.service.PlanService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 学习计划(Plan)表控制层
 *
 * @author makejava
 * @since 2023-02-12 12:03:01
 */
@RestController
@Slf4j
@RequestMapping("plan")
@SuppressWarnings("all")
//@RequiresAuthentication
public class PlanController  {


    @Resource
    private PlanService planService;

    @PostMapping("plan")
    public Result plan(@RequestBody Plan plan) {
        plan.setPlanCreateTime(LocalDateTime.now());
        if ( planService.save(plan)) {
            return Result.success("创建计划成功");
        } else {
            return Result.error("创建计划失败");
        }
    }

//    @PutMapping("plan")
//    public Result plan(@RequestBody Plan plan) {
//
//    }


}

