package com.boot.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/17 14:35
 * @FileName: Article
 * @Description: 文章实体（Es）
 */
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class Article {
    //    文章id
    private String articleId;
    //    文章内容
    private String content;
    //    文章创建时间
//    @JsonFormat(shape = JsonFormat.Shape.STRING,
//            pattern = "yyyy-MM-dd HH:mm:ss",
//            timezone = "GMT+8")
    private Long articleCreateTime;
    //    文章标题
    private String articleTitle;
    //    文章单词字数
    private Integer wordNumber;
}
