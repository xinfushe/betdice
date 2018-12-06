package com.aidilude.betdice.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class BonusRecord {

    private Integer id;

    private String round;

    private String pledgorAccount;

    private BigDecimal ratio;

    private BigDecimal amount;

    private String transactionId;

    private Date transferTime;

    private Integer status;

    public BonusRecord(String round, String pledgorAccount, BigDecimal ratio, BigDecimal amount, Integer status) {
        this.round = round;
        this.pledgorAccount = pledgorAccount;
        this.ratio = ratio;
        this.amount = amount;
        this.status = status;
    }

}