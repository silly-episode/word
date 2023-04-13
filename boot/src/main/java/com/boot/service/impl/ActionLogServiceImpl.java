package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.ActionLogDao;
import com.boot.entity.ActionLog;
import com.boot.service.ActionLogService;
import org.springframework.stereotype.Service;

/**
 * (ActionLog)表服务实现类
 *
 * @author makejava
 * @since 2023-04-13 10:18:38
 */
@Service("actionLogService")
public class ActionLogServiceImpl extends ServiceImpl<ActionLogDao, ActionLog> implements ActionLogService {

}

