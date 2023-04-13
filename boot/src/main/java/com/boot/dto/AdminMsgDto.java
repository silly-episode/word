package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/8 9:39
 * @FileName: adminMsgDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMsgDto {
    private Long adminId;
    //登录账户
    private String account;
    //电话
    private String tel;
    //描述
    private String remark;
    //区分管理员和超级管理员，0是超级管理员，1是普通管理员
    private String role;
    //添加时间
    private LocalDateTime addCreateTime;
    //持有人
    private String keepName;

    private LocalDateTime lockTime;

    private String userStatus;
}
