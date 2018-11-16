package com.aidilude.betdice.mapper;

import com.aidilude.betdice.po.Rank;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface RankMapper {

    @Insert("insert into rank(round,wallet_address,win_currency,bet_count,bet_amount,win_count,win_amount,mining_amount,mining_currency) " +
            "values(#{round},#{walletAddress},#{winCurrency},#{betCount},#{betAmount},#{winCount},#{winAmount},#{miningAmount},#{miningCurrency})")
    public Integer insert(Rank rank);

    @Select("select p.round,p.wallet_address,p.currency as win_currency,p.bet_amount," +
            "m.currency as mining_currency,m.mining_amount " +
            "from personal_statistics p left join mining_record m " +
            "on p.wallet_address = m.wallet_address and p.round = m.round " +
            "where p.round = #{round} and p.currency = #{winCurrency} " +
            "and m.round = #{round} and m.currency = #{miningCurrency} " +
            "order by bet_amount desc limit 0,#{offset}")
    public List<Rank> selectRanks(@Param("round") String round,
                                  @Param("winCurrency") String winCurrency,
                                  @Param("miningCurrency") String miningCurrency,
                                  @Param("offset") Integer offset);

    @Select("select p.round,p.wallet_address,p.currency as win_currency,p.bet_count,p.bet_amount,p.win_count,p.win_amount," +
            "m.currency as mining_currency,m.mining_amount " +
            "from personal_statistics p left join mining_record m " +
            "on p.wallet_address = m.wallet_address and p.round = m.round " +
            "where p.round = #{round} and p.currency = #{winCurrency} " +
            "and m.round = #{round} and m.currency = #{miningCurrency} " +
            "order by bet_amount desc limit 0,#{offset}")
    public List<Rank> selectRanksWithAllColumn(@Param("round") String round,
                                               @Param("winCurrency") String winCurrency,
                                               @Param("miningCurrency") String miningCurrency,
                                               @Param("offset") Integer offset);

}