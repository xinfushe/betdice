package com.aidilude.betdice.po;

import lombok.Data;

@Data
public class Transaction {

    private String tid;

    private Integer timestamp;

    private String round;

    private String owner;

    private Integer number;

    private Integer numberWin;

    private String amount;

    private String amountWin;

    private String currency;

    private String mining;

    private String miningCurrency;

    private String inviterWallet;

    private String inviterGetAmount;

}