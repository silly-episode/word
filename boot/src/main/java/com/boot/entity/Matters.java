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
 * 待办事项表(Matters)表实体类
 *
 * @author makejava
 * @since 2023-05-07 10:58:44
 */
@Accessors(chain = true)
@TableName("matters")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class Matters extends Model<Matters> {
    //id
    @TableId(type = IdType.ASSIGN_ID)
    private Long mattersId;
    //管理员Id
    private Long adminId;
    //事项主题
    private String mattersTitle;
    //事项内容
    private String mattersContent;
    //重要性：1轻 2中 3 重
    private String mattersImportance;
    //插入时间
    private LocalDateTime mattersInsertTime;
    //完成时间
    private LocalDateTime mattersFinishTime;
    //事项状态 0未完成 1已完成
    private Boolean mattersStatus;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.mattersId;
    }
}

