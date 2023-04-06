package com.boot.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.boot.entity.EmotionWords;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 激励语(EmotionWords)表数据库访问层
 *
 * @author makejava
 * @since 2023-03-29 16:56:57
 */
@Repository
public interface EmotionWordsDao extends BaseMapper<EmotionWords> {

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<EmotionWords> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<EmotionWords> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<EmotionWords> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<EmotionWords> entities);

    /**
     * @param frequency:
     * @Return: EmotionWords
     * @Author: DengYinzhe
     * @Description: TODO 随机获取一条记录
     * @Date: 2023/3/31 16:17
     */
    @Select("SELECT * FROM word.emotion_words AS t1 JOIN (SELECT ROUND(RAND()*(SELECT MAX(order_id) FROM word.emotion_words  where frequency=#{frequency})) AS id) AS t2 WHERE t1.order_id>=t2.id and t1.frequency=#{frequency} ORDER BY t1.order_id LIMIT 1")
    EmotionWords selectOneByRand(String frequency);

}

