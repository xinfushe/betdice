package com.aidilude.betdice.mapper.provider;

import com.aidilude.betdice.util.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.jdbc.SQL;

public class PledgeRecordProvider {

    public String selectByCondition(@Param("id") String id,
                                    @Param("pledgorAccount") String pledgorAccount,
                                    @Param("receivingAccount")String receivingAccount){
        SQL sql = new SQL();

        sql.SELECT("*");
        sql.FROM("pledge_record");
        if(!StringUtils.isEmpty(id))
            sql.WHERE("id = #{id}");
        if(!StringUtils.isEmpty(pledgorAccount))
            sql.WHERE("pledgor_account = #{pledgorAccount}");
        if(!StringUtils.isEmpty(receivingAccount))
            sql.WHERE("receiving_account = #{receivingAccount}");

        return sql.toString();
    }

    public String selectAllWithdrawAmount(String pledgorAccout){
        SQL sql = new SQL();

        sql.SELECT("IFNULL(SUM(amount),0)");
        sql.FROM("pledge_record");
        if(!StringUtils.isEmpty(pledgorAccout))
            sql.WHERE("pledgor_account = #{pledgorAccout}");
        sql.WHERE("TIMESTAMPDIFF(HOUR,transfer_time,NOW()) >= 24");
        sql.WHERE("amount > 0");

        return sql.toString();
    }

    public String selectTotalPledgeAmount(String pledgeAccount){
        SQL sql = new SQL();

        sql.SELECT("IFNULL(SUM(amount),0)");
        sql.FROM("pledge_record");
        if(!StringUtils.isEmpty(pledgeAccount))
            sql.WHERE("pledgor_account = #{pledgorAccout}");
        sql.WHERE("amount > 0");

        return sql.toString();
    }

}