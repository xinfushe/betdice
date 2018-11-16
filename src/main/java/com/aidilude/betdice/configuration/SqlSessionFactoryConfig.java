package com.aidilude.betdice.configuration;

import com.aidilude.betdice.property.MybatisProperties;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages={"com.aidilude.betdice.mapper"})   //扫描mapper接口包，并为每个接口添加@Mapper注解
public class SqlSessionFactoryConfig {

    @Resource
    private MybatisProperties mybatisProperties;

    @Resource
    private DataSource dataSource;

    @Bean(name = "sqlSessionFactory")
    public SqlSessionFactoryBean sqlSessionFactory(){
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(mybatisProperties.getConfigPath()));
        sqlSessionFactoryBean.setTypeAliasesPackage(mybatisProperties.getEntityPackage());
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean;
    }

}