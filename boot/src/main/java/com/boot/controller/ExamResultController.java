package com.boot.controller;


import com.boot.common.result.Result;
import com.boot.entity.ExamResult;
import com.boot.service.ExamResultService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;


/**
 * (ExamResult)表控制层
 *
 * @author makejava
 * @since 2023-02-20 14:22:15
 */
@RestController
@RequestMapping("examResult")
public class ExamResultController {
    /**
     * 服务对象
     */
    @Resource
    private ExamResultService examResultService;


    /**
     * @param examResult:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 录入成绩，传入grade和planId
     * @Date: 2023/2/20 14:38
     */
    @PostMapping("examResult")
    public Result examResult(@RequestBody ExamResult examResult) {
        examResult.setExamTime(LocalDateTime.now());
        if (examResultService.save(examResult)) {
            return Result.success("录入成绩成功");
        } else {
            return Result.error("录入成绩失败");
        }
    }

    /**
     * @param userId:
     * @param beginTime:
     * @param endTime:
     * @param planId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 查询用户成绩，时间条件，计划id （平均分，最高分，最低分）（模块名称，考试时间，考试成绩，是否合格）
     * @Date: 2023/2/23 20:29
     */
    @GetMapping("examResult/{userId}/{beginTime}/{endTime}/{planId}")
    public Result examResult(
            @PathVariable() Long userId,
            @PathVariable(required = false) LocalDate beginTime,
            @PathVariable(required = false) LocalDate endTime,
            @PathVariable(required = false) Long planId) {


        return Result.success();
    }


}

