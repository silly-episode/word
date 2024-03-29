package com.boot.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.boot.bo.*;
import com.boot.common.Exception.CustomException;
import com.boot.common.result.Result;
import com.boot.entity.Community;
import com.boot.service.CommunityService;
import com.boot.utils.SystemInfoUtils;
import com.boot.utils.TranslateUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/22 22:05
 * @FileName: CommonController
 * @Description:
 */

@RestController
@Slf4j
@RequestMapping("common")
public class CommonController {

    @Resource
    private TranslateUtils translateUtils;

    @Resource
    private CommunityService communityService;

    /**
     * @Return: Result<Community>
     * @Author: DengYinzhe
     * @Description: TODO 获取社区基础信息
     * @Date: 2023/3/31 17:02
     */
    @GetMapping("community")
    public Result<Community> community() {
        QueryWrapper<Community> queryWrapper = new QueryWrapper<>();
        Community community = communityService.getOne(queryWrapper);
        return Result.success(community);
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 系统信息
     * @Date: 2023/4/22 15:35
     */
    @GetMapping("systemInfo")
    @RequiresAuthentication
    public Result<Map<String, Object>> systemInfo() {
        try {
            MyMemoryInfo memoryInfo = SystemInfoUtils.getMemory();
            MyCpuInfo cpuInfo = SystemInfoUtils.getCpuInfo();
            List<MyFileInfo> fileInfoList = SystemInfoUtils.getFileInfo();
            MyJvmInfo jvmInfo = SystemInfoUtils.getJvmInfo();
            MyServerInfo serverInfo = SystemInfoUtils.getServerInfo();
//            System.out.println(serverInfo);
//            System.out.println(jvmInfo);
//            System.out.println(fileInfoList);
//            System.out.println(myCpuInfo);
//            System.out.println(memoryInfo);

            Map<String, Object> map = new HashMap<>(5);
            map.put("memoryInfo", memoryInfo);
            map.put("cpuInfo", cpuInfo);
            map.put("fileInfoList", fileInfoList);
            map.put("jvmInfo", jvmInfo);
            map.put("serverInfo", serverInfo);
            return Result.success(map);
        } catch (FileNotFoundException | UnknownHostException e) {
            e.printStackTrace();
            return Result.error("系统错误");
        }
    }


    /**
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 翻译
     * @Date: 2023/2/22 22:13
     */
/*    源语言，支持：
    auto：自动识别（识别为一种语言）zh：简体中文 zh-TW：繁体中文 en：英语 ja：日语 ko：韩语
    fr：法语 es：西班牙语 it：意大利语 de：德语 tr：土耳其语 ru：俄语 pt：葡萄牙语 vi：越南语 id：印尼语 th：泰语
    ms：马来西亚语 ar：阿拉伯语 hi：印地语*/
/*    目标语言，各源语言的目标语言支持列表如下
    zh（简体中文）：en（英语）、ja（日语）、ko（韩语）、fr（法语）、es（西班牙语）、it（意大利语）、de（德语）、tr（土耳其语）、ru（俄语）、pt（葡萄牙语）、vi（越南语）、id（印尼语）、th（泰语）、ms（马来语）
    zh-TW（繁体中文）：en（英语）、ja（日语）、ko（韩语）、fr（法语）、es（西班牙语）、it（意大利语）、de（德语）、tr（土耳其语）、ru（俄语）、pt（葡萄牙语）、vi（越南语）、id（印尼语）、th（泰语）、ms（马来语）
    en（英语）：zh（中文）、ja（日语）、ko（韩语）、fr（法语）、es（西班牙语）、it（意大利语）、de（德语）、tr（土耳其语）、ru（俄语）、pt（葡萄牙语）、vi（越南语）、id（印尼语）、th（泰语）、ms（马来语）、ar（阿拉伯语）、hi（印地语）
    ja（日语）：zh（中文）、en（英语）、ko（韩语）
    ko（韩语）：zh（中文）、en（英语）、ja（日语）
    fr（法语）：zh（中文）、en（英语）、es（西班牙语）、it（意大利语）、de（德语）、tr（土耳其语）、ru（俄语）、pt（葡萄牙语）
    es（西班牙语）：zh（中文）、en（英语）、fr（法语）、it（意大利语）、de（德语）、tr（土耳其语）、ru（俄语）、pt（葡萄牙语）
    it（意大利语）：zh（中文）、en（英语）、fr（法语）、es（西班牙语）、de（德语）、tr（土耳其语）、ru（俄语）、pt（葡萄牙语）
    de（德语）：zh（中文）、en（英语）、fr（法语）、es（西班牙语）、it（意大利语）、tr（土耳其语）、ru（俄语）、pt（葡萄牙语）
    tr（土耳其语）：zh（中文）、en（英语）、fr（法语）、es（西班牙语）、it（意大利语）、de（德语）、ru（俄语）、pt（葡萄牙语）
    ru（俄语）：zh（中文）、en（英语）、fr（法语）、es（西班牙语）、it（意大利语）、de（德语）、tr（土耳其语）、pt（葡萄牙语）
    pt（葡萄牙语）：zh（中文）、en（英语）、fr（法语）、es（西班牙语）、it（意大利语）、de（德语）、tr（土耳其语）、ru（俄语）
    vi（越南语）：zh（中文）、en（英语）
    id（印尼语）：zh（中文）、en（英语）
    th（泰语）：zh（中文）、en（英语）
    ms（马来语）：zh（中文）、en（英语）
    ar（阿拉伯语）：en（英语）
    hi（印地语）：en（英语）*/
    @PostMapping("translate")
    public Result<String> translate(@RequestBody Map<String, String> map) {
        String targetText = null;
        String sourceText = map.get("sourceText");
        String source = map.get("source");
        String target = map.get("target");
        if (source == null || target == null) {
            return Result.error("请选择翻译语言选项");
        }
        if (sourceText.isEmpty()) {
            return Result.error("请输入源文本");
        }
        try {
            targetText = translateUtils.translate(sourceText, source, target);
        } catch (TencentCloudSDKException e) {
            throw new CustomException("翻译服务异常");
        }
        return Result.success("返回结果", targetText);
    }

}
