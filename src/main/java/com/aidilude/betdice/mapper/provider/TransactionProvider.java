package com.aidilude.betdice.mapper.provider;

import com.aidilude.betdice.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class TransactionProvider {

    public String selectByCondition(@Param("tid") String tid,
                                    @Param("round") String round,
                                    @Param("owner") String owner,
                                    @Param("currency") String currency,
                                    @Param("inviterWallet") String inviterWallet){
        SQL sql = new SQL();

        sql.SELECT("*");
        sql.FROM("transaction");
        if(!StringUtils.isEmpty(tid))
            sql.WHERE("tid = #{tid}");
        if(!StringUtils.isEmpty(round))
            sql.WHERE("round = #{round}");
        if(!StringUtils.isEmpty(owner))
            sql.WHERE("owner = #{owner}");
        if(!StringUtils.isEmpty(currency))
            sql.WHERE("currency = #{currency}");
        if(!StringUtils.isEmpty(inviterWallet))
            sql.WHERE("inviter_wallet = #{inviterWallet}");
        sql.ORDER_BY("timestamp desc");

        return sql.toString();
    }

}