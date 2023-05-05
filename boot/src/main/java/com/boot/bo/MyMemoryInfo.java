package com.boot.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 14:41
 * @FileName: MemoryInfo
 * @Description: 内存信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
@Accessors(chain = true)
public class MyMemoryInfo {
    /*系统内存总量*/
    private String memoryTotal;
    /*系统剩余内存*/
    private String remainMemory;
    /*系统已用内存*/
    private String usedMemory;
    /*系统内存使用率*/
    private double memoryPercent;


    /*JVM内存总量*/
    private String jvmMemoryTotal;
    /*JVM剩余内存*/
    private String jvmRemainMemory;
    /*JVM已用内存*/
    private String jvmUsedMemory;
    /*JVM内存使用率*/
    private double jvmMemoryPercent;


}
