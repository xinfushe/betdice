package com.aidilude.betdice.mapper;

import com.aidilude.betdice.mapper.provider.PledgeRecordProvider;
import com.aidilude.betdice.po.PledgeRecord;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;

public interface PledgeRecordMapper {

    @SelectProvider(type = PledgeRecordProvider.class, method = "selectByCondition")
    public List<PledgeRecord> selectByCondition(@Param("id") String id,
                                                @Param("pledgorAccount") String pledgorAccount,
                                                @Param("receivingAccount")String receivingAccount);

    @Insert("insert into pledge_record(id,pledgor_account,receiving_account,amount,transfer_time) " +
            "values(#{id},#{pledgorAccount},#{receivingAccount},#{amount},#{transferTime})")
    public Integer insert(PledgeRecord pledgeRecord);

    @Select("select * from pledge_record " +
            "where pledgor_account = #{pledgorAccount} " +
            "and amount > 0 " +
            "and TIMESTAMPDIFF(HOUR,transfer_time,NOW()) >= 24 " +
            "order by transfer_time asc")
    public List<PledgeRecord> selectWithdrawable(String pledgorAccount);

    @Select("select IFNULL(SUM(amount),0) from pledge_record " +
            "where pledgor_account = #{pledgorAccout} " +
            "and amount > 0 " +
            "and TIMESTAMPDIFF(HOUR,transfer_time,NOW()) >= 24")
    public BigDecimal selectAllWithdrawAmount(String pledgorAccout);

    @Update("update pledge_record set amount = amount - #{amount} where id = #{id}")
    public Integer reduceAmount(@Param("id") String id, @Param("amount") BigDecimal amount);

}