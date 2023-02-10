package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/9 11:32
 * @FileName: userMsgDto
 * @Description: 用户基础信息
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
}
