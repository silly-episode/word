package com.boot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (ExamResult)表实体类
 *
 * @author makejava
 * @since 2023-02-20 14:22:15
 */
@Accessors(chain = true)
@TableName("exam_result")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class ExamResult extends Model<ExamResult> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long resultId;
    //取三次考试的平均分数
    private Integer grade;
    //考试结果时间
    private LocalDateTime examTime;
    //考试计划id
    private Long planId;
    // 用户id
    private Long userId;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.resultId;
    }
}

