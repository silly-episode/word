package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/2 20:56
 * @FileName: RegisterMessage
 * @Description: 注册信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterMessage {

    private String nickName;

    private String password;

    private String account;
}
