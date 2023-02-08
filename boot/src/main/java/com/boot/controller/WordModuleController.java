package com.boot.controller;



import com.boot.common.result.CodeMsg;
import com.boot.common.result.Result;
import com.boot.dto.WordModuleDto;
import com.boot.entity.WordModule;
import com.boot.service.WordModuleService;
import com.boot.utils.BeanDtoVoUtils;
import com.boot.utils.MinIOUtils;

import com.boot.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;



import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Objects;


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

    @Resource
    private WordModuleService wordModuleService;


    /**
     * @param file:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: TODO
     * @Date: 2023/1/31 8:00
     */
    @PostMapping("wordModule")
    public Result wordModule(@RequestParam MultipartFile[] file,@Valid WordModuleDto wordModuleDto) {

        MultipartFile imageFile = null;
        String imageFileName = "";
        MultipartFile wordFile=null;
        String wordFileName = "";
        String bucketName = "word";
        Long moduleId = SnowFlakeUtil.getNextId();
        String wordFileType = "application/json";
        String imageFileType = "image/jpeg";

        WordModule wordModule;

//        判断文件数量
        if (file.length < 2) {
            return Result.error(CodeMsg.NEED_TWO_FILES);
        }
//        判断空文件
        for (MultipartFile multipartFile : file) {
            log.info(multipartFile.getContentType());
            if (0 == multipartFile.getSize()) {
                return Result.error(CodeMsg.NULL_FILE);
            }
            log.info(multipartFile.getContentType());
            log.info(String.valueOf(Objects.requireNonNull(multipartFile.getOriginalFilename()).substring(multipartFile.getOriginalFilename().lastIndexOf("."))));
            if (wordFileType.equals(multipartFile.getContentType())) {
                wordFile = multipartFile;
            } else if(imageFileType.equals(multipartFile.getContentType())){
                imageFile = multipartFile;
            }
        }
//        判断文件格式
        if (0==Objects.requireNonNull(imageFile).getSize()||0== Objects.requireNonNull(wordFile).getSize()){
            return Result.error(CodeMsg.FILE_FORMAT_ERROR);
        }



//上传文件到minIo
        try {
            imageFileName = (bucketName + "_" + wordModuleDto.getModuleName() + "_image_" + moduleId.toString()  + imageFile.getOriginalFilename().substring(imageFile.getOriginalFilename().lastIndexOf(".")));

            minioUtils.putObject(bucketName, imageFile, imageFileName);

            wordFileName = (bucketName + "_" + wordModuleDto.getModuleName() + "_word_" + moduleId.toString() +".json");
            minioUtils.putObject(bucketName, wordFile, wordFileName);
        } catch (Exception e) {
            log.error("上传文件失败：{}", e.getMessage());
//            删除文件
//            判断文件是否存在
        }


//        存入ES

//        将wordfile转为buffer


//        存入Mysql

        wordModule = BeanDtoVoUtils.convert(wordModuleDto, WordModule.class);
        wordModule.setModuleId(moduleId);
        wordModule.setModuleImagePath(imageFileName);
        wordModule.setWordPath(wordFileName);
        wordModule.setCreateTime(LocalDateTime.now());
        wordModuleService.save(wordModule);


        return Result.error("error");
    }

}


