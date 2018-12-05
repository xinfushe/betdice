package com.aidilude.betdice.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@NotNull(message = "转账记录为空")
public class PledgeTransactionDto {

    @NotBlank(message = "交易ID为空")
    private String id;

    @NotBlank(message = "质押人账户为空")
    private String pledgorAccount;

    @NotBlank(message = "收钱账户为空")
    private String receivingAccount;

    @NotNull(message = "质押金额为空")
    @DecimalMin(value = "10000000000000", message = "质押金不能低于10万")
    private BigDecimal amount;

}