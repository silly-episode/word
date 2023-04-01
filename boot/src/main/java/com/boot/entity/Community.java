package com.boot.entity;


import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 社区运行状况（redis固化）(Community)表实体类
 *
 * @author makejava
 * @since 2023-03-31 16:57:21
 */
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class Community extends Model<Community> {

    private Long communityId;
    //版本信息
    private String version;
    //愿景
    private String wish;
    //创建时间
    private LocalDateTime createTime;
    //会员人数
    private Integer memberNum;
    //最高在线人数
    private Integer topOnlineNum;
    //文章数
    private Integer articleNum;
    //单词模块数
    private Integer moduleNum;
    //gitHub地址
    private String gitHubAddress;
    //联系电话
    private String tel;

    /**
     * 获取主键值
     *
     * @return 主键值
     */
    @Override
    public Serializable pkVal() {
        return this.communityId;
    }
}

