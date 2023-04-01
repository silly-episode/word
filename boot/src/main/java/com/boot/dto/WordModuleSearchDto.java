package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/31 17:42
 * @FileName: wordModuleSearchDto
 * @Description: wordModule搜索的Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordModuleSearchDto extends PageDto {

    /*开始时间*/
    private LocalDateTime beginTime;
    /*结束时间*/
    private LocalDateTime endTime;
    /*模块名称*/
    private String moduleName;
    /*上级模块*/
    private String superiorModule;
    /*学习人数升序或降序,*/
    private Boolean studyNumberOrderByAsc;
}
