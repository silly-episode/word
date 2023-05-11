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
 * @FileName: GameResult
 * @Description:
 */
@SuppressWarnings("all")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Accessors(chain = true)
public class GameResult {
    //结果类型，‘0’为计划成绩；‘1’为文章成绩；‘2’为键盘小游戏成绩
    private String resultType;
    //键盘小游戏得分
    private Integer score;
    //键盘小游戏按键总数
    private Integer total;
    //考试结果时间
    private LocalDateTime examTime;
    /*Echarts的value 值*/
    private Double value;
}
