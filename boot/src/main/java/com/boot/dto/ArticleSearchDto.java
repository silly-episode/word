package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/26 19:13
 * @FileName: articleSearchDto
 * @Description: 文章查询的Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleSearchDto extends PageDto {
    /*开始时间*/
    private String beginTime;
    /*结束时间*/
    private String endTime;
    /*字数上限*/
    private Integer countUp;
    /*字数下限*/
    private Integer countLow;
    /*文章标题*/
    private String search;

}
