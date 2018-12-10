package com.aidilude.betdice.service;

import com.aidilude.betdice.cache.WithdrawCountCache;
import com.aidilude.betdice.component.ApplicationComponent;
import com.aidilude.betdice.dto.PledgeTransactionDto;
import com.aidilude.betdice.dto.WithdrawDto;
import com.aidilude.betdice.mapper.*;
import com.aidilude.betdice.po.*;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.property.SystemProperties;
import com.aidilude.betdice.util.HttpUtils;
import com.aidilude.betdice.util.StringUtils;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class PledgeService {

    @Resource
    private ApiProperties apiProperties;

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private ApplicationComponent applicationComponent;

    @Resource
    private ReceivingAccountMapper receivingAccountMapper;

    @Resource
    private PledgeRecordMapper pledgeRecordMapper;

    @Resource
    private WithdrawRecordMapper withdrawRecordMapper;

    @Resource
    private BonusRecordMapper bonusRecordMapper;

    @Resource
    private TurnMapper turnMapper;

    public ReceivingAccount newReceivingAccount(){
        String newAccount = null;
        try {
            String url = apiProperties.getChainRequestProtocol() + apiProperties.getChainIP() + ":" + apiProperties.getChainPort() + apiProperties.getPreffix() + apiProperties.getNewWalletAddress();
            newAccount = HttpUtils.doGet(url, null);
        } catch (Exception e) {
            log.error("网络错误[代码：1]", e);
            return null;
        }
        if(StringUtil.isEmpty(newAccount)) {
            log.error("网络错误[代码：2]");
            return null;
        }
        JSONObject jsonNewAccount = JSONObject.parseObject(newAccount);
        if(jsonNewAccount.getBooleanValue("success") != true) {
            log.error("网络错误[代码：3]");
            return null;
        }
        ReceivingAccount newReceivingAccount = new ReceivingAccount();
        newReceivingAccount.setWalletAddress(jsonNewAccount.getString("address"));
        newReceivingAccount.setWalletSecret(jsonNewAccount.getString("secret"));
        Integer count = receivingAccountMapper.insert(newReceivingAccount);
        if(count == null || count == 0) {
            log.error("系统错误[代码：1]");
            return null;
        }
        return newReceivingAccount;
    }

    @Transactional(rollbackFor = Exception.class)
    public void recordPledgeTransaction(PledgeTransactionDto pledgeTransactionDto) throws Exception{
        PledgeRecord pledgeRecord = new PledgeRecord();
        BeanUtils.copyProperties(pledgeTransactionDto, pledgeRecord);
        pledgeRecord.setTransferTime(new Date());
        List<PledgeRecord> pledgeRecords = pledgeRecordMapper.selectByCondition(null, pledgeRecord.getPledgorAccount(), pledgeRecord.getReceivingAccount());
        Integer pledgorCount = 0;
        if(pledgeRecords == null || pledgeRecords.size() == 0)
            pledgorCount++;
        Integer count1 = pledgeRecordMapper.insert(pledgeRecord);
        if(count1 == null || count1 == 0)
            throw new Exception("插入质押记录失败");
        Integer count2 = receivingAccountMapper.update(pledgorCount, pledgeRecord.getAmount(), pledgeRecord.getReceivingAccount());
        if(count2 == null || count2 == 0)
            throw new Exception("更新收钱账户失败");
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean withdraw(WithdrawDto withdrawDto) throws Exception {
        BigDecimal withdrawAmount = withdrawDto.getWithdrawAmount();
        BigDecimal allWithdrawAmount = pledgeRecordMapper.selectAllWithdrawAmount(withdrawDto.getPledgorAccount());
        if(allWithdrawAmount.compareTo(new BigDecimal("0")) == 0)
            return false;
        if(allWithdrawAmount.compareTo(withdrawAmount) < 0)
            return false;
        List<PledgeRecord> pledgeRecords = pledgeRecordMapper.selectWithdrawable(withdrawDto.getPledgorAccount());
        if(pledgeRecords == null || pledgeRecords.size() == 0)
            return false;
        boolean isNext = true;   //是否进行下一次循环
        for(PledgeRecord pledgeRecord : pledgeRecords){
            BigDecimal shouldSubAmount;   //本次循环即将提现金额
            if(withdrawAmount.compareTo(pledgeRecord.getAmount()) <= 0){
                shouldSubAmount = withdrawAmount;
                isNext = false;   //一次提完，不再进行下一次循环提现
            }else{
                shouldSubAmount = pledgeRecord.getAmount();
            }
            //1.扣质押记录余额
            Integer count1 = pledgeRecordMapper.reduceAmount(pledgeRecord.getId(), shouldSubAmount);
            if(count1 == null || count1 == 0)
                throw new Exception("扣质押余额异常");
            //2.新增提现记录
            WithdrawRecord withdrawRecord = new WithdrawRecord(withdrawDto.getTransactionId(), pledgeRecord.getId(), shouldSubAmount, new Date());
            Integer count2 = withdrawRecordMapper.insert(withdrawRecord);
            if(count2 == null || count2 == 0)
                throw new Exception("新增提现记录异常");
            if(!isNext)   //跳出循环
                break;
            withdrawAmount = withdrawAmount.subtract(shouldSubAmount);   //扣除已经提现的，进行下一次循环提现
        }
        //3.扣收钱账户余额
        Integer count3 = receivingAccountMapper.update(0, withdrawDto.getWithdrawAmount().multiply(new BigDecimal("-1")), pledgeRecords.get(0).getReceivingAccount());
        if(count3 == null || count3 == 0)
            throw new Exception("扣收钱账户余额异常");
        //4.今日提现次数+1
        Integer todayWithdrawCount = WithdrawCountCache.get(withdrawDto.getPledgorAccount());
        if(todayWithdrawCount == null){
            WithdrawCountCache.put(withdrawDto.getPledgorAccount(), 1);
        }else{
            WithdrawCountCache.put(withdrawDto.getPledgorAccount(), ++todayWithdrawCount);
        }
        return true;
    }

    public void pledgeBonusTransfer(){
        Integer successCount = 0;
        Integer failedCount = 0;
        Integer totalCount = 0;
        String lastRound = StringUtils.gainLastRound();
        log.info("【质押分红转账】开始执行，轮次：【" + lastRound + "】");
        List<BonusRecord> bonusRecords = bonusRecordMapper.selectByCondition(null, lastRound, null, null, 0);
        if(bonusRecords == null || bonusRecords.size() == 0){
            log.error("【质押分红转账】没有质押统计记录");
            return;
        }
        Turn lastTurn = turnMapper.selectByPrimaryKey(lastRound, apiProperties.getShareBonusCurrency());
        if(lastTurn == null){
            log.error("【质押分红转账】没有上一轮投注");
            return;
        }
        for(BonusRecord bonusRecord : bonusRecords){
            String result;
            try {
                result = applicationComponent.transfer(bonusRecord.getAmount().setScale(0, BigDecimal.ROUND_DOWN), lastTurn.getOfficialWalletSecret(), bonusRecord.getPledgorAccount(), apiProperties.getShareBonusCurrency());
            } catch (Exception e) {
                log.error("【质押分红转账】转账网络异常，质押分红统计：【" + JSON.toJSONString(bonusRecord) + "】", e);
                failedCount++;
                totalCount++;
                continue;
            }
            if(StringUtils.isEmpty(result)) {
                log.error("【质押分红转账】转账结果为空，质押分红统计：【" + JSON.toJSONString(bonusRecord) + "】");
                failedCount++;
                totalCount++;
                continue;
            }
            JSONObject jsonResult = JSONObject.parseObject(result);
            if(jsonResult.getBooleanValue("success") != true){
                log.error("【质押分红转账】转账结果不成功，转账结果：【" + jsonResult.toJSONString() + "】，质押分红统计：【" + JSON.toJSONString(bonusRecord) + "】");
                failedCount++;
                totalCount++;
                continue;
            }
            bonusRecord.setTransactionId(jsonResult.getString("transactionId"));
            bonusRecord.setTransferTime(new Date());
            bonusRecord.setStatus(1);
            Integer count = bonusRecordMapper.updateStatus(bonusRecord);
            if(count == null || count == 0){
                log.error("【质押分红转账】质押分红统计状态修改失败，转账结果：【" + jsonResult.toJSONString() + "】，质押分红统计：【" + JSON.toJSONString(bonusRecord) + "】");
                failedCount++;
                totalCount++;
                continue;
            }
            successCount++;
            totalCount++;
        }
        log.info("【质押分红转账】执行结束，轮次：【" + lastRound + "】，总数：【" + totalCount + "】，成功数：【" + successCount + "】，失败数：【" + failedCount + "】");
    }

    public static void main(String[] args) {

    }

}