package com.aidilude.betdice.mapper.provider;

import com.aidilude.betdice.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class BonusRecordProvider {

    public String selectByCondition(@Param("id") Integer id,
                                    @Param("round") String round,
                                    @Param("pledgorAccount") String pledgorAccount,
                                    @Param("transactionId") String transactionId,
                                    @Param("status") Integer status){
        SQL sql = new SQL();

        sql.SELECT("*");
        sql.FROM("bonus_record");
        if(id != null)
            sql.WHERE("id = #{id}");
        if(!StringUtils.isEmpty(round))
            sql.WHERE("round = #{round}");
        if(!StringUtils.isEmpty(pledgorAccount))
            sql.WHERE("pledgor_account = #{pledgorAccount}");
        if(!StringUtils.isEmpty(transactionId))
            sql.WHERE("transaction_id = #{transactionId}");
        if(status != null)
            sql.WHERE("status = #{status}");

        return sql.toString();
    }

    public String selectAmountByCondition(@Param("id") Integer id,
                                          @Param("round") String round,
                                          @Param("pledgorAccount") String pledgorAccount,
                                          @Param("transactionId") String transactionId,
                                          @Param("status") Integer status){
        SQL sql = new SQL();

        sql.SELECT("IFNULL(SUM(amount),0)");
        sql.FROM("bonus_record");
        if(id != null)
            sql.WHERE("id = #{id}");
        if(!StringUtils.isEmpty(round))
            sql.WHERE("round = #{round}");
        if(!StringUtils.isEmpty(pledgorAccount))
            sql.WHERE("pledgor_account = #{pledgorAccount}");
        if(!StringUtils.isEmpty(transactionId))
            sql.WHERE("transaction_id = #{transactionId}");
        if(status != null)
            sql.WHERE("status = #{status}");

        return sql.toString();
    }

}