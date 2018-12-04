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

    private Date transferTime;

    private Integer status;

}