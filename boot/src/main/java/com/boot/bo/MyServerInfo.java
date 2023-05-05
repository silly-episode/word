package com.boot.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/5/5 11:20
 * @FileName: MyServerInfo
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
@Accessors(chain = true)
public class MyServerInfo {
    /*服务器名称*/
    private String serverName;
    /*操作系统*/
    private String serverSystem;
    /*版本*/
    private String serverVersion;
    /*IP*/
    private String serverIp;
    /*用户名*/
    private String serverUser;
    /*系统架构*/
    private String serverArchitecture;
}
