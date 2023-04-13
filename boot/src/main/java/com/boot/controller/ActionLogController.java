package com.boot.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.result.Result;
import com.boot.entity.ActionLog;
import com.boot.service.ActionLogService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * (ActionLog)表控制层
 *
 * @author makejava
 * @since 2023-04-13 10:18:37
 */
@RestController
@RequestMapping("actionLog")
public class ActionLogController {
    /**
     * 服务对象
     */
    @Resource
    private ActionLogService actionLogService;

    /**
     * 分页查询所有数据
     *
     * @param page      分页对象
     * @param actionLog 查询实体
     * @return 所有数据
     */
    @GetMapping
    public Result selectAll(Page<ActionLog> page, ActionLog actionLog) {


        return Result.success();
    }

}

