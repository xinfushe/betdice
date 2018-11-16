package com.aidilude.betdice.mapper;

import com.aidilude.betdice.mapper.provider.TurnProvider;
import com.aidilude.betdice.po.Turn;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.Map;

public interface TurnMapper {

    @Insert("insert into turn(round,currency,official_wallet_address,official_wallet_secret) " +
            "values(#{round},#{currency},#{officialWalletAddress},#{officialWalletSecret})")
    public Integer insert(Turn turn);

    @SelectProvider(type = TurnProvider.class, method = "selectByPrimaryKey")
    public Turn selectByPrimaryKey(@Param("round") String round, @Param("currency") String currency);

    @Update("update turn " +
            "set total_bet_count = total_bet_count + #{totalBetCount}," +
            "total_bet_amount = total_bet_amount + #{totalBetAmount}," +
            "partake_customer_count = partake_customer_count + #{partakeCustomerCount}," +
            "total_win_count = total_win_count + #{totalWinCount}," +
            "total_win_amount = total_win_amount + #{totalWinAmount} " +
            "where round = #{round} " +
            "and currency = #{currency}")
    public Integer updateStatisticsData(Turn turn);

    @Update("update turn set jackpot_amount = #{jackpotAmount} " +
            "where round = #{round} and currency = #{currency}")
    public Integer updateJackpotByPrimaryKey(@Param("jackpotAmount") BigDecimal jackpotAmount, @Param("round") String round, @Param("currency") String currency);

}