package com.boot.utils;

import com.boot.entity.ActionLog;
import com.boot.entity.Admin;
import com.boot.service.ActionLogService;
import com.boot.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/13 18:14
 * @FileName: ActionLogUtils
 * @Description: 录入操作日志
 */
@Component
@Slf4j
public class ActionLogUtils {

    public final String INSERT = "INSERT";
    public final String INSERT_BATCH = "INSERT_BATCH";
    public final String DELETE = "DELETE";
    public final String DELETE_BATCH = "DELETE_BATCH";
    public final String UPDATE = "UPDATE";
    public final String EXPORT = "EXPORT";
    @Resource
    private ActionLogService actionLogService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private AdminService adminService;

    public void saveActionLog(HttpServletRequest request, String actionKind, String remark) {
        Long adminId = jwtUtils.getUserIdFromRequest(request);
        Admin admin = adminService.getById(adminId);
        if (admin == null) {
            return;
        }
        ActionLog log = new ActionLog()
                .setActionId(adminId)
                .setActionTime(LocalDateTime.now())
                .setActionKind(actionKind)
                .setRemark(remark)
                .setKeepName(admin.getKeepName())
                .setRole(admin.getRole());
        actionLogService.save(log);
    }

}
