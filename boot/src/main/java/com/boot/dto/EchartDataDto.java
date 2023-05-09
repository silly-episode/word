package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/5/9 17:18
 * @FileName: EchartDataDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class EchartDataDto {
    private String name;
    private Integer value;
}
