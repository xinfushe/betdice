package com.aidilude.betdice.property;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "system")
@PropertySource("classpath:property/system.properties")
public class SystemProperties {

    private Long accessTimeLimit;

    private String customerAccessKey;

    private String adminAccessKey;

    private Integer pageSize;

    public Long getAccessTimeLimit() { return accessTimeLimit; }

    public void setAccessTimeLimit(Long accessTimeLimit) {
        this.accessTimeLimit = accessTimeLimit;
    }

    public String getCustomerAccessKey() { return customerAccessKey; }

    public void setCustomerAccessKey(String customerAccessKey) {
        this.customerAccessKey = customerAccessKey;
    }

    public String getAdminAccessKey() {
        return adminAccessKey;
    }

    public void setAdminAccessKey(String adminAccessKey) {
        this.adminAccessKey = adminAccessKey;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

}