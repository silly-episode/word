package com.boot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.dto.PlanVo;
import com.boot.entity.Plan;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 学习计划(Plan)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-12 12:03:02
 */
@Repository
public interface PlanDao extends BaseMapper<Plan> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Plan> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Plan> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Plan> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Plan> entities);

    /**
     * @param userId:
     * @Return: List<PlanVo>
     * @Author: DengYinzhe
     * @Description: 查询用户的计划（附带模块名称）
     * @Date: 2023/2/14 15:39
     */
    List<PlanVo> selectAll(@Param("userId") Long userId);


}

