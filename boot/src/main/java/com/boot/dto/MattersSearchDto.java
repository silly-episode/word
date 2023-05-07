package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/5/7 12:49
 * @FileName: mattersSearchDto
 * @Description:
 */
@SuppressWarnings("all")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MattersSearchDto extends PageDto {
    /*开始时间*/
    private LocalDateTime beginTime;
    /*结束时间*/
    private LocalDateTime endTime;
    /*查询*/
    private String search;
    /*状态*/
    private Boolean matterStatus;
}
