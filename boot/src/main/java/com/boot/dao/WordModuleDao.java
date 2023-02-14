package com.boot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.bo.WordPlan;
import com.boot.entity.WordModule;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * (WordModule)表数据库访问层
 *
 * @author makejava
 * @since 2023-02-08 15:30:18
 */
@Repository
public interface WordModuleDao extends BaseMapper<WordModule> {

/**
* 批量新增数据（MyBatis原生foreach方法）
*
* @param entities List<WordModule> 实例对象列表
* @return 影响行数
*/
int insertBatch(@Param("entities") List<WordModule> entities);

/**
* 批量新增或按主键更新数据（MyBatis原生foreach方法）
*
* @param entities List<WordModule> 实例对象列表
* @return 影响行数
* @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
*/
int insertOrUpdateBatch(@Param("entities") List<WordModule> entities);


    /**
     * @param userId:
     * @Return: WordPlan
     * @Author: DengYinzhe
     * @Description: 查询正在执行的单词计划
     * @Date: 2023/2/14 9:53
     */
    WordPlan selectWordPlan(@Param("userId") Long userId);


}

