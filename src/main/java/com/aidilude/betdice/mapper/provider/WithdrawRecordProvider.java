package com.aidilude.betdice.mapper.provider;

import com.aidilude.betdice.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class WithdrawRecordProvider {

    public String selectByCondition(@Param("id") Integer id,
                                    @Param("transactionId") String transactionId,
                                    @Param("pledgeRecordId") String pledgeRecordId){
        SQL sql = new SQL();

        sql.SELECT("*");
        sql.FROM("withdraw_record");
        if(id != null)
            sql.WHERE("id = #{id}");
        if(!StringUtils.isEmpty(transactionId))
            sql.WHERE("transaction_id = #{transactionId}");
        if(!StringUtils.isEmpty(pledgeRecordId))
            sql.WHERE("pledge_record_id = #{pledgeRecordId}");

        return sql.toString();
    }

}