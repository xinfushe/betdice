package com.aidilude.betdice.mapper.provider;

import com.aidilude.betdice.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class RankProvider {

    public String selectByCondition(@Param("round") String round,
                                    @Param("currency") String currency){
        SQL sql = new SQL();

        sql.SELECT("round,wallet_address,win_currency,bet_amount,mining_currency,mining_amount");
        sql.FROM("rank");
        if(!StringUtils.isEmpty(round))
            sql.WHERE("round = #{round}");
        if(!StringUtils.isEmpty(currency))
            sql.WHERE("currency = #{currency}");

        return sql.toString();
    }

}