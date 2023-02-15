package com.boot.utils;

import com.boot.config.SmsConfig;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import com.tencentcloudapi.sms.v20190711.SmsClient;
import com.tencentcloudapi.sms.v20190711.models.SendSmsRequest;
import com.tencentcloudapi.sms.v20190711.models.SendSmsResponse;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/31 14:16
 * @FileName: SmsUtils
 * @Description: 短信发送
 */
@Component
@Slf4j
@NoArgsConstructor
public class SmsUtils {


    public boolean sendMessage(String phone, String code) {

        try {
            //这里是实例化一个Credential，也就是认证对象，参数是密钥对；你要使用肯定要进行认证
            Credential credential = new Credential(SmsConfig.SECRET_ID, SmsConfig.SECRET_KEY);

            //HttpProfile这是http的配置文件操作，比如设置请求类型(post,get)或者设置超时时间了、还有指定域名了
            //最简单的就是实例化该对象即可，它的构造方法已经帮我们设置了一些默认的值
            HttpProfile httpProfile = new HttpProfile();
            //这个setEndpoint可以省略的
            httpProfile.setEndpoint(SmsConfig.END_POINT);

            //实例化一个客户端配置对象,这个配置可以进行签名（使用私钥进行加密的过程），对方可以利用公钥进行解密
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            //实例化要请求产品(以sms为例)的client对象
            SmsClient smsClient = new SmsClient(credential, "ap-guangzhou", clientProfile);

            //实例化request封装请求信息
            SendSmsRequest request = new SendSmsRequest();
            String[] phoneNumber = {"+86" + phone};
            request.setPhoneNumberSet(phoneNumber);
            request.setSmsSdkAppid(SmsConfig.APP_ID);
            request.setSign(SmsConfig.SIGN_NAME);
            request.setTemplateID(SmsConfig.TEMPLATE_ID);
            //生成随机验证码和时间
            String verificationCode = code;
            String time = "5";
            String[] templateParamSet = {verificationCode, time};
            request.setTemplateParamSet(templateParamSet);

            //发送短信
            SendSmsResponse response = smsClient.SendSms(request);
            log.info(SendSmsResponse.toJsonString(response));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
