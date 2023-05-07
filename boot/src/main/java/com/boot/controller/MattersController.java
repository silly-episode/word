package com.boot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.result.Result;
import com.boot.dto.MattersSearchDto;
import com.boot.entity.Matters;
import com.boot.service.MattersService;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 搜索
     * @Date: 2023/5/7 13:11
     */
    @PostMapping("matterSearch")
    public Result selectAll(@RequestBody MattersSearchDto mattersSearchDto) {
        Page<Matters> pageInfo = new Page<>(mattersSearchDto.getPageNum(), mattersSearchDto.getPageSize());
        LambdaQueryWrapper<Matters> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .ge(null != mattersSearchDto.getBeginTime(), Matters::getMattersInsertTime, mattersSearchDto.getBeginTime())
                .le(null != mattersSearchDto.getEndTime(), Matters::getMattersInsertTime, mattersSearchDto.getEndTime())
                .eq(null != mattersSearchDto.getMatterStatus(), Matters::getMattersStatus, mattersSearchDto.getMatterStatus())
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
    public Result insert(@RequestBody Matters matters) {
        matters.setMattersInsertTime(LocalDateTime.now());
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
    @PutMapping("matterStatus")
    public Result mattersStatus(@RequestBody Matters matters) {
        /*判断*/
        if (matters.getMattersStatus()) {
            matters.setMattersStatus(false);
//            todo 传啥？
            matters.setMattersFinishTime(null);
        } else {
            matters.setMattersStatus(true);
            matters.setMattersFinishTime(LocalDateTime.now());
        }
        /*修改*/
        if (mattersService.updateById(matters)) {
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

