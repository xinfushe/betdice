package com.aidilude.betdice.cache;

import java.math.BigDecimal;

public class PledgePoolCache {

    private static BigDecimal totalWithdrawableAmount;   //总可提现质押量

    public static void init(BigDecimal amount){
        setTotalWithdrawableAmount(amount);
    }

    public static void setTotalWithdrawableAmount(BigDecimal amount){
        totalWithdrawableAmount = amount;
    }

    public static BigDecimal getTotalWithdrawableAmount(){
        return totalWithdrawableAmount;
    }

}