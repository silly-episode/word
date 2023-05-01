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
 * @since 2023-04-27 14:42:32
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
    //取两次考试的平均分数
    private Integer grade;
    //考试结果时间
    private LocalDateTime examTime;
    //考试计划id
    private Long planId;
    //用户id
    private Long userId;
    //用时
    private String usageTime;
    //结果类型，‘0’为计划成绩；‘1’为文章成绩；‘2’为键盘小游戏成绩
    private String resultType;
    //键盘小游戏得分
    private Integer score;
    //文章练习正确率
    private Double correctRate;
    //文章练习的秒数
    private Integer content;
    //文章练习按键正确数
    private Integer correctNum;
    //键盘小游戏按键总数
    private Integer total;
    //文章练习输入数
    private Integer inputNum;
    //文章练习的速度
    private Double velocity;


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

