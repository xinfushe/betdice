package com.aidilude.betdice.task;

import com.aidilude.betdice.mapper.BonusRecordMapper;
import com.aidilude.betdice.mapper.PledgeRecordMapper;
import com.aidilude.betdice.mapper.TurnMapper;
import com.aidilude.betdice.po.BonusRecord;
import com.aidilude.betdice.po.PledgeRecord;
import com.aidilude.betdice.po.PledgeStatistics;
import com.aidilude.betdice.po.Turn;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.property.SystemProperties;
import com.aidilude.betdice.util.HttpUtils;
import com.aidilude.betdice.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Iterator;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
public class PledgeStatisticsTask {

    @Resource
    private ApiProperties apiProperties;

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private PledgeRecordMapper pledgeRecordMapper;

    @Resource
    private BonusRecordMapper bonusRecordMapper;

    @Resource
    private TurnMapper turnMapper;

    @Scheduled(cron = "30 50 11 * * ?")   //每日23点59分排行统计截止
    public void run() {
        String lastRound = StringUtils.gainLastRound();
        Turn lastTurn = turnMapper.selectByPrimaryKey(lastRound, apiProperties.getShareBonusCurrency());
        if(lastTurn == null){
            log.error("【质押分红统计】轮次不存在");
            return;
        }
        log.info("【质押分红统计】开始执行，轮次：【" + lastRound + "】，奖池账户：【" + lastTurn.getOfficialWalletAddress() + "】");
        String url = apiProperties.getChainRequestProtocol() + apiProperties.getChainIP() + ":" + apiProperties.getChainPort() + apiProperties.getPreffix() + apiProperties.getQueryOfficialAmount() + lastTurn.getOfficialWalletAddress();
        String result = null;
        try {
            result = HttpUtils.doGet(url, null);
        } catch (Exception e) {
            log.error("【质押分红统计】请求奖池余额网络异常", e);
            return;
        }
        if (StringUtils.isEmpty(result)) {
            log.error("【质押分红统计】请求奖池余额结果为空");
            return;
        }
        JSONObject jsonResult = JSONObject.parseObject(result);
        if (jsonResult.getBooleanValue("success") != true) {
            log.error("【质押分红统计】请求奖池余额结果不成功");
            return;
        }
        JSONArray jsonBalances = jsonResult.getJSONArray("balances");
        BigDecimal officialAmount = null;
        for (Iterator iterator = jsonBalances.iterator(); iterator.hasNext(); ) {
            JSONObject jsonBalance = (JSONObject) iterator.next();
            if (jsonBalance.getString("currency").equals(apiProperties.getShareBonusCurrency())) {
                officialAmount = jsonBalance.getBigDecimal("balance");
                break;
            }
        }
        if (officialAmount == null) {
            log.error("【质押分红统计】找不到奖池余额");
            return;
        }
        officialAmount = officialAmount.divide(new BigDecimal(systemProperties.getPledgeBonusRatio()), 0, BigDecimal.ROUND_DOWN);   //奖池的三分之一拿来分红
        BigDecimal allWithdrawAmount = pledgeRecordMapper.selectAllWithdrawAmount(null);
        if (allWithdrawAmount.compareTo(new BigDecimal("0")) == 0) {
            log.error("【质押分红统计】合法质押金额为0");
            return;
        }
        List<PledgeStatistics> pledgeStatisticsList = pledgeRecordMapper.selectWithdrawableStatistics();
        if (pledgeStatisticsList == null || pledgeStatisticsList.size() == 0) {
            log.error("【质押分红统计】没有合法的质押记录");
            return;
        }
        for(PledgeStatistics pledgeStatistics : pledgeStatisticsList){
            BigDecimal ratio = pledgeStatistics.getAllPledgeAmount().divide(allWithdrawAmount, 5, BigDecimal.ROUND_DOWN);
            BigDecimal bonusAmount = ratio.multiply(officialAmount).setScale(0, BigDecimal.ROUND_DOWN);
            BonusRecord bonusRecord = new BonusRecord(lastRound, pledgeStatistics.getPledgorAccount(), ratio, bonusAmount, 0);
            Integer count = bonusRecordMapper.insert(bonusRecord);
            if(count == null || count == 0){
                log.error("【质押分红统计】插入质押分红统计记录异常，质押统计记录：【" + JSON.toJSONString(bonusRecord) + "】");
            }
        }
        log.info("【质押分红统计】执行结束，轮次：【" + lastRound + "】，奖池账户：【" + lastTurn.getOfficialWalletAddress() + "】");
    }

}