package com.boot.entity;

import co.elastic.clients.elasticsearch._types.mapping.FieldType;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.text.DateFormat;
import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/17 14:35
 * @FileName: Article
 * @Description: 文章实体（Es）
 */
@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class Article {
    //    文章id
    private Long articleId;
    //    文章内容
    private String content;
    //    文章创建时间
    @JsonFormat(shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8")
    private LocalDateTime articleCreateTime;
    //    文章标题
    private String articleTitle;
    //    文章单词字数
    private Integer wordNumber;
}
