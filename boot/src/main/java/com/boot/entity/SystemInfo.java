package com.boot.entity;


import com.boot.bo.MyCpuInfo;
import com.boot.bo.MyFileInfo;
import com.boot.bo.MyMemoryInfo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 14:38
 * @FileName: SystemInfo
 * @Description: 系统信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class SystemInfo {
    /*系统和JVM内存情况*/
    private MyMemoryInfo memoryInfo;
    /*CPU情况*/
    private MyCpuInfo cpuInfo;
    /*外存使用情况*/
    private List<MyFileInfo> fileInfoList;

}
