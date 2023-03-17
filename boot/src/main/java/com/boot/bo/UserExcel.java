package com.boot.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/17 9:50
 * @FileName: userExcel
 * @Description: 用户导出的类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@SuppressWarnings("all")
@EqualsAndHashCode
//普通单元格的高度
@ContentRowHeight(26)
//头部单元格的高度
@HeadRowHeight(30)
//普通单元格的字体大小
@ContentFontStyle(fontHeightInPoints = 12)

public class UserExcel {
    //    避免科学计数法
    @NumberFormat(value = "#")
//    设置单元格水平和垂直
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
//    设置单元格宽度
    @ColumnWidth(30)
// 转换器设置为long和string的转换防止精度丢失
    @ExcelProperty(value = "Id", index = 0, converter = LongStringConverter.class)
    private Long userId;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(25)
    @ExcelProperty(value = "账号", index = 1)
    private String account;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(30)
    @ExcelProperty(value = "昵称", index = 2)
    private String nickName;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(25)
    @ExcelProperty(value = "手机号", index = 3)
    private String tel;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(25)
    @ExcelProperty(value = "邮箱", index = 4)
    private String email;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(25)
    @ExcelProperty(value = "QQ", index = 5)
    private String qq;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(25)
    @ExcelProperty(value = "微信号", index = 6)
    private String wechat;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(40)
    @ExcelProperty(value = "个性签名", index = 7)
    private String signature;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(10)
    @ExcelProperty(value = "积分", index = 8)
    private Integer integration;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(15)
    @ExcelProperty(value = "账号状态", index = 9)
    private String userStatus;

//    @DateTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(25)
    @ExcelProperty(value = "注册时间", index = 10)
    private LocalDateTime registerTime;

    @DateTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(25)
    @ExcelProperty(value = "锁定时间", index = 11)
    private LocalDateTime lockTime;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(40)
    @ExcelProperty(value = "备注", index = 12)
    private String remark;



}
