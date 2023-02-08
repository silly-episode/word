package com.boot.controller;





import com.boot.common.result.CodeMsg;
import com.boot.common.result.Result;
import com.boot.dto.WordModuleDto;
import com.boot.utils.MinIOUtils;
import com.boot.utils.SmsUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;


/**
 * (WordModule)表控制层
 *
 * @author makejava
 * @since 2023-01-28 23:53:07
 */

@RestController
@Slf4j
@RequestMapping("word")
public class WordModuleController {

    @Resource
    private MinIOUtils minioUtils;




    /**
     * @param file:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: TODO
     * @Date: 2023/1/31 8:00
     */
    @PostMapping("wordModule")
    public Result wordModule(@RequestParam MultipartFile[] file,@Valid WordModuleDto wordModuleDto) {

        MultipartFile image = null;

        MultipartFile word=null;

        String bucketName = "word";

        String wordFileType = "json";
        for (MultipartFile multipartFile : file) {
            // 判断上传文件是否为空
            if (null == multipartFile || 0 == multipartFile.getSize()) {
                return Result.error(CodeMsg.NULL_FILE);
            }
            if (wordFileType.equals(multipartFile.getContentType())) {
                word = multipartFile;
            } else {
                image = multipartFile;
            }
        }

        try {
            // 文件名
            assert image != null;
            String originalFilename = image.getOriginalFilename();
            // 新的文件名 = 存储桶名称_时间戳.后缀名
            String fileName = bucketName + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 开始上传
            minioUtils.putObject(bucketName, image, fileName);

            return Result.success();
        } catch (Exception e) {
            log.error("上传文件失败：{}", e.getMessage());

        }
        return Result.error("error");
    }

}


