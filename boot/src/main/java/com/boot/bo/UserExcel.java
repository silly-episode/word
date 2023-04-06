package com.boot.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import com.boot.common.JsonTransformation.LocalDateStringConverter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
//    设置单元格水平和垂直
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
public class UserExcel {
    //    避免科学计数法
    @NumberFormat(value = "#")
//    设置单元格宽度
    @ColumnWidth(30)
// 转换器设置为long和string的转换防止精度丢失
    @ExcelProperty(value = "用户ID", index = 0, converter = LongStringConverter.class)
    private Long userId;

    @ColumnWidth(25)
    @ExcelProperty(value = "账号", index = 1)
    private String account;

    @ColumnWidth(30)
    @ExcelProperty(value = "昵称", index = 2)
    private String nickName;

    @ColumnWidth(25)
    @ExcelProperty(value = "手机号", index = 3)
    private String tel;

    @ColumnWidth(25)
    @ExcelProperty(value = "邮箱", index = 4)
    private String email;

    @ColumnWidth(25)
    @ExcelProperty(value = "QQ", index = 5)
    private String qq;

    @ColumnWidth(25)
    @ExcelProperty(value = "微信号", index = 6)
    private String wechat;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(40)
    @ExcelProperty(value = "个性签名", index = 7)
    private String signature;

    @ColumnWidth(10)
    @ExcelProperty(value = "积分", index = 8)
    private Integer integration;

    @ColumnWidth(15)
    @ExcelProperty(value = "账号状态", index = 9)
    private String userStatus;

//    不知道为什么这里的格式无用，设置后，单元格的时间会变成#############
//    @DateTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
    @ColumnWidth(25)
    @ExcelProperty(value = "注册时间", index = 10)
    private LocalDateTime registerTime;

    //    //    @DateTimeFormat("yyyy年MM月dd日 HH时mm分ss秒")
    @ColumnWidth(25)
    @ExcelProperty(value = "锁定时间", index = 11, converter = LocalDateStringConverter.class)
    private LocalDate lockTime;

    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.LEFT, verticalAlignment = VerticalAlignmentEnum.CENTER)
    @ColumnWidth(40)
    @ExcelProperty(value = "备注", index = 12)
    private String remark;


}
