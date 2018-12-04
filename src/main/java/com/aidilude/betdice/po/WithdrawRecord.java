package com.aidilude.betdice.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class WithdrawRecord {

    private Integer id;

    private String pledgeRecordId;

    private BigDecimal amount;

    private Date transferTime;

}