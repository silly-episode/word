package com.boot.utils;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/7 20:52
 * @FileName: HttpUtils
 * @Description: 解析真实ip地址
 */
public class HttpUtils {
    /*
      获取用户真实IP地址，不使用request.getRemoteAddr();的原因是有可能用户使用了代理软件方式避免真实IP地址,

      可是，如果通过了多级反向代理的话，X-Forwarded-For的值并不止一个，而是一串IP值，究竟哪个才是真正的用户端的真实IP呢？
      答案是取X-Forwarded-For中第一个非unknown的有效IP字符串。

      如：X-Forwarded-For：192.168.1.134, 192.168.1.135, 192.168.1.136,
      192.168.1.137

      用户真实IP为： 192.168.1.134


     */

    /**
     * @param request:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 获取用户ip地址
     * @Date: 2023/2/7 20:53
     */
    public static String getIpAddress(HttpServletRequest request) {
        String Xip = request.getHeader("X-Real-IP");
        String XFor = request.getHeader("X-Forwarded-For");

        //多次反向代理后会有多个ip值，第一个ip才是真实ip
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            int index = XFor.indexOf(",");
            if (index != -1) {
                return "0:0:0:0:0:0:0:1".equals(XFor.substring(0, index)) ? "127.0.0.1" : XFor.substring(0, index);
            } else {
                return "0:0:0:0:0:0:0:1".equals(XFor) ? "127.0.0.1" : XFor;
            }
        }
        XFor = Xip;
        if (StringUtils.isNotEmpty(XFor) && !"unKnown".equalsIgnoreCase(XFor)) {
            return "0:0:0:0:0:0:0:1".equals(XFor) ? "127.0.0.1" : XFor;
        }

        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("Proxy-Client-IP");
        }

        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("WL-Proxy-Client-IP");
        }

        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_CLIENT_IP");
        }

        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if (StringUtils.isBlank(XFor) || "unknown".equalsIgnoreCase(XFor)) {
            XFor = request.getRemoteAddr();
        }
        return "0:0:0:0:0:0:0:1".equals(XFor) ? "127.0.0.1" : XFor;
    }
}
