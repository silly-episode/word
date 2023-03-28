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
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户表(User)表实体类
 *
 * @author makejava
 * @since 2023-02-01 11:31:13
 */
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@TableName("user")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class User extends Model<User> {
    //用户id
    @TableId(type = IdType.ASSIGN_ID)
    private Long userId;
    //登录账号
    private String account;
    //登录密码
    private String password;
    //用户昵称
    private String nickName;
    //电话
    private String tel;
    //邮箱
    private String email;
    //qq
    private String qq;
    //微信
    private String wechat;
    //头像地址
    private String headImage;
    //用户状态，0正常，1锁定，2删除
    private String userStatus;
    //注册时间
    private LocalDateTime registerTime;
    //锁定时间
    private LocalDate lockTime;
    //备注
    private String remark;
    //个性签名
    private String signature;
    //随机盐
    private String salt;
    //    用户积分
    private Integer integration;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.userId;
    }
}

