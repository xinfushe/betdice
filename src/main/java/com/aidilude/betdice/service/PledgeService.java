package com.aidilude.betdice.service;

import com.aidilude.betdice.dto.PledgeTransaction;
import com.aidilude.betdice.mapper.PledgeRecordMapper;
import com.aidilude.betdice.mapper.ReceivingAccountMapper;
import com.aidilude.betdice.po.PledgeRecord;
import com.aidilude.betdice.po.ReceivingAccount;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.util.HttpUtils;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class PledgeService {

    @Resource
    private ApiProperties apiProperties;

    @Resource
    private ReceivingAccountMapper receivingAccountMapper;

    @Resource
    private PledgeRecordMapper pledgeRecordMapper;

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
    public void recordPledgeTransaction(PledgeTransaction pledgeTransaction) throws Exception{
        PledgeRecord pledgeRecord = new PledgeRecord();
        BeanUtils.copyProperties(pledgeTransaction, pledgeRecord);
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

}