package com.aidilude.betdice.configuration;

import com.aidilude.betdice.component.ApiInterceptor;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class WebAppConfig implements WebMvcConfigurer {

    //以这种方式将拦截器注入为一个bean，可以防止拦截器中无法注入bean的问题出现
    @Bean
    public ApiInterceptor apiInterceptor(){
        return new ApiInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiInterceptor()).addPathPatterns("/api/**");
    }

    @Bean
    public HttpMessageConverter<Object> fastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
        FastJsonConfig fastJsonConfig = new FastJsonConfig();
        // 空值特别处理
        // WriteMapNullValue Map字段如果为null,输出为[],而非null
        // WriteNullListAsEmpty 将Collection类型字段的字段空值输出为[]
        // WriteNullStringAsEmpty 将字符串类型字段的空值输出为空字符串 ""
        // WriteNullNumberAsZero 将数值类型字段的空值输出为0
        // WriteNullBooleanAsFalse 将Boolean类型字段的空值输出为false
        fastJsonConfig.setSerializerFeatures(
                SerializerFeature.PrettyFormat,   //是否格式化输出，默认为false
                SerializerFeature.DisableCircularReferenceDetect   //禁止循环引用，即出现$.data[0]
        );
        //统一配置日期格式
        fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");
        fastJsonConfig.setCharset(Charset.forName("UTF-8"));
        fastConverter.setFastJsonConfig(fastJsonConfig);
        // 处理中文乱码问题
        List<MediaType> fastMediaTypes = new ArrayList<>();
        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
        fastConverter.setSupportedMediaTypes(fastMediaTypes);
        return fastConverter;
    }

}