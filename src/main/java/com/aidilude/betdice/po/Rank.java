package com.aidilude.betdice.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Rank {

    private String round;

    private String walletAddress;

    private String winCurrency;

    private Integer betCount;

    private BigDecimal betAmount;

    private Integer winCount;

    private BigDecimal winAmount;

    private BigDecimal miningAmount;

    private String miningCurrency;

}