package com.boot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.boot.bo.WordPlan;
import com.boot.common.result.Result;
import com.boot.dto.PlanVo;
import com.boot.entity.Plan;
import com.boot.entity.WordModule;
import com.boot.service.PlanService;
import com.boot.service.WordModuleService;
import com.boot.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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

    @Resource
    private JwtUtils jwtUtils;

    /**
     * @param userId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 查询所有的计划 已测试
     * @Date: 2023/2/14 14:49
     */
    @GetMapping("plan")
    public Result plan(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
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
    public Result plan(@RequestBody Plan plan, HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);

//        新增
        if (plan.getPlanId() == null) {
            plan
                    .setPlanCreateTime(LocalDateTime.now())
                    .setUserId(userId);
            LambdaQueryWrapper<Plan> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Plan::getUserId, userId);
            /*没有主计划则设置其为主计划*/
            long count = planService.count(queryWrapper);
            if (count == 0) {
                plan.setPlanStatus("1");
            }
        }
        if (planService.saveOrUpdate(plan)) {
            /*学习模块的学习人数加1*/
            WordModule wordModule = wordModuleService.getById(plan.getModuleId());
            wordModule.setStudyNumber(wordModule.getStudyNumber() + 1);
            wordModuleService.updateById(wordModule);
            return Result.success("成功");
        } else {
            return Result.error("失败");
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
        Plan plan = planService.getById(planId);
        if (plan == null) {
            return Result.error("该计划不存在");
        }
        if ("1".equals(planService.getById(planId).getPlanStatus())) {
            return Result.error("无法删除主计划");
        } else if (planService.removeById(planId)) {
            /*学习模块的学习人数加1*/
            WordModule wordModule = wordModuleService.getById(plan.getModuleId());
            wordModule.setStudyNumber(wordModule.getStudyNumber() - 1);
            wordModuleService.updateById(wordModule);
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
     * @param oldPlanId:
     * @param newPlanId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 更换主计划 已测试
     * @Date: 2023/2/14 16:07
     */
    @PutMapping("mainPlan/{oldPlanId}/{newPlanId}")
    public Result mainPlan(@PathVariable Long oldPlanId, @PathVariable Long newPlanId) {
        Plan oldPlan = new Plan().setPlanId(oldPlanId).setPlanStatus("0");
        Plan newPlan = new Plan().setPlanId(newPlanId).setPlanStatus("1");
        List<Plan> list = new ArrayList<>();

        list.add(oldPlan);
        list.add(newPlan);
        System.out.println(oldPlan.toString());
        System.out.println(newPlan.toString());
        log.info(oldPlan.toString());
        log.info(newPlan.toString());
        if (planService.updateBatchById(list, 2)) {
            return Result.success("更改成功");
        } else {
            return Result.error("更改失败");
        }
    }

    /**
     * @param planId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 修改状态为已完成 已测试
     * @Date: 2023/2/14 17:13
     */
    @PutMapping("finishPlan/{planId}")
    public Result finishPlan(@PathVariable Long planId) {
        UpdateWrapper<Plan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("plan_status", 2).eq("plan_id", planId);
        if (!"1".equals(planService.getById(planId).getPlanStatus())) {
            return Result.error("无法设置非主计划的任务为已完成");
        } else if (planService.update(updateWrapper)) {
            return Result.success("更改成功");
        } else {
            return Result.error("更改失败");
        }
    }

}

