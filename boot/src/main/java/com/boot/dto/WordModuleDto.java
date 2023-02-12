package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/8 11:25
 * @FileName: WordModuleDto
 * @Description: 单词模块dto
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WordModuleDto {

    private String superiorModule;

    private String moduleName;

    private String remark;

}
