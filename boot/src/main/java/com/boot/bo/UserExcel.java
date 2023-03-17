package com.boot.bo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
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
@ContentRowHeight(20)
//头部单元格的高度
@HeadRowHeight(25)
//所有单元格的宽度
@ColumnWidth(25)
//普通单元格的字体大小
@ContentFontStyle(fontHeightInPoints = 20)

@ContentStyle()
public class UserExcel {
    @ExcelProperty(value = "id", index = 0)
    private Long userId;
    @ExcelProperty(value = "账号", index = 1)
    private String account;
    @ExcelProperty(value = "昵称", index = 2)
    private String nickName;
    @ExcelProperty(value = "手机号", index = 3)
    private String tel;
    @ExcelProperty(value = "邮箱", index = 4)
    private String email;
    @ExcelProperty(value = "QQ", index = 5)
    private String qq;
    @ExcelProperty(value = "微信号", index = 6)
    private String wechat;
    @ExcelProperty(value = "个性签名", index = 7)
    private String signature;
    @ExcelProperty(value = "积分", index = 8)
    private Integer integration;
    @ExcelProperty(value = "账号状态", index = 9)
    private String userStatus;
    @ExcelProperty(value = "注册时间", index = 10)
    private LocalDateTime registerTime;
    @ExcelProperty(value = "锁定时间", index = 11)
    private LocalDateTime lockTime;
    @ExcelProperty(value = "备注", index = 12)
    private String remark;



}
