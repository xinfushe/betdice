package com.aidilude.betdice.po;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class PledgeRecord {

    private String id;

    private String pledgorAccount;

    private String receivingAccount;

    private BigDecimal amount;

    private Date transferTime;

}