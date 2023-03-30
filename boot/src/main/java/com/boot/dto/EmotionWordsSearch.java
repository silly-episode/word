package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/29 17:28
 * @FileName: EmotionWordsSearch
 * @Description: emotionWords的Dto
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmotionWordsSearch extends PageDto {

    /*开始时间*/
    private LocalDateTime beginTime;
    /*结束时间*/
    private LocalDateTime endTime;
    /**/
}
