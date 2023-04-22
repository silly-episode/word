package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 10:56
 * @FileName: SmsCodeDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsCodeDto {
    private String tel;
    private String code;
}
