package com.aidilude.betdice.mapper;

import com.aidilude.betdice.po.ReceivingAccount;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;

public interface ReceivingAccountMapper {

    @Select("select * from receiving_account order by pledgor_count asc limit 0,1")
    public ReceivingAccount selectMinPledgeCountAccount();

    @Insert("insert into receiving_account(wallet_address,wallet_secret,pledgor_count,pledge_amount) " +
            "values(#{walletAddress},#{walletSecret},0,0)")
    public Integer insert(ReceivingAccount receivingAccount);

    @Select("select * from receiving_account where wallet_address = #{walletAddress}")
    public ReceivingAccount selectByWalletAddress(String walletAddress);

    @Update("update receiving_account " +
            "set pledgor_count = pledgor_count + #{pledgorCount}," +
            "pledge_amount = pledge_amount + #{pledgeAmount} " +
            "where wallet_address = #{walletAddress}")
    public Integer update(@Param("pledgorCount") Integer pledgorCount,
                          @Param("pledgeAmount") BigDecimal pledgeAmount,
                          @Param("walletAddress") String walletAddress);

}