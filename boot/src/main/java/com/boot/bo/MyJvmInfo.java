package com.boot.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 15:09
 * @FileName: JvmInfor
 * @Description: Jvm信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
@Accessors(chain = true)
public class MyJvmInfo {
    /*名称*/
    private String jvmName;
    /*版本*/
    private String jvmVersion;
    /*启动时间*/
    private LocalDateTime jvmStartTime;
    /*运行时长*/
    private String jvmRunTime;
    /*安装路径*/
    private String jvmPath;
    /*项目路径*/
    private String projectPath;
    /*PID*/
    private String jvmPid;
    /*处理级别*/
    private String processingLevel;
    /*运行参数*/
    private String runParams;
}
