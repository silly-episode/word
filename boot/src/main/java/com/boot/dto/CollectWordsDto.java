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
public class CollectWordsDto {

    private List<Long> bookId;

    private String word;
    //词性：词意；词性：词意
    private String meaning;
    //音标
    private String ukphone;
}
