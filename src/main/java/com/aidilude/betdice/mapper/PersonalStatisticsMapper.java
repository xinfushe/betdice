package com.aidilude.betdice.mapper;

import com.aidilude.betdice.mapper.provider.MiningRecordProvider;
import com.aidilude.betdice.mapper.provider.PersonalStatisticsProvider;
import com.aidilude.betdice.po.PersonalStatistics;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface PersonalStatisticsMapper {

    @SelectProvider(type = PersonalStatisticsProvider.class, method = "selectByCondition")
    public List<PersonalStatistics> selectByCondition(@Param("round") String round,
                                                      @Param("walletAddress") String walletAddress,
                                                      @Param("currency") String currency);

    @Insert("insert into personal_statistics(round,wallet_address,currency,bet_count,bet_amount,win_count,win_amount) " +
            "values(#{round},#{walletAddress},#{currency},#{betCount},#{betAmount},#{winCount},#{winAmount})")
    public Integer insert(PersonalStatistics personalStatistics);

    @Update("update personal_statistics " +
            "set bet_count = bet_count + #{betCount}," +
            "bet_amount = bet_amount + #{betAmount}," +
            "win_count = win_count + #{winCount}," +
            "win_amount = win_amount + #{winAmount} " +
            "where wallet_address = #{walletAddress} " +
            "and round = #{round} " +
            "and currency = #{currency}")
    public Integer update(PersonalStatistics personalStatistics);

    @Update("CREATE TABLE `personal_statistics`  ( " +
            "  `round` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, " +
            "  `wallet_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, " +
            "  `currency` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, " +
            "  `bet_count` int(11) NULL DEFAULT 0, " +
            "  `bet_amount` decimal(25, 5) NULL DEFAULT 0.00000, " +
            "  `win_count` int(11) NULL DEFAULT 0, " +
            "  `win_amount` decimal(25, 5) NULL DEFAULT 0.00000, " +
            "  PRIMARY KEY (`round`, `wallet_address`, `currency`) USING BTREE " +
            ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;")
    public Integer newTable();

}