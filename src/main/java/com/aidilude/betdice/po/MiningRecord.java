package com.aidilude.betdice.po;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MiningRecord {

    private String round;

    private String walletAddress;

    private String currency;

    private BigDecimal miningAmount;

}