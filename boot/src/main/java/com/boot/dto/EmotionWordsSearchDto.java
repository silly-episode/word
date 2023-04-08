package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/29 17:28
 * @FileName: EmotionWordsSearchDto
 * @Description: emotionWords的Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmotionWordsSearchDto extends PageDto {

    /*开始时间*/
    private LocalDateTime beginTime;
    /*结束时间*/
    private LocalDateTime endTime;
    /*作者*/
    private String author;
    /*频率升序或降序*/
    private String frequency;
}
