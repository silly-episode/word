package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/26 19:07
 * @FileName: PageDto
 * @Description: 分页搜索的Dto
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto {

    /**
     * 页号
     */
    private Integer pageNum;

    /**
     * 页大小
     */
    private Integer pageSize;

}
