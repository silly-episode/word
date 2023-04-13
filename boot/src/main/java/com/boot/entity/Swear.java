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
 * 发誓表(Swear)表实体类
 *
 * @author makejava
 * @since 2023-04-13 15:50:03
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("swear")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class Swear extends Model<Swear> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long swearId;
    //用户ID
    private Long userId;
    //发誓时间
    private LocalDateTime swearTime;
    //完成状况，0未完成，1已经完成
    private String swearStatus;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.swearId;
    }
}

