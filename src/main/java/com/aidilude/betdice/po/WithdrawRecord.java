package com.aidilude.betdice.po;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WithdrawRecord {

    private Integer id;

    private String transactionId;

    private String pledgeRecordId;

    private BigDecimal amount;

    private Date transferTime;

    public WithdrawRecord(String transactionId, String pledgeRecordId, BigDecimal amount, Date transferTime) {
        this.transactionId = transactionId;
        this.pledgeRecordId = pledgeRecordId;
        this.amount = amount;
        this.transferTime = transferTime;
    }

}