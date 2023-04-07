package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/7 20:02
 * @FileName: AdminSearchDto
 * @Description: 管理员搜索的Dto
 */
@SuppressWarnings("all")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdminSearchDto extends PageDto {
    /*开始时间*/
    private LocalDateTime beginTime;
    /*结束时间*/
    private LocalDateTime endTime;
    /*账号、电话、持有人、用户id*/
    private String accountOrTelOrNickNameOrUserId;
    /*用户状态*/
    private String userStatus;
}
