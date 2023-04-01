package com.boot.entity;


import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 激励语(EmotionWords)表实体类
 *
 * @author makejava
 * @since 2023-03-29 17:02:19
 */

@TableName("emotion_words")
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class EmotionWords extends Model<EmotionWords> {
    //主键
    @TableId(type = IdType.ASSIGN_ID)
    @ExcelIgnore
    private Long emoId;
    //英语内容
    @ExcelProperty("英语短句")
    private String engContent;
    //中文内容
    @ExcelProperty("中文短句")
    private String cnContent;
    //排序Id
    @ExcelIgnore
    private Integer orderId;
    //录入时间
    @ExcelIgnore
    private LocalDateTime emoCreateTime;
    //出现频率
    @ExcelProperty("出现频率")
    private String frequency;
    //作者
    @ExcelProperty("作者")
    private String emoAuthor;


    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.emoId;
    }
}

