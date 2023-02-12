package com.boot.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 学习计划(Plan)表实体类
 *
 * @author makejava
 * @since 2023-02-12 12:03:03
 */
@Accessors(chain = true)
@TableName("plan")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class Plan extends Model<Plan> {
    //计划id
    private Long planId;
    //用户id
    private Long userId;
    //创建时间
    private LocalDateTime createTime;
    //状态，没想好
    private String status;
    //单词总数
    private Integer allWord;
    //模块id
    private Integer moduleId;
    //已完成单词数量
    private Integer finishedWord;
    //计划每天完成单词数量
    private Integer dayWord;



    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.planId;
    }
    }

