package com.aidilude.betdice.service;

import com.aidilude.betdice.component.ApplicationComponent;
import com.aidilude.betdice.dto.PledgeTransactionDto;
import com.aidilude.betdice.dto.WithdrawDto;
import com.aidilude.betdice.mapper.PledgeRecordMapper;
import com.aidilude.betdice.mapper.ReceivingAccountMapper;
import com.aidilude.betdice.mapper.WithdrawRecordMapper;
import com.aidilude.betdice.po.PledgeRecord;
import com.aidilude.betdice.po.ReceivingAccount;
import com.aidilude.betdice.po.WithdrawRecord;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.property.SystemProperties;
import com.aidilude.betdice.util.HttpUtils;
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
        return true;
    }

    public static void main(String[] args) {

    }

}