package com.boot.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/13 21:11
 * @FileName: BookNameChangeDto
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookNameChangeDto {
    private Long bookId;
    private String bookName;
}
