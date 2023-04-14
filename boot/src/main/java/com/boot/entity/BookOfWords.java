package com.boot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * (BookOfWords)表实体类
 *
 * @author makejava
 * @since 2023-04-14 13:35:32
 */
@Accessors(chain = true)
@TableName("book_of_words")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class BookOfWords extends Model<BookOfWords> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long wordId;

    private Long bookId;
    //单词
    private String word;
    //解释
    private String trans;
    //词性
    private String pos;
    //单词插入时间
    private LocalDateTime wordInsertTime;
    //例句
    private String sentenceEn;
    //例句翻译
    private String sentenceZh;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.wordId;
    }
}

