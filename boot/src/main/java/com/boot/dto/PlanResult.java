package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/5/11 11:39
 * @FileName: PlanResult
 * @Description:
 */
@SuppressWarnings("all")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class PlanResult {
    //取两次考试的平均分数
    private Integer grade;
    /*计划名称*/
    private String planName;
    /*模块名称*/
    private String moduleName;
    //用时
    private String usageTime;
    //考试结果时间
    private LocalDateTime examTime;
    /*Echarts的value 值*/
    private Double value;
}
