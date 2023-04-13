package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/13 20:22
 * @FileName: BookOfWordSearchDto
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookOfWordSearchDto extends PageDto {
    private Long bookId;
}
