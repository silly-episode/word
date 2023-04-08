package com.boot.dto;

import com.boot.entity.Plan;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/14 14:57
 * @FileName: planVo
 * @Description:
 */
@SuppressWarnings("all")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanVo extends Plan {
    private String moduleName;
}
