package com.boot.controller;

import com.boot.common.Exception.CustomException;
import com.boot.common.result.Result;
import com.boot.utils.TranslateUtils;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
@SuppressWarnings("all")
@RequestMapping("common")
public class CommonController {

    @Resource
    private TranslateUtils translateUtils;

    /**
     * @param sourceText: 源文本
     * @param source:     源语种
     * @param target:     目标语种
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
    public Result translate(@RequestBody Map<String,String> map) {
        String targetText = null;
        try {
            targetText = translateUtils.translate(map.get("sourceText"), map.get("source"), map.get("target"));
        } catch (TencentCloudSDKException e) {
            throw new CustomException("翻译服务异常");
        }
        return Result.success(targetText);
    }

}
