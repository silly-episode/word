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
 * @Date: 2023/4/13 14:09
 * @FileName: ActionLogExcel
 * @Description: 操作日志的导出
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
public class ActionLogExcel {

    @ColumnWidth(30)
    @ExcelProperty(value = "操作时间", index = 0)
    private LocalDateTime actionTime;

    @NumberFormat(value = "#")
    @ColumnWidth(30)
    @ExcelProperty(value = "管理员ID", index = 1, converter = LongStringConverter.class)
    private Long adminId;


    @ColumnWidth(30)
    @ExcelProperty(value = "持有人姓名", index = 2)
    private String keepName;

    @ColumnWidth(25)
    @ExcelProperty(value = "角色", index = 3)
    private String role;

    @ColumnWidth(20)
    @ExcelProperty(value = "操作类型", index = 4)
    private String actionKind;

    @ColumnWidth(71)
    @ExcelProperty(value = "结果描述", index = 5)
    private String remark;


}
