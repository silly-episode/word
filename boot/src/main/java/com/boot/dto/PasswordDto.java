package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 11:34
 * @FileName: PasswordDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordDto {

    private String oldPassword;

    private String newPassword;
}
