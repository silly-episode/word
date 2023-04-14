package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/14 10:51
 * @FileName: CollectWordsDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class CollectWordsDto {

    private List<Long> bookId;

    private String word;
    //词性：词意；词性：词意
    private String trans;
    //例句
    private String sentenceEn;
    //例句翻译
    private String sentenceZh;
    //词性
    private String pos;
}
