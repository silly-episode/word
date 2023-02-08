package com.boot.entity;


import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (WordModule)表实体类
 *
 * @author makejava
 * @since 2023-01-28 23:53:07
 */
@TableName("word_module")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordModule extends Model<WordModule> {
    //单词模块id
    @TableId(type = IdType.ASSIGN_ID)
    private Long moduleId;
    //单词模块名称
    private String moduleName;
    //单词头像路径
    private String moduleImagePath;
    //描述
    private String remark;
    //单词文件路径
    private String wordPath;
    //创建时间
    private LocalDateTime createTime;
    //状态
    private String status;
    //在线学习人数
    private Integer studyNumber;
    //上级模块名称
    private String superiorModule;



    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.moduleId;
    }
    }

