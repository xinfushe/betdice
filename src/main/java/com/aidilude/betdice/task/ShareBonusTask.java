package com.aidilude.betdice.task;

import com.aidilude.betdice.component.ApplicationComponent;
import com.aidilude.betdice.mapper.TurnMapper;
import com.aidilude.betdice.po.Turn;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.util.HttpUtils;
import com.aidilude.betdice.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Configuration
@EnableScheduling
public class ShareBonusTask {

    private static final Logger logger = LoggerFactory.getLogger(ShareBonusTask.class);

    @Resource
    private ApiProperties apiProperties;

    @Resource
    private TurnMapper turnMapper;

    @Resource
    private ApplicationComponent applicationComponent;

//    @Scheduled(cron = "0 0 12 * * ?")   //秒 分 时 日 月 星期 年
    public void run() {
        String lastRound = StringUtils.gainLastRound();
        Turn turn = turnMapper.selectByPrimaryKey(lastRound, apiProperties.getShareBonusCurrency());
        if(turn == null){
            logger.info("游戏轮次：【" + lastRound + "】不存在");
            return;
        }
        String queryOfficialAmountUrl = apiProperties.getChainRequestProtocol() + apiProperties.getChainIP() + ":" + apiProperties.getChainPort() + apiProperties.getPreffix() + apiProperties.getQueryOfficialAmount();
        String officialAmountResult = null;
        try {
            officialAmountResult = HttpUtils.doGet(queryOfficialAmountUrl + turn.getOfficialWalletAddress(), null);
        } catch (Exception e) {
            logger.error("【" + lastRound + "】官方账户余额网络请求异常", e);
            return;
        }
        if(StringUtils.isEmpty(officialAmountResult)){
            logger.error("【" + lastRound + "】官方账户余额网络请求异常");
            return;
        }
        JSONObject jsonOfficialAmount = JSONObject.parseObject(officialAmountResult);
        if(jsonOfficialAmount.getBooleanValue("success") != true){
            logger.error("【" + lastRound + "】官方账户余额请求结果失败，请求结果：【" + jsonOfficialAmount.toJSONString() + "】");
            return;
        }
        JSONArray officialCurrencys = jsonOfficialAmount.getJSONArray("balances");
        JSONObject officialAmount = null;
        for (Iterator iterator = officialCurrencys.iterator(); iterator.hasNext(); ) {
            JSONObject officialCurrency = (JSONObject) iterator.next();
            if(officialCurrency.getString("currency").equals(apiProperties.getShareBonusCurrency())) {
                officialAmount = officialCurrency;
                break;
            }
        }
        if(officialAmount == null){
            logger.error("【" + lastRound + "】官方账户没有【" + apiProperties.getShareBonusCurrency() + "】余额");
            return;
        }
        BigDecimal officialBalance = officialAmount.getBigDecimal("balance");
        if(officialBalance.compareTo(new BigDecimal("0")) <= 0){
            logger.error("【" + lastRound + "】官方账户余额不足，余额：【" + officialBalance.toString() + "】，币种：【" + officialAmount.getString("currency") + "】");
            return;
        }

        logger.info("【" + lastRound + "】轮分红开始，本轮官方钱包地址：【" + turn.getOfficialWalletAddress() + "】，官方账户余额：【" + officialBalance + "】");

        //查询本次用户持有
        String queryHoldCurrencyUrl = apiProperties.getChainRequestProtocol() + apiProperties.getChainIP() + ":" + apiProperties.getChainPort() + apiProperties.getPreffix() + apiProperties.getQueryHoldAmount() + "?min=" + apiProperties.getMinHoldAmount() + "&currency=" + apiProperties.getHoldCurrency();
        String holdCurrencyResult = null;
        try {
            holdCurrencyResult = HttpUtils.doGet(queryHoldCurrencyUrl, null);
        } catch (Exception e) {
            logger.error("【" + lastRound + "】用户持有币网络请求异常", e);
            return;
        }
        if(StringUtils.isEmpty(holdCurrencyResult)){
            logger.error("【" + lastRound + "】用户持有币网络请求异常");
            return;
        }
        JSONObject jsonHoldCurrency = JSONObject.parseObject(holdCurrencyResult);
        if(jsonHoldCurrency.getBooleanValue("success") != true){
            logger.error("【" + lastRound + "】用户持有币请求结果失败，请求结果：【" + jsonHoldCurrency.toJSONString() + "】");
            return;
        }
        JSONArray holdCurrencys = jsonHoldCurrency.getJSONArray("users");

        //统计总持有
        BigDecimal totalHoldCurrency = new BigDecimal("0");
        for (Iterator iterator = holdCurrencys.iterator(); iterator.hasNext(); ) {
            JSONObject holdCurrency = (JSONObject) iterator.next();
            if(holdCurrency.getString("currency").equals(apiProperties.getHoldCurrency()) && holdCurrency.getBigDecimal("balance").compareTo(new BigDecimal("0")) > 0)
                totalHoldCurrency = totalHoldCurrency.add(holdCurrency.getBigDecimal("balance"));
        }

        //统计每个用户的持有比例
        List<Map<String, Object>> holdRatios = new ArrayList<>();
        for (Iterator iterator = holdCurrencys.iterator(); iterator.hasNext(); ) {
            JSONObject holdCurrency = (JSONObject) iterator.next();
            if(holdCurrency.getString("currency").equals(apiProperties.getHoldCurrency()) && holdCurrency.getBigDecimal("balance").compareTo(new BigDecimal("0")) > 0){
                Map<String, Object> holdRatio = new HashMap<>();
                holdRatio.put("address", holdCurrency.getString("address"));
                holdRatio.put("ratio", holdCurrency.getBigDecimal("balance").divide(totalHoldCurrency, 2, BigDecimal.ROUND_DOWN));
                holdRatios.add(holdRatio);
            }
        }

        //调用区块链接口转账
        Integer successCount = 0;
        Integer failedCount = 0;
        Integer totalCount = 0;
        BigDecimal totalTransferAmount = new BigDecimal("0");
        for(Map<String, Object> holdRatio : holdRatios){
            if(new BigDecimal(holdRatio.get("ratio").toString()).compareTo(new BigDecimal(0)) > 0){
                BigDecimal customerGet = officialBalance.multiply((BigDecimal) holdRatio.get("ratio")).setScale(0, BigDecimal.ROUND_DOWN);
                String transferResult = null;
                try {
//                    transferResult = applicationComponent.transfer(customerGet, turn.getOfficialWalletSecret(), (String) holdRatio.get("address"));
                } catch (Exception e) {
                    logger.error("【" + lastRound + "】分红转账异常，用户：【" + holdRatio.get("address").toString() + "】，应得金额：【" + customerGet.toString() + "】币种：【" + apiProperties.getShareBonusCurrency() + "】", e);
                    failedCount++;
                    totalCount++;
                    continue;
                }
                if(StringUtils.isEmpty(transferResult)) {
                    logger.error("【" + lastRound + "】分红转账异常，用户：【" + holdRatio.get("address").toString() + "】，应得金额：【" + customerGet.toString() + "】币种：【" + apiProperties.getShareBonusCurrency() + "】");
                    failedCount++;
                    totalCount++;
                    continue;
                }
                JSONObject jsonTransferResult = JSONObject.parseObject(transferResult);
                if(jsonTransferResult.getBooleanValue("success") != true) {
                    logger.error("【" + lastRound + "】分红转账异常，用户：【" + holdRatio.get("address").toString() + "】，应得金额：【" + customerGet.toString() + "】币种：【" + apiProperties.getShareBonusCurrency() + "】，异常返回：【" + transferResult + "】");
                    failedCount++;
                    totalCount++;
                    continue;
                }
                logger.error("【" + lastRound + "】分红转账成功，交易ID：【" + jsonTransferResult.getString("transactionId") + "】，用户：【" + holdRatio.get("address").toString() + "】，所得金额：【" + customerGet.toString() + "】币种：【" + apiProperties.getShareBonusCurrency() + "】");
                successCount++;
                totalCount++;
                totalTransferAmount = totalTransferAmount.add(customerGet);
            }
        }
        logger.info("【" + lastRound + "】轮分红结束，转账总金额：【" + totalTransferAmount.toString() + "】，处理总数：【" + totalCount + "】，成功：【" + successCount + "】个，失败：【" + failedCount + "】个");
        turnMapper.updateJackpotByPrimaryKey(officialBalance, lastRound, apiProperties.getShareBonusCurrency());
    }

    public static void main(String[] args) {

    }

}
