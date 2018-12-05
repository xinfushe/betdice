package com.aidilude.betdice.mapper;

import com.aidilude.betdice.mapper.provider.WithdrawRecordProvider;
import com.aidilude.betdice.po.WithdrawRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;

public interface WithdrawRecordMapper {

    @Insert("insert into withdraw_record(transaction_id,pledge_record_id,amount,transfer_time) " +
            "values(#{transactionId},#{pledgeRecordId},#{amount},#{transferTime})")
    public Integer insert(WithdrawRecord withdrawRecord);

    @SelectProvider(type = WithdrawRecordProvider.class, method = "selectByCondition")
    public List<WithdrawRecord> selectByCondition(@Param("id") Integer id,
                                                  @Param("transactionId") String transactionId,
                                                  @Param("pledgeRecordId") String pledgeRecordId);

}