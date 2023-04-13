package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/9 11:32
 * @FileName: userMsgDto
 * @Description: 用户基础信息（个人）
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMsgDto {

    private Long userId;
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

    private String signature;

    private Integer integration;
    //登录账号
    private String account;

    private LocalDateTime registerTime;
    //    今天是否发誓
    private boolean swear;
}
