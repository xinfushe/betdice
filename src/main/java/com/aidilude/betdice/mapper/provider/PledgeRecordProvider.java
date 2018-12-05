package com.aidilude.betdice.mapper.provider;

import com.aidilude.betdice.po.PledgeRecord;
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

}