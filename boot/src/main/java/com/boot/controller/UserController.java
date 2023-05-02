package com.boot.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.Exception.CustomException;
import com.boot.common.Hutool.IdUtils;
import com.boot.common.result.CodeMsg;
import com.boot.common.result.Result;
import com.boot.dto.*;
import com.boot.entity.LoginLog;
import com.boot.entity.Swear;
import com.boot.entity.User;
import com.boot.service.SwearService;
import com.boot.service.UserService;
import com.boot.utils.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
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
    @Resource
    private SwearService swearService;
    /*登录方式*/
    private final String loginBySm = "sms";
    private final String loginByPwd = "pwd";
    /*用户状态*/
    private final String normalUserStatus = "0";
    private final String lockUserStatus = "1";
    private final String deleteUserStatus = "2";
    /*登录日志结果类型*/
    private final String successResult = "0";
    private final String nullUserResult = "1";
    private final String smsOverdueResult = "2";
    private final String smsErrorResult = "3";
    private final String pwdErrorResult = "4";
    private final String lockResult = "5";
    private LoginLog loginLog;

    /**
     * @param phone: 中国手机号码
     * @Return: String 短信验证码
     * @Author: DengYinzhe
     * @Description: 获取短信验证码并存入Redis中 已测试
     * @Date: 2023/2/9 10:43
     */
    @GetMapping("sms/{phone}")
    public Result getCode(@PathVariable("phone") String phone) {
        User userBean = userService.getUserByTel(phone);
        if (userBean == null) {
//            该手机号的用户不存在，所以不发送短信
            return Result.error(CodeMsg.ACCOUNT_NOT_FOUND);
        }
        String code = RandomUtils.getSixBitRandom();
        if (smsUtils.sendMessage(phone, code)) {
            redisUtils.add(codePre + phone, code, 5L, TimeUnit.MINUTES);
            return Result.success("发送短信成功");
        } else {
            return Result.error("发送短信失败");
        }
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 已经登录的发送验证码 需要认证
     * @Date: 2023/4/22 10:47
     */
    @GetMapping("smsLogined/{phone}")
    @RequiresAuthentication
    public Result getCodeLogined(@PathVariable("phone") String phone) {
        String code = RandomUtils.getSixBitRandom();
        if (smsUtils.sendMessage(phone, code)) {
            redisUtils.add(codePre + phone, code, 5L, TimeUnit.MINUTES);
            return Result.success("发送短信成功");
        } else {
            return Result.error("发送短信失败");
        }
    }


    /**
     * @param loginMessage: 登录信息
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 登录
     * @Date: 2023/2/9 10:44
     */
    @PostMapping("login")
    public Result login(@RequestBody LoginMessage loginMessage, HttpServletRequest request) {
        User userBean = null;
        boolean flag = false;
        String smsCode = null;
        //获取用户真实的IP地址并设置登录时间

        loginLog = new LoginLog()
                .setIp(HttpUtils.getIpAddress(request))
                .setLoginTime(LocalDateTime.now());
        //获取用户信息
        if (loginBySm.equals(loginMessage.getType())) {
            loginLog.setLoginType("短信登录").setTel(loginMessage.getLoginAccount());
            userBean = userService.getUserByTel(loginMessage.getLoginAccount());
        } else if (loginByPwd.equals(loginMessage.getType())) {
            loginLog.setLoginType("账号登录").setAccount(loginMessage.getLoginAccount());
            userBean = userService.getUserByAccount(loginMessage.getLoginAccount());
        }
        // 该账号对应的用户不存在或用户封禁
        if (null == userBean) {
            loginLog.setResult(nullUserResult).setLogRemark("尝试登录的账户并不存在");
            return Result.error(CodeMsg.ACCOUNT_NOT_FOUND);
        } else if (lockUserStatus.equals(userBean.getUserStatus())) {
            loginLog = userService.userToLoginLog(userBean, loginLog);
            loginLog.setResult(lockResult).setLogRemark("该账户已锁定");
            return Result.error(CodeMsg.ACCOUNT_NOT_ACTIVATED);
        }
        // 用户存在则将用户信息转与loginLog
        loginLog = userService.userToLoginLog(userBean, loginLog);
        //  登录逻辑判断，登录成功则放行，错误则返回对应的错误信息
        if (loginBySm.equals(loginMessage.getType())) {
            smsCode = redisUtils.get(codePre + loginMessage.getLoginAccount());
            if (null == smsCode) {
                loginLog.setResult(smsOverdueResult).setLogRemark("验证码过期或验证码不存在");
                return Result.error(CodeMsg.CAPTCHA_EXPIRED);
            } else if (loginMessage.getLoginPassword().equals(smsCode)) {
                flag = true;
            } else {
                loginLog.setResult(smsErrorResult).setLogRemark("验证码错误");
                return Result.error(CodeMsg.CAPTCHA_INVALID);
            }
        } else if (loginByPwd.equals(loginMessage.getType())) {
            if (userBean.getPassword().equals(loginMessage.getLoginPassword())) {
                flag = true;
            } else {
                loginLog.setResult(pwdErrorResult).setLogRemark("密码错误");
                return Result.error(CodeMsg.BAD_CREDENTIAL);
            }
        }
        //  登录成功后的行为：重置删除状态、登录日志、返回签名token
        if (flag) {
            if (deleteUserStatus.equals(userBean.getUserStatus())) {
                userBean.setUserStatus(normalUserStatus);
                userService.updateById(userBean);
            }
            loginLog.setResult(successResult).setLogRemark("用户登录成功");
            /*返回信息*/
            Map<String, Object> map = new HashMap<>(2);
            String token = jwtUtils.sign(String.valueOf(userBean.getUserId()));
            UserMsgDto userInfo = BeanDtoVoUtils.convert(userBean, UserMsgDto.class);


            LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
            LocalDateTime today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

            //        发誓情况
            LambdaQueryWrapper<Swear> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Swear::getUserId, userBean.getUserId())
                    .ge(Swear::getSwearTime, today_start)
                    .le(Swear::getSwearTime, today_end);
            long count = swearService.count(queryWrapper);
            if (count > 0) {
                userInfo.setSwear(true);
            } else {
                userInfo.setSwear(false);
            }

            map.put("token", token);
            map.put("userInfo", userInfo);
            return Result.success("登录成功", map);
        }
        return Result.error("系统异常");
    }

    /**
     * @param file:   用户头像
     * @param userId: 用户id
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 修改用户头像 已测试
     * @Date: 2023/2/9 10:53
     */
    @PostMapping("userImage")
    @RequiresAuthentication
    public Result userImage(@RequestParam MultipartFile file, UserOfId userOfId) {

        Long userId = Long.valueOf(userOfId.getUserId());
        User user = userService.getById(userId);
        String fileName = "user_image_" + userId.toString() + "_"
                + String.valueOf(LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"))
                + file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".")));
        try {
            if (0 != file.getSize() && "image/jpeg".equals(file.getContentType())) {
                // 上传用户头像
                minIOUtils.putObject("word", file, fileName);
            } else {
                return Result.error("文件不是jpg图片或内容为空");
            }
        } catch (Exception e) {
//            log.error("上传头像失败");
            minIOUtils.removeObject("word", fileName);
            return Result.error("上传头像失败");
        }
        // 如果用户不是默认头像则删除头像文件
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
    public void userImage(@PathVariable("userId") Long userId, HttpServletResponse response) throws IOException {
        ServletOutputStream outputStream = null;
        InputStream inputStream = null;
        try {
            //输出流，通过输出流将文件写回浏览器
            outputStream = response.getOutputStream();
            response.setContentType("image/jpeg");
            //从MinIo中获取用户头像
            String bucketName = "word";
            String objectName = userService.getById(userId).getHeadImage();
            inputStream = minIOUtils.getObject(bucketName, objectName);

            if (inputStream != null) {
                int len;
                byte[] bytes = new byte[1024 * 4];
                while ((len = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, len);
                    outputStream.flush();
                }
            } else {
                throw new CustomException("头像获取失败");
            }
        } catch (IOException e) {
            throw new CustomException("头像获取失败");
        } catch (CustomException e) {
            throw new CustomException("头像获取失败");
        } finally {
            //关闭资源
            if (inputStream != null) {
                inputStream.close();
            }
            if (inputStream != null) {
                outputStream.close();
            }
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
    @RequiresAuthentication
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
//    @GetMapping("user")
////    @RequiresAuthentication
//    public Result<UserMsgDto> user(HttpServletRequest request) {
////        时间处理
//        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
//        LocalDateTime today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
//        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////        获取信息
//        Long userId = jwtUtils.getUserIdFromRequest(request);
//        User user = userService.getById(userId);
//        UserMsgDto userMsgDto = BeanDtoVoUtils.convert(user, UserMsgDto.class);
////        发誓情况
//        LambdaQueryWrapper<Swear> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(Swear::getUserId, userId)
//                .ge(Swear::getSwearTime, today_start)
//                .le(Swear::getSwearTime, today_end);
//        long count = swearService.count(queryWrapper);
//        if (count > 0) {
//            userMsgDto.setSwear(true);
//        } else {
//            userMsgDto.setSwear(false);
//        }
//
//        return Result.success(userMsgDto);
//    }

    /**
     * @param userId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 注销账户，月初彻底删除  已测试
     * @Date: 2023/2/9 14:27
     */
    @DeleteMapping("user")
    @RequiresAuthentication
    public Result userDelete(HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        assert userId != null;
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.set("user_status", '2').eq("user_id", userId);
        boolean result = userService.update(wrapper);
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
        Long userId = IdUtils.getSnowFlakeInstance().nextId();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", registerMessage.getAccount());
        User userFromDb = userService.getOne(queryWrapper);
        if (userFromDb != null) {
            return Result.error("该账号已经存在");
        }
        User user = BeanDtoVoUtils
                .convert(registerMessage, User.class)
                .setHeadImage("user_defalut_image.jpg")
                .setRegisterTime(LocalDateTime.now())
                .setUserId(userId);
        if (userService.save(user)) {
            String token = jwtUtils.sign(String.valueOf(userId));
            return Result.success("注册成功", token);
        } else {
            return Result.error("注册失败");
        }

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
    @RequiresAuthentication
    public Result password(@RequestBody PasswordDto passwordDto, HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("token异常");
        }
        User user = userService.getById(userId);
        if (!user.getPassword().equals(passwordDto.getOldPassword())) {
            return Result.error("旧密码不正确");
        }
        if (user.getPassword().equals(passwordDto.getNewPassword())) {
            return Result.error("旧密码和新密码相同");
        }
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("password", passwordDto.getNewPassword()).eq("user_id", userId);
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
    @RequiresAuthentication
    public Result tel(@RequestBody SmsCodeDto smsCodeDto, HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        if (userId == null) {
            return Result.error("token异常");
        }
        String tel = smsCodeDto.getTel();
        String code = smsCodeDto.getCode();
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
     * @Description: TODO 重置密码
     * @Date: 2023/2/14 20:58
     */
    @PutMapping("resetPassword")
    @RequiresAuthentication
    public Result password(@RequestBody ResetPasswordDto resetPasswordDto) {
        if (resetPasswordDto.getCode().equals(redisUtils.get(codePre + resetPasswordDto.getTel()))) {
            UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
            updateWrapper.set("password", resetPasswordDto.getPassword()).eq("tel", resetPasswordDto.getTel());
            if (userService.update(updateWrapper)) {
                return Result.success("重置密码成功");
            } else {
                return Result.error("更换密码失败");
            }
        } else {
            return Result.error("验证码不正确");
        }


    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 积分排名
     * @Date: 2023/4/13 15:33
     */
    @PostMapping("hotIntegration")
    public Result hotIntegration(@RequestBody PageDto pageDto) {
        Page<User> pageInfo = new Page<>(1, 100);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUserStatus, "0").orderByDesc(User::getIntegration);
        userService.page(pageInfo, wrapper);
        return Result.success(pageInfo);
    }

    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 发誓
     * @Date: 2023/4/13 16:13
     */
    @PostMapping("swear")
    @RequiresAuthentication
    public Result swear(HttpServletRequest request) {

        LocalDateTime today_start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime today_end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        Long userId = jwtUtils.getUserIdFromRequest(request);
        LambdaQueryWrapper<Swear> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Swear::getUserId, userId)
                .ge(Swear::getSwearTime, today_start)
                .le(Swear::getSwearTime, today_end);
        long count = swearService.count(queryWrapper);
        if (count == 0) {
            Swear swear = new Swear()
                    .setSwearTime(LocalDateTime.now())
                    .setUserId(userId);
            if (swearService.save(swear)) {
                return Result.success("发誓成功");
            } else {
                return Result.error("系统错误");
            }
        } else {
            return Result.success("已经发过誓啦");
        }
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 发誓动态
     * @Date: 2023/4/13 16:59
     */
    @PostMapping("swearSearch")
    public Result swearSearch(@RequestBody PageDto pageDto) {
//      时间处理
        LocalDate yesterday = LocalDate.now().minus(1, ChronoUnit.DAYS);
        LocalDateTime yesterday_start = LocalDateTime.of(yesterday, LocalTime.MIN);
        LocalDateTime yesterday_end = LocalDateTime.of(yesterday, LocalTime.MAX);
        DateTimeFormatter time = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

//      昨日动态
        Page<Swear> swearPage = new Page<>(pageDto.getPageNum(), pageDto.getPageSize());
        LambdaQueryWrapper<Swear> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .ge(Swear::getSwearTime, yesterday_start)
                .le(Swear::getSwearTime, yesterday_end);
        swearService.page(swearPage, queryWrapper);
//        转换
        Page<SwearUserDto> pageInfo = BeanDtoVoUtils.pageVo(swearPage, SwearUserDto.class);
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        for (SwearUserDto record : pageInfo.getRecords()) {
            wrapper.clear();
            wrapper.eq(User::getUserId, record.getUserId());
            User user = userService.getOne(wrapper);
            record.setNickName(user.getNickName());
        }

        return Result.success(pageInfo);
    }


}

