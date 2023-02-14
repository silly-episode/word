package com.boot.controller;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.boot.bo.WordPlan;
import com.boot.common.result.Result;
import com.boot.dao.WordModuleDao;
import com.boot.entity.Plan;
import com.boot.entity.WordModule;
import com.boot.service.PlanService;
import com.boot.service.WordModuleService;
import com.boot.vo.PlanVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class PlanController {


    @Resource
    private PlanService planService;


    @Resource
    private WordModuleService wordModuleService;

    /**
     * @param userId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 查询所有的计划 已测试
     * @Date: 2023/2/14 14:49
     */
    @GetMapping("plan/{userId}")
    public Result plan(@PathVariable Long userId) {
        Map map = new HashMap<>();
        List<PlanVo> list = planService.selectAll(userId);
        WordPlan wordPlan = wordModuleService.selectWordPlan(userId);
        map.put("mainPlan", wordPlan);
        map.put("commonPlan", list);
        return Result.success(map);
    }


    /**
     * @param plan:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 创建计划  已测试
     * @Date: 2023/2/14 13:31
     */
    @PostMapping("plan")
    public Result plan(@RequestBody Plan plan) {
        plan.setPlanCreateTime(LocalDateTime.now());
        if (planService.save(plan)) {
            return Result.success("创建计划成功");
        } else {
            return Result.error("创建计划失败");
        }
    }

    /**
     * @param planId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 删除计划 已测试
     * @Date: 2023/2/14 15:19
     */
    @DeleteMapping("plan/{planId}")
    public Result planDelete(@PathVariable Long planId) {
        if ("1".equals(planService.getById(planId).getPlanStatus())) {
            return Result.error("无法删除主计划");
        } else if (planService.removeById(planId)) {
            return Result.success("删除计划成功");
        } else {
            return Result.error("删除计划失败");
        }
    }

    /**
     * @param :
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 查询所有的单词模块信息 已测试
     * @Date: 2023/2/14 13:46
     */
    @GetMapping("wordModule")
    public Result wordModule() {

        List<WordModule> list = new WordModule().selectAll();

        return Result.success(list);
    }

    /**
     * @param planId:
     * @param dayWord:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 更改某个计划天数 已测试
     * @Date: 2023/2/14 15:57
     */
    @PutMapping("plan/{planId}/{dayWord}")
    public Result plan(@PathVariable Long planId,@PathVariable Integer dayWord){
        UpdateWrapper<Plan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("day_word", dayWord).eq("plan_id", planId);
        if (planService.update(updateWrapper)) {
            return Result.success("更改成功");
        }else {
            return Result.error("更改失败");
        }
    }
    /**
     * @param oldPlanId:
     * @param newPlanId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 更换主计划 已测试
     * @Date: 2023/2/14 16:07
     */
    @PutMapping("mainPlan/{oldPlanId}/{newPlanId}")
    public Result mainPlan(@PathVariable Long oldPlanId,@PathVariable Long newPlanId){
        Plan oldPlan = new Plan();
        Plan newPlan = new Plan();
        List<Plan> list = new ArrayList<>();
        oldPlan.setPlanId(oldPlanId);
        oldPlan.setPlanStatus("0");
        newPlan.setPlanId(newPlanId);
        newPlan.setPlanStatus("1");
        list.add(oldPlan);
        list.add(newPlan);
        System.out.println(oldPlan.toString());
        System.out.println(newPlan.toString());
        log.info(oldPlan.toString());
        log.info(newPlan.toString());
        if (planService.updateBatchById(list,2)) {
            return Result.success("更改成功");
        }else {
            return Result.error("更改失败");
        }
    }

}

