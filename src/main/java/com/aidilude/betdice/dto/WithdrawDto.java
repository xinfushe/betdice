package com.aidilude.betdice.dto;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NotNull(message = "提现实体为空")
public class WithdrawDto {

    @NotBlank(message = "质押人账户为空")
    private String pledgorAccount;

    @NotNull(message = "提现金额为空")
    @DecimalMin(value = "0", message = "提现金额小于等于0", inclusive = false)
    private BigDecimal withdrawAmount;

    @NotBlank(message = "交易ID为空")
    private String transactionId;

}