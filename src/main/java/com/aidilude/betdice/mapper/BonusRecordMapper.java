package com.aidilude.betdice.mapper;

import com.aidilude.betdice.mapper.provider.BonusRecordProvider;
import com.aidilude.betdice.po.BonusRecord;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

public interface BonusRecordMapper {

    @Insert("insert into bonus_record(round,pledgor_account,ratio,amount,status) " +
            "values(#{round},#{pledgorAccount},#{ratio},#{amount},#{status})")
    public Integer insert(BonusRecord bonusRecord);

    @Update("update bonus_record set status = #{status},transaction_id = #{transactionId},transfer_time = #{transferTime} where id = #{id}")
    public Integer updateStatus(BonusRecord bonusRecord);

    @SelectProvider(type = BonusRecordProvider.class, method = "selectByCondition")
    public List<BonusRecord> selectByCondition(@Param("id") Integer id,
                                               @Param("round") String round,
                                               @Param("pledgorAccount") String pledgorAccount,
                                               @Param("transactionId") String transactionId,
                                               @Param("status") Integer status);

    @SelectProvider(type = BonusRecordProvider.class, method = "selectAmountByCondition")
    public BigDecimal selectAmountByCondition(@Param("id") Integer id,
                                              @Param("round") String round,
                                              @Param("pledgorAccount") String pledgorAccount,
                                              @Param("transactionId") String transactionId,
                                              @Param("status") Integer status);

}