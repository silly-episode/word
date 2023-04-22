package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 12:05
 * @FileName: ResetPasswordDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResetPasswordDto extends SmsCodeDto {
    private String password;
}
