package com.boot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.result.Result;
import com.boot.dto.MattersSearchDto;
import com.boot.entity.Matters;
import com.boot.service.MattersService;
import com.boot.utils.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * 待办事项表(Matters)表控制层
 *
 * @author makejava  todo  restfule 测试
 * @since 2023-05-07 10:58:44
 */
@RestController
@RequestMapping("matters")
@RequiresAuthentication
public class MattersController {
    /**
     * 服务对象
     */
    @Resource
    private MattersService mattersService;
    @Resource
    private JwtUtils jwtUtils;

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 搜索
     * @Date: 2023/5/7 13:11
     */
    @PostMapping("matterSearch")
    public Result selectAll(@RequestBody MattersSearchDto mattersSearchDto, HttpServletRequest request) {
//        System.out.println(mattersSearchDto);
        Page<Matters> pageInfo = new Page<>(mattersSearchDto.getPageNum(), mattersSearchDto.getPageSize());
        LambdaQueryWrapper<Matters> wrapper = new LambdaQueryWrapper<>();
        long adminId = jwtUtils.getUserIdFromRequest(request);
        wrapper
                .eq(Matters::getAdminId, adminId)
                .ge(null != mattersSearchDto.getBeginTime(), Matters::getMattersInsertTime, mattersSearchDto.getBeginTime())
                .le(null != mattersSearchDto.getEndTime(), Matters::getMattersInsertTime, mattersSearchDto.getEndTime())
                .eq(null != mattersSearchDto.getMatterStatus(), Matters::getMattersStatus, mattersSearchDto.getMatterStatus())
                .eq(!mattersSearchDto.getMatterImportance().isEmpty(), Matters::getMattersImportance, mattersSearchDto.getMatterImportance())
                .eq(!mattersSearchDto.getSearch().isEmpty(), Matters::getMattersTitle, mattersSearchDto.getSearch())
                .orderByDesc(Matters::getMattersInsertTime);
        mattersService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 新增
     * @Date: 2023/5/7 12:47
     */
    @PostMapping("matter")
    public Result insert(@RequestBody Matters matters, HttpServletRequest request) {
        long adminId = jwtUtils.getUserIdFromRequest(request);
        matters
                .setMattersInsertTime(LocalDateTime.now())
                .setAdminId(adminId);
        if (mattersService.save(matters)) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败");
        }
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 修改
     * @Date: 2023/5/7 13:12
     */
    @PutMapping("matter")
    public Result update(@RequestBody Matters matters) {
        if (mattersService.updateById(matters)) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 修改事项状态
     * @Date: 2023/5/7 12:33
     */
    @PutMapping("mattersId/{mattersId}/mattersStatus/{mattersStatus}")
    public Result mattersStatus(
            @PathVariable("mattersId") long mattersId,
            @PathVariable("mattersStatus") boolean mattersStatus,
            HttpServletRequest request
    ) {

        long adminId = jwtUtils.getUserIdFromRequest(request);
        LambdaUpdateWrapper<Matters> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(Matters::getAdminId, adminId).eq(Matters::getMattersId, mattersId);
        /*判断*/
        if (mattersStatus) {
            wrapper
                    .set(Matters::getMattersStatus, true)
                    .set(Matters::getMattersFinishTime, LocalDateTime.now());
        } else {
            wrapper
                    .set(Matters::getMattersStatus, false)
                    .set(Matters::getMattersFinishTime, null);
        }
        /*修改*/
        if (mattersService.update(wrapper)) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 删除事项
     * @Date: 2023/5/7 11:55
     */
    @DeleteMapping("matter/{mattersId}")
    public Result delete(@PathVariable("mattersId") Long mattersId) {
        if (mattersService.removeById(mattersId)) {
            return Result.success("移除成功");
        } else {
            return Result.error("移除失败");
        }

    }
}

