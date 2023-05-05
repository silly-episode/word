package com.boot.bo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/22 14:57
 * @FileName: FileInfo
 * @Description: 外存情况
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
@Accessors(chain = true)
public class MyFileInfo {

    private String name;

    private String label;

    private String mount;

    private String fsType;

    private String freeSpace;

    private String unFreeSpace;

    private String totalSpace;

    private double unFreePercent;

}
