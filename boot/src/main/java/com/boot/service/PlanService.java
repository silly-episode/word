package com.boot.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.boot.entity.Plan;
import com.boot.vo.PlanVo;

import java.util.List;

/**
 * 学习计划(Plan)表服务接口
 *
 * @author makejava
 * @since 2023-02-12 12:03:04
 */
public interface PlanService extends IService<Plan> {

    List<PlanVo> selectAll(Long userId);

}

