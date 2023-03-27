package com.boot.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.converters.longconverter.LongStringConverter;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/27 11:45
 * @FileName: LoginLogExcel
 * @Description: 登录日志Excel
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
//普通单元格的高度
@ContentRowHeight(26)
//头部单元格的高度
@HeadRowHeight(30)
//普通单元格的字体大小
@ContentFontStyle(fontHeightInPoints = 12)
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)
public class LoginLogExcel {


    @ColumnWidth(25)
    @ExcelProperty(value = "登录时间", index = 0)
    private LocalDateTime loginTime;


    //登录账户
    @ColumnWidth(20)
    @ExcelProperty(value = "账号", index = 2)
    private String account;


    //ip
    @ColumnWidth(25)
    @ExcelProperty(value = "IP地址", index = 7)
    private String ip;


    //    避免科学计数法
    @NumberFormat(value = "#")
//    设置单元格宽度
    @ColumnWidth(25)
// 转换器设置为long和string的转换防止精度丢失
    @ExcelProperty(value = "用户ID", index = 1, converter = LongStringConverter.class)
    private Long userId;


    //用户名
    @ColumnWidth(20)
    @ExcelProperty(value = "昵称", index = 3)
    private String nickName;


    //电话
    @ColumnWidth(20)
    @ExcelProperty(value = "手机号", index = 4)
    private String tel;


    //结果的描述
    @ColumnWidth(40)
    @ExcelProperty(value = "登录结果", index = 8)
    private String logRemark;


    //用户状态
    @ColumnWidth(15)
    @ExcelProperty(value = "账号状态", index = 5)
    private String userStatus;


    //登录方式：账号登录，短信登录
    @ColumnWidth(17)
    @ExcelProperty(value = "登录方式", index = 6)
    private String loginType;


}
