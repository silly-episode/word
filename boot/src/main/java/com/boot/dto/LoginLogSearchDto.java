package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/27 9:17
 * @FileName: LoginLogSearch
 * @Description: 登录日志查询的Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginLogSearchDto extends PageDto {
    /*开始时间*/
    private LocalDateTime beginTime;
    /*结束时间*/
    private LocalDateTime endTime;
    /*账号、电话、用户名、用户id*/
    private String accountOrTelOrNickNameOrUserId;
    /*登录类型，验证码登录还是密码登录*/
    private String loginType;
    /*登录结果*/
    private String result;
}
