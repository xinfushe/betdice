package com.aidilude.betdice.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "system.api")
@PropertySource("classpath:property/system.properties")
public class ApiProperties {

    private String chainRequestProtocol;

    private String chainIP;

    private String chainPort;

    private String shareBonusCurrency;

    private String HoldCurrency;

    private String rankWinCurrency;

    private String rankMiningCurrency;

    private Integer rankOffset;

    private String currencys;

    private String preffix;

    private String queryHoldAmount;

    private String queryOfficialAmount;

    private String newWalletAddress;

    private String transfer;

    private Integer transferType;

    private String transferFee;

    private String minHoldAmount;

}