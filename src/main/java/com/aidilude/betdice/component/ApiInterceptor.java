package com.aidilude.betdice.component;

import com.aidilude.betdice.property.SystemProperties;
import com.aidilude.betdice.util.Result;
import com.aidilude.betdice.util.ResultCode;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private ApplicationComponent applicationComponent;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        /******************************************系统维护******************************************/

        /******************************************跨域配置******************************************/
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type,Content-Length,Authorization,Accept,X-Requested-With,secret,timestamp");
        response.setHeader("Access-Control-Allow-Methods","PUT,POST,GET,DELETE,OPTIONS");
        /******************************************接口放行******************************************/
        String uri = request.getRequestURI();
        if(uri.contains("/serverTime"))
            return true;
        /******************************************身份验证******************************************/
        if(uri.contains("/queryCurrentTurn") || uri.contains("/recordTransaction") || uri.contains("/pledge")){
            String realIP = applicationComponent.getIPAddress(request);
            if(!applicationComponent.isLegalIP(realIP)){
                Result.returnMsg(response, ResultCode.IllegalAccess, "非法访问");
                return false;
            }
        }else{
            String secret = request.getHeader("secret");
            String timestamp = request.getHeader("timestamp");
            if(!applicationComponent.isLegalAccess(secret, timestamp, systemProperties.getCustomerAccessKey())){
                Result.returnMsg(response, ResultCode.IllegalAccess, "非法访问.");
                return false;
            }
        }
        return true;
    }

}