package com.aidilude.betdice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        List<Parameter> pars = new ArrayList<Parameter>();
        ParameterBuilder secretPar = new ParameterBuilder();
        secretPar
                .name("secret")
                .description("访问系统的密钥")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build();
        ParameterBuilder timestampPar = new ParameterBuilder();
        timestampPar
                .name("timestamp")
                .description("请求创建的时间戳")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(true)
                .build();
        pars.add(secretPar.build());
        pars.add(timestampPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(pars)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.aidilude.betdice.controller"))
                .paths(PathSelectors.any())
                .build();
    }
    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("【艾迪路德】betdice - API")
                .description("betdice接口文档")
                .termsOfServiceUrl("")
                .contact("")
                .version("")
                .build();
    }

}