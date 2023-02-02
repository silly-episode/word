package com.boot.controller;





import com.boot.utils.MinIOUtils;
import com.boot.utils.SmsUtils;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;


/**
 * (WordModule)表控制层
 *
 * @author makejava
 * @since 2023-01-28 23:53:07
 */

@RestController
@Slf4j
public class WordModuleController {

    @Resource
    private MinIOUtils minioUtils;
    @Autowired
    private SmsUtils smsUtils;



    /**
     * @param file:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: TODO
     * @Date: 2023/1/31 8:00
     */
    @PostMapping("upload")
    public String upload(MultipartFile file) {



        smsUtils.sendMessage("13142397682");
        log.info("in2");
        // 判断上传文件是否为空
        if (null == file || 0 == file.getSize()) {
            return "2";
        }
        try {
            String bucketName = "word";
            // 文件名
            String originalFilename = file.getOriginalFilename();
            log.info(originalFilename);
            // 新的文件名 = 存储桶名称_时间戳.后缀名
            String fileName = bucketName + "_" + System.currentTimeMillis() + originalFilename.substring(originalFilename.lastIndexOf("."));
            // 开始上传
            minioUtils.putObject(bucketName, file, fileName);

            return "0";
        } catch (Exception e) {
//            log.error("上传文件失败：{}", e.getMessage());

        }
        return "1";
    }

}


