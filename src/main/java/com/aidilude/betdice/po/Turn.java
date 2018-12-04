package com.aidilude.betdice.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Turn {

    private String round;

    private String officialWalletAddress;

    private String officialWalletSecret;

    private Integer totalBetCount;

    private BigDecimal totalBetAmount;

    private Integer partakeCustomerCount;

    private Integer totalWinCount;

    private BigDecimal totalWinAmount;

    private BigDecimal jackpotAmount;

    private String currency;

}