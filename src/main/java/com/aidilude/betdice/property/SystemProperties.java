package com.aidilude.betdice.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "system")
@PropertySource("classpath:property/system.properties")
public class SystemProperties {

    private Long accessTimeLimit;

    private String customerAccessKey;

    private String adminAccessKey;

    private Integer pageSize;

    private Integer maxReceivingPledgorCount;

}