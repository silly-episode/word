package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/5/6 10:49
 * @FileName: AdminMsgDto2
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdminMsgDto2 {
    private Long adminId;
    //登录账户
    private String account;
    //电话
    private String tel;
    //区分管理员和超级管理员，0是超级管理员，1是普通管理员
    private String role;
    //添加时间
    private LocalDateTime addCreateTime;
    //持有人
    private String keepName;

}
