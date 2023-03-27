package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/27 17:17
 * @FileName: UserSearchDto
 * @Description: 用户查询的Dto
 */
@SuppressWarnings("all")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserSearchDto extends PageDto {
    /*开始时间*/
    private LocalDateTime beginTime;
    /*结束时间*/
    private LocalDateTime endTime;
    /*账号、电话、用户名、用户id*/
    private String accountOrTelOrNickNameOrUserId;
    /*用户状态*/
    private String userStatus;
    /*积分升序或降序,*/
    private Boolean integrationOrderByAsc;
}
