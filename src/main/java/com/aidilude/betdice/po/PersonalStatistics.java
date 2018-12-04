package com.aidilude.betdice.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PersonalStatistics {

    private String round;

    private String walletAddress;

    private String currency;

    private Integer betCount;

    private BigDecimal betAmount;

    private Integer winCount;

    private BigDecimal winAmount;

    public PersonalStatistics(String round, String walletAddress, String currency) {
        this.round = round;
        this.walletAddress = walletAddress;
        this.currency = currency;
    }

}