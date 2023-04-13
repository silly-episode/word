package com.boot.dto;

import com.boot.entity.Swear;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/13 16:43
 * @FileName: SwearUserDto
 * @Description: 发誓和用户信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SwearUserDto extends Swear {
    private String nickName;
}
