package com.aidilude.betdice.mapper;

import com.aidilude.betdice.mapper.provider.MiningRecordProvider;
import com.aidilude.betdice.po.MiningRecord;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface MiningRecordMapper {

    @SelectProvider(type = MiningRecordProvider.class, method = "selectByCondition")
    public List<MiningRecord> selectByCondition(@Param("round") String round,
                                                @Param("walletAddress") String walletAddress,
                                                @Param("currency") String currency);

    @Insert("insert into mining_record(round,wallet_address,currency,mining_amount) " +
            "values(#{round},#{walletAddress},#{currency},#{miningAmount})")
    public Integer insert(MiningRecord miningRecord);

    @Update("update mining_record set mining_amount = mining_amount + #{miningAmount} " +
            "where wallet_address = #{walletAddress} " +
            "and round = #{round} " +
            "and currency = #{currency}")
    public Integer update(MiningRecord miningRecord);

    @Update("CREATE TABLE `mining_record`  ( " +
            "  `round` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, " +
            "  `wallet_address` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, " +
            "  `currency` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, " +
            "  `mining_amount` decimal(25, 5) NULL DEFAULT NULL, " +
            "  PRIMARY KEY (`round`, `wallet_address`, `currency`) USING BTREE " +
            ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;")
    public Integer newTable();

}