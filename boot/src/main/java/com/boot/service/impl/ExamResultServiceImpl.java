package com.boot.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.boot.dao.ExamResultDao;
import com.boot.entity.ExamResult;
import com.boot.service.ExamResultService;
import org.springframework.stereotype.Service;

/**
 * (ExamResult)表服务实现类
 *
 * @author makejava
 * @since 2023-04-27 14:42:40
 */
@Service("examResultService")
public class ExamResultServiceImpl extends ServiceImpl<ExamResultDao, ExamResult> implements ExamResultService {

}

