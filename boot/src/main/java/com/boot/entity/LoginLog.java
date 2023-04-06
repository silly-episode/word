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
 * (LoginLog)表实体类
 *
 * @author makejava
 * @since 2023-03-25 19:18:18
 */
@Accessors(chain = true)
@TableName("login_log")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class LoginLog extends Model<LoginLog> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long logId;

    private LocalDateTime loginTime;
    //登录账户
    private String account;

    private String ip;
    //用户id
    private Long userId;
    //0成功，1用户不存在，2验证码过期或不存在，3验证码错误，4密码错误，5账户已锁定
    private String result;
    //用户名
    private String nickName;
    //电话
    private String tel;
    //结果的描述
    private String logRemark;
    //用户状态
    private String userStatus;
    //登录方式：账号登录，短信登录
    private String loginType;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.logId;
    }
}

