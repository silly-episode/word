package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/27 19:17
 * @FileName: UserMsgDto2
 * @Description: 用户基础信息（管理员）
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMsgDto2 extends UserMsgDto {

    private String remark;

    private String userStatus;

    private LocalDateTime lockTime;
}
