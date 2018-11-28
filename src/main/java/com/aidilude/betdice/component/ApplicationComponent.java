package com.aidilude.betdice.component;

import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.property.SystemProperties;
import com.aidilude.betdice.util.*;
import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;

@Component
public class ApplicationComponent {

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private ApiProperties apiProperties;

    //###########################################功能函数###########################################

    public boolean isLegalAccess(String secret, String timestamp, String key){
        if(StringUtils.isEmpty(secret) || !StringUtils.isTimestamp(timestamp)){
            return false;
        }
        long sub = new Date().getTime() - Long.valueOf(timestamp);
        if(sub > systemProperties.getAccessTimeLimit()) {
            return false;
        }
        if(!secret.equals(EncryptUtils.MD5Encode(timestamp + key))) {
            return false;
        }
        return true;
    }

    public boolean isLegalIP(String IP){
        if(StringUtils.isEmpty(IP))
            return false;
        if(!IP.equals(apiProperties.getChainIP()) && !IP.equals("127.0.0.1") && !IP.equals("192.168.1.12")) {
            return false;
        }
        return true;
    }

    public String getIPAddress(HttpServletRequest request) {

        String ip = null;

        //X-Forwarded-For：Squid 服务代理
        String ipAddresses = request.getHeader("X-Forwarded-For");

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //Proxy-Client-IP：apache 服务代理
            ipAddresses = request.getHeader("Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //WL-Proxy-Client-IP：weblogic 服务代理
            ipAddresses = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //HTTP_CLIENT_IP：有些代理服务器
            ipAddresses = request.getHeader("HTTP_CLIENT_IP");
        }

        if (ipAddresses == null || ipAddresses.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            //X-Real-IP：nginx服务代理
            ipAddresses = request.getHeader("X-Real-IP");
        }

        //有些网络通过多层代理，那么获取到的ip就会有多个，一般都是通过逗号（,）分割开来，并且第一个ip为客户端的真实IP
        if (ipAddresses != null && ipAddresses.length() != 0) {
            ip = ipAddresses.split(",")[0];
        }

        //还是不能获取到，最后再通过request.getRemoteAddr();获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ipAddresses)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public String transfer(BigDecimal amount, String secret, String receiver) throws Exception{
//        amount = amount.multiply(new BigDecimal(100000000));
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("type", apiProperties.getTransferType());
        jsonBody.put("secret", secret);
        jsonBody.put("fee", apiProperties.getTransferFee());
        jsonBody.put("args", "[\"" + apiProperties.getShareBonusCurrency() + "\",\"" + amount.toString() + "\",\"" + receiver + "\"]");
        String url = apiProperties.getChainRequestProtocol() + apiProperties.getChainIP() + ":" + apiProperties.getChainPort() + apiProperties.getPreffix() + apiProperties.getTransfer();
        return HttpUtils.doPut(url, null, jsonBody.toJSONString());
    }

}