package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.PlanDao;
import com.boot.entity.Plan;
import com.boot.service.PlanService;
import com.boot.vo.PlanVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 学习计划(Plan)表服务实现类
 *
 * @author makejava
 * @since 2023-02-12 12:03:05
 */
@Service("planService")
public class PlanServiceImpl extends ServiceImpl<PlanDao, Plan> implements PlanService {
    @Resource
    private PlanDao planDao;

    @Override
    public List<PlanVo> selectAll(Long userId) {
        return planDao.selectAll(userId);
    }
}

