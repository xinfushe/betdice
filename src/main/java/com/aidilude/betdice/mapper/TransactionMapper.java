package com.aidilude.betdice.mapper;

import com.aidilude.betdice.mapper.provider.TransactionProvider;
import com.aidilude.betdice.po.Transaction;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

public interface TransactionMapper {

    @Insert("insert into transaction(tid,timestamp,round,owner,number,number_win,amount,amount_win,currency,mining,mining_currency,inviter_wallet,inviter_get_amount) " +
            "values(#{tid},#{timestamp},#{round},#{owner},#{number},#{numberWin},#{amount},#{amountWin},#{currency},#{mining},#{miningCurrency},#{inviterWallet},#{inviterGetAmount})")
    public Integer insert(Transaction transaction);

    @SelectProvider(type = TransactionProvider.class, method = "selectByCondition")
    public List<Transaction> selectByCondition(@Param("tid") String tid,
                                               @Param("round") String round,
                                               @Param("owner") String owner,
                                               @Param("currency") String currency,
                                               @Param("inviterWallet") String inviterWallet);

    @Update("CREATE TABLE `transaction`  ( " +
            "  `tid` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL, " +
            "  `timestamp` int(11) NULL DEFAULT NULL COMMENT '交易时间戳', " +
            "  `round` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '轮次', " +
            "  `owner` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '当前转账地址', " +
            "  `number` int(11) NULL DEFAULT NULL COMMENT '小于改号码获胜', " +
            "  `number_win` int(11) NULL DEFAULT NULL COMMENT '开奖号码', " +
            "  `amount` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投注金额', " +
            "  `amount_win` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '获奖金额', " +
            "  `currency` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '币种', " +
            "  `mining` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '挖矿奖励', " +
            "  `mining_currency` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '挖矿币种', " +
            "  `inviter_wallet` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '邀请人地址', " +
            "  `inviter_get_amount` decimal(25, 5) NULL DEFAULT NULL COMMENT '邀请人所得奖励', " +
            "  PRIMARY KEY (`tid`) USING BTREE " +
            ") ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;")
    public Integer newTable();

}