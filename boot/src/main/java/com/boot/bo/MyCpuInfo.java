package com.boot.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 14:51
 * @FileName: CpuInfo
 * @Description: Cpu信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
@Accessors(chain = true)
public class MyCpuInfo {
    /*CPU型号*/
    private String cpuModel;
    /*核心数量*/
    private int cpuNum;
    /*系统使用率*/
    private double sys;
    /*用户使用率*/
    private double user;
    /*当前空闲率*/
    private double free;
}

