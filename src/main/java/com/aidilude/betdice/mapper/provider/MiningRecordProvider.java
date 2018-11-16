package com.aidilude.betdice.mapper.provider;

import com.aidilude.betdice.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class MiningRecordProvider {

    public String selectByCondition(@Param("round") String round,
                                    @Param("walletAddress") String walletAddress,
                                    @Param("currency") String currency){
        SQL sql = new SQL();

        sql.SELECT("*");
        sql.FROM("mining_record");
        if(StringUtils.isRound(round))
            sql.WHERE("round = #{round}");
        if(!StringUtils.isEmpty(walletAddress))
            sql.WHERE("wallet_address = #{walletAddress}");
        if(!StringUtils.isEmpty(currency))
            sql.WHERE("currency = #{currency}");

        return sql.toString();
    }

}