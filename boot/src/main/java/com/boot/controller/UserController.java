package com.boot.controller;


import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.boot.common.Exception.CustomException;
import com.boot.common.result.CodeMsg;
import com.boot.common.result.Result;
import com.boot.dto.LoginMessage;
import com.boot.dto.RegisterMessage;
import com.boot.dto.UserMsgDto;
import com.boot.entity.User;
import com.boot.service.UserService;
import com.boot.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;


/**
 * 用户表(User)表控制层
 *
 * @author makejava
 * @since 2023-02-01 11:31:13
 */
@RestController
@RequestMapping("user")
@Slf4j
@SuppressWarnings("all")
public class UserController {
    private final String codePre = "verifyCode";
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;
    @Resource
    private JwtUtils jwtUtils;
    @Resource
    private SmsUtils smsUtils;
    @Resource
    private RedisUtils redisUtils;
    @Resource
    private MinIOUtils minIOUtils;

    /**
     * @param phone: 中国手机号码
     * @Return: String 短信验证码
     * @Author: DengYinzhe
     * @Description: 获取短信验证码并存入Redis中 已测试
     * @Date: 2023/2/9 10:43
     */
    @GetMapping("sms")
    public String getCode(@RequestParam("phone") String phone) {
        User userBean = userService.getUserByTel(phone);
        if (userBean == null) {
            return "400";
        }
        String code = RandomUtils.getSixBitRandom();
        if (smsUtils.sendMessage(phone, code)) {
            redisUtils.add(codePre + phone, code, 5L, TimeUnit.MINUTES);
            return "200";
        } else {
            return "400";
        }

    }

    /**
     * @param loginMessage: 登录信息
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 密码登录 已测试
     * @Date: 2023/2/9 10:44
     */
    @PostMapping("loginPassword")
    public String loginPassword(@RequestBody LoginMessage loginMessage) {

        User userBean = userService.getUserByAccount(loginMessage.getLoginAccount());
        if (null == userBean) {
            return JsonUtils.getBeanToJson(Result.error(CodeMsg.BAD_CREDENTIAL));
        } else if (userBean.getPassword().equals(loginMessage.getLoginPassword())) {
            return JsonUtils.getBeanToJson(Result.success(jwtUtils.sign(loginMessage.getLoginAccount())));
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * @param loginMessage: 登录信息
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 短信登录 已测试
     * @Date: 2023/2/9 10:45
     */
    @PostMapping("loginSms")
    public String loginSms(@RequestBody LoginMessage loginMessage) {
        User userBean = userService.getUserByTel(loginMessage.getLoginAccount());
        if (null == userBean) {
            return JsonUtils.getBeanToJson(Result.error(CodeMsg.BAD_CREDENTIAL));
        } else if (loginMessage.getLoginPassword().equals(redisUtils.get(codePre + loginMessage.getLoginAccount()))) {
            return JsonUtils.getBeanToJson(Result.success(jwtUtils.sign(userBean.getAccount())));
        } else {
            throw new UnauthorizedException();
        }
    }

    /**
     * @param file:   用户头像
     * @param userId: 用户id
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 修改用户头像 已测试
     * @Date: 2023/2/9 10:53
     */
    @PutMapping("userImage")
    public Result userImage(@RequestParam MultipartFile file, @RequestParam("userId") Long userId) {
        User user = userService.getById(userId);
        String fileName = "user_image_" + userId.toString() + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        try {
            if (0 != file.getSize() && "image/jpeg".equals(file.getContentType())) {
                minIOUtils.putObject("word", file, fileName);
            }
        } catch (Exception e) {
            log.error("上传头像失败");
            minIOUtils.removeObject("word", fileName);
            return Result.error("上传头像失败");
        }
//        如果用户不是默认头像则删除头像文件
        if (!"user_defalut_image.jpg".equals(user.getHeadImage())) {
            minIOUtils.removeObject("word", user.getHeadImage());
        }
        user.setHeadImage(fileName);
        userService.updateById(user);
        return Result.success();
    }

    /**
     * @param userId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 获取用户头像, 已测试
     * @Date: 2023/2/9 11:44
     */
    @GetMapping("userImage/{userId}")
    public byte[] userImage(@PathVariable("userId") Long userId) throws IOException, CustomException {
        String bucketName = "word";
        String objectName = userService.getById(userId).getHeadImage();
        InputStream inputStream = minIOUtils.getObject(bucketName, objectName);
        if (inputStream != null) {
            return IoUtils.toByteArray(inputStream);
        } else {
            throw new CustomException("头像获取失败");
        }

    }

    /**
     * @param userMsgDto:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 修改用户信息  已测试
     * @Date: 2023/2/9 11:44
     */
    @PutMapping("user")
    public Result user(@RequestBody UserMsgDto userMsgDto) {
        if (userService.updateById(BeanDtoVoUtils.convert(userMsgDto, User.class))) {
            return Result.success();
        } else {
            return Result.error("更新失败");
        }
    }

    /**
     * @param userMsgDto:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 获取用户信息 已测试
     * @Date: 2023/2/9 11:44
     */
    @GetMapping("user/{userId}")
    public Result<UserMsgDto> user(@PathVariable("userId") Long userId) {
        User user = userService.getById(userId);
        UserMsgDto userMsgDto = BeanDtoVoUtils.convert(user, UserMsgDto.class);
        return Result.success(userMsgDto);
    }

    /**
     * @param userId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 注销账户，月初彻底删除  已测试
     * @Date: 2023/2/9 14:27
     */
    @DeleteMapping("user/{userId}")
    public Result userDelete(@PathVariable("userId") Long userId) {
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.set("user_status", '2').eq("user_id", userId);
        boolean result = userService.update(null, wrapper);
        if (result) {
            return Result.success("注销成功");
        } else {
            return Result.error("注销失败");
        }
    }

    /**
     * @param registerMessage:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 用户注册  已测试
     * @Date: 2023/2/10 13:08
     */
    @PostMapping("user")
    public Result user(@RequestBody RegisterMessage registerMessage) {

        User user = BeanDtoVoUtils.convert(registerMessage, User.class);
        user.setHeadImage("user_defalut_image.jpg");
        user.setRegisterTime(LocalDateTime.now());
        userService.save(user);
        return Result.success("注册成功");
    }

    /**
     * @param userId:
     * @param password:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 修改密码
     * @Date: 2023/2/14 20:09
     */
    @PutMapping("password")
    public Result password(@RequestParam Long userId, @RequestParam String password) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("password", password).eq("user_id", userId);
        if (userService.update(updateWrapper)) {
            return Result.success("修改密码成功");
        } else {
            return Result.error("修改密码失败");
        }
    }

    /**
     * @param userId:
     * @param tel:
     * @param code:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 绑定新手机号 todo
     * @Date: 2023/2/14 20:36
     */
    @PostMapping("tel")
    public Result tel(@RequestParam Long userId, @RequestParam String tel, @RequestParam String code) {

        if (null != userService.getUserByTel(tel)) {
            return Result.error(111, "手机号已被绑定");
        }
        if (code.equals(redisUtils.get(codePre + tel))) {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("tel", tel).eq("user_id", userId);
            if (userService.update(updateWrapper)) {
                return Result.success("绑定手机成功");
            } else {
                return Result.success("绑定手机号失败");
            }
        } else {
            return Result.error(222, "验证码不正确");
        }

    }

    /**
     * @param tel:
     * @param code:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 验证手机号码，用于销户或者未登录时重新设置密码或者更换手机号码 todo
     * @Date: 2023/2/14 20:48
     */
    @GetMapping("tel/{tel}/{code}")
    public Result tel(@PathVariable String tel, @PathVariable String code) {

        if (code.equals(redisUtils.get(codePre + tel))) {
            return Result.success("手机号验证成功");
        } else {
            return Result.error(222, "验证码不正确");
        }
    }

    /**
     * @param tel:
     * @param code:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 更换密码
     * @Date: 2023/2/14 20:58
     */
    @PutMapping("password/{tel}/{password}")
    public Result password(@PathVariable String tel, @PathVariable String password) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("password", password).eq("tel", tel);
        if (userService.update(updateWrapper)) {
            return Result.success("重置密码成功");
        } else {
            return Result.error("更换密码失败");
        }

    }

}

