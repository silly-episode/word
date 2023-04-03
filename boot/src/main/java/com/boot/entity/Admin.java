package com.boot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 管理员表(Admin)表实体类
 *
 * @author makejava
 * @since 2023-04-03 11:38:23
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class Admin extends Model<Admin> {
    //管理员id
    @TableId(type = IdType.ASSIGN_ID)
    private Long adminId;
    //登录账户
    private String account;
    //密码
    private String password;
    //电话
    private String tel;
    //描述
    private String remark;
    //区分管理员和超级管理员，0是超级管理员，1是普通管理员
    private Integer role;
    //添加时间
    private LocalDateTime addCreateTime;
    //持有人
    private String keepName;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.adminId;
    }
}

