package com.aidilude.betdice.po;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ReceivingAccount {

    private String walletAddress;

    private String walletSecret;

    private Integer pledgorCount;

    private BigDecimal pledgeAmount;

}