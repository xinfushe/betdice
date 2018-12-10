package com.aidilude.betdice.controller.api;

import com.aidilude.betdice.mapper.BonusRecordMapper;
import com.aidilude.betdice.mapper.PledgeRecordMapper;
import com.aidilude.betdice.property.SystemProperties;
import com.aidilude.betdice.util.Result;
import com.aidilude.betdice.util.ResultCode;
import com.aidilude.betdice.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bonus")
public class BonusController {

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private PledgeRecordMapper pledgeRecordMapper;

    @Resource
    private BonusRecordMapper bonusRecordMapper;

    @GetMapping("/queryBonusStatistics")
    @ApiOperation(value = "查询分红统计", response = Result.class)
    public Result queryBonusStatistics(@ApiParam(name = "pledgorAccount", value = "质押人账户", required = true) @RequestParam String pledgorAccount) {
        if (StringUtils.isEmpty(pledgorAccount))
            return Result.returnMsg(ResultCode.InvalidParam, "质押人账户为空");
        //1.统计总质押量
        BigDecimal totalPledgeAmount = pledgeRecordMapper.selectTotalPledgeAmount(pledgorAccount);
        //2.统计可提现金额
        BigDecimal withdrawAbleAmount = pledgeRecordMapper.selectAllWithdrawAmount(pledgorAccount);
        //3.统计历史所得分红
        BigDecimal historyBonusAmount = bonusRecordMapper.selectAmountByCondition(null, null, pledgorAccount, null, 1);
        //4.统计今日预估分红（比例）
        BigDecimal totalWithdrawAbleAmount = pledgeRecordMapper.selectAllWithdrawAmount(null);
        BigDecimal todayBonusExpectRatio = withdrawAbleAmount.divide(totalWithdrawAbleAmount, 5, BigDecimal.ROUND_DOWN);
        Map<String, Object> result = new HashMap<>();
        result.put("totalPledgeAmount", totalPledgeAmount);
        result.put("withdrawAbleAmount", withdrawAbleAmount);
        result.put("historyBonusAmount", historyBonusAmount);
        result.put("todayBonusExpectRatio", todayBonusExpectRatio);
        return Result.returnSingleData(result);
    }

    @GetMapping("/queryPoolBonus")
    @ApiOperation(value = "查询奖池分红信息", response = Result.class)
    public Result queryPoolBonus(){
        Map<String, Object> result = new HashMap<>();
        //统计每10万个ASCHBet质押的XAS收益
        BigDecimal totalWithdrawAbleAmount = pledgeRecordMapper.selectAllWithdrawAmount(null);
        BigDecimal unit = new BigDecimal("10000000000000");
        BigDecimal unitBonusExpectRatio = unit.divide(totalWithdrawAbleAmount, 5, BigDecimal.ROUND_DOWN);
        result.put("unitBonusExpectRatio", unitBonusExpectRatio);
        result.put("poolBonusRatio", systemProperties.getPledgeBonusRatio());
        return Result.returnSingleData(result);
    }

    @GetMapping("/queryRecentPledgeRecord")
    @ApiOperation(value = "查询最新质押记录", response = Result.class)
    public Result queryRecentPledgeRecord(){
        List<Map<String, Object>> result = pledgeRecordMapper.selectRecent();
        return Result.returnSingleData(result);
    }

}