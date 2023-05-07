package com.boot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.entity.Matters;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 待办事项表(Matters)表数据库访问层
 *
 * @author makejava
 * @since 2023-05-07 10:58:44
 */
public interface MattersDao extends BaseMapper<Matters> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<Matters> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<Matters> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<Matters> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<Matters> entities);

}

