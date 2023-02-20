package com.boot.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.api.ApiController;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.result.Result;
import com.boot.entity.ExamResult;
import com.boot.service.ExamResultService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

/**
 * (ExamResult)表控制层
 *
 * @author makejava
 * @since 2023-02-20 14:22:15
 */
@RestController
@RequestMapping("examResult")
public class ExamResultController  {
    /**
     * 服务对象
     */
    @Resource
    private ExamResultService examResultService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @param examResult 查询实体
     * @return 所有数据
     */
    @GetMapping
    public Result selectAll(Page<ExamResult> page, ExamResult examResult) {
        return success(this.examResultService.page(page, new QueryWrapper<>(examResult)));
    }

    @PostMapping("examResult")
    public Result examResult() {


    }


}

