package com.aidilude.betdice.configuration;

import com.aidilude.betdice.property.DataSourceProperties;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.support.spring.stat.DruidStatInterceptor;
import org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration   //让spring在启动的时候扫描该类以及该类中定义的bean
@EnableTransactionManagement   //开启注解事务管理
public class DataSourceConfig {

    @Resource
    private DataSourceProperties druidProperties;

    @Bean(name = "dataSource")
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setDriverClassName(druidProperties.getDriverClassName());
        datasource.setUrl(druidProperties.getUrl());
        datasource.setUsername(druidProperties.getUsername());
        datasource.setPassword(druidProperties.getPassword());

        datasource.setInitialSize(druidProperties.getInitialSize());
        datasource.setMinIdle(druidProperties.getMinIdle());
        datasource.setMaxWait(druidProperties.getMaxWait());
        datasource.setMaxActive(druidProperties.getMaxActive());

        datasource.setTimeBetweenEvictionRunsMillis(druidProperties.getTimeBetweenEvictionRunsMillis());
        datasource.setMinEvictableIdleTimeMillis(druidProperties.getMinEvictableIdleTimeMillis());

        datasource.setValidationQuery(druidProperties.getValidationQuery());
        datasource.setTestWhileIdle(druidProperties.getTestWhileIdle());
        datasource.setTestOnBorrow(druidProperties.getTestOnBorrow());
        datasource.setTestOnReturn(druidProperties.getTestOnReturn());

        datasource.setRemoveAbandoned(druidProperties.getRemoveAbandoned());
        datasource.setRemoveAbandonedTimeout(druidProperties.getRemoveAbandonedTimeout());
        return datasource;
    }

}