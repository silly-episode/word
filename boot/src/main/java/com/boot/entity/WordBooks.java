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
 * (WordBooks)表实体类
 *
 * @author makejava
 * @since 2023-02-25 10:39:58
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("word_books")
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class WordBooks extends Model<WordBooks> {
    @TableId(type = IdType.ASSIGN_ID)
    private Long bookId;

    private String bookName;

    private Long userId;

    private LocalDateTime bookCreateTime;

    private LocalDateTime bookUpdateTime;

    private Integer wordCount;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.bookId;
    }
}

