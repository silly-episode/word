package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.CommunityDao;
import com.boot.entity.Community;
import com.boot.service.CommunityService;
import org.springframework.stereotype.Service;

/**
 * 社区运行状况（redis固化）(Community)表服务实现类
 *
 * @author makejava
 * @since 2023-03-31 16:57:21
 */
@Service("communityService")
public class CommunityServiceImpl extends ServiceImpl<CommunityDao, Community> implements CommunityService {

}

