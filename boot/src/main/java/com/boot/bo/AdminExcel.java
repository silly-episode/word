package com.boot.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.NumberFormat;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/8 9:56
 * @FileName: AdminExcel
 * @Description: 管理员导出为Excel
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
public class AdminExcel {
    @NumberFormat(value = "#")
    @ColumnWidth(30)
    @ExcelProperty(value = "管理员ID", index = 0)
    private Long adminId;
    //登录账户
    @ColumnWidth(23)
    @ExcelProperty(value = "账户", index = 1)
    private String account;
    //持有人
    @ColumnWidth(18)
    @ExcelProperty(value = "持有人", index = 2)
    private String keepName;
    //电话
    @ColumnWidth(18)
    @NumberFormat(value = "#")
    @ExcelProperty(value = "手机号", index = 3)
    private String tel;

    //区分管理员和超级管理员，0是超级管理员，1是普通管理员
    @ColumnWidth(18)
    @ExcelProperty(value = "角色", index = 4)
    private String role;
    //添加时间
    @ColumnWidth(25)
    @ExcelProperty(value = "添加时间", index = 5)
    private LocalDateTime addCreateTime;

    @ColumnWidth(13)
    @ExcelProperty(value = "状态", index = 6)
    private String userStatus;
    @ColumnWidth(25)
    @ExcelProperty(value = "锁定/解锁时间", index = 7)
    private LocalDateTime lockTime;

    //描述
    @ColumnWidth(37)
    @ExcelProperty(value = "描述", index = 8)
    private String remark;
}
