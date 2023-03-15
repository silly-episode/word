package com.boot.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * (BookOfWords)表实体类
 *
 * @author makejava
 * @since 2023-02-25 10:40:36
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class BookOfWords extends Model<BookOfWords> {
    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(type = IdType.ASSIGN_ID)
    private Long wordId;
    @JsonSerialize(using = ToStringSerializer.class)
    private Long bookId;

    private String word;
    //词性：词意；词性：词意
    private String meaning;
    //音标
    private String ukphone;

    private LocalDateTime wordInsertTime;
}

