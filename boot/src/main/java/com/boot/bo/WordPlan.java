package com.boot.bo;

import com.boot.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/12 21:49
 * @FileName: WordPlan
 * @Description: 查询每天背诵单词的信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class WordPlan extends Plan {

    //单词模块名称
    private String moduleName;
    //单词头像路径
    private String moduleImagePath;
    //描述
    private String remark;
    //单词文件路径
    private String wordPath;
    //创建时间
    private LocalDateTime wordModuleCreateTime;
    //状态
    private String wordModuleStatus;
    //在线学习人数
    private Integer studyNumber;
    //上级模块名称
    private String superiorModule;


}
