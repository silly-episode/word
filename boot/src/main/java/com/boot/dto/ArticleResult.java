package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/5/11 11:35
 * @FileName: ArticleResultDto
 * @Description: 文章练习结果
 */
@SuppressWarnings("all")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class ArticleResult {
    //文章练习正确率
    private Double correctRate;
    //文章练习的秒数
    private Integer content;
    //文章练习按键正确数
    private Integer correctNum;
    //文章练习输入数
    private Integer inputNum;
    //文章练习的速度
    private Double velocity;
    //考试结果时间
    private LocalDateTime examTime;
    /*Echarts的value 值*/
    private Double value;
}
