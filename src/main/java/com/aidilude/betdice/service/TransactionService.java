package com.aidilude.betdice.service;

import com.aidilude.betdice.mapper.*;
import com.aidilude.betdice.po.*;
import com.aidilude.betdice.util.Result;
import com.aidilude.betdice.util.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    @Resource
    private TransactionMapper transactionMapper;

    @Resource
    private TurnMapper turnMapper;

    @Resource
    private PersonalStatisticsMapper personalStatisticsMapper;

    @Resource
    private InviteRecordMapper inviteRecordMapper;

    @Resource
    private MiningRecordMapper miningRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    public void newTransaction(Transaction transaction) throws Exception {
        Turn turn = new Turn();
        List<Transaction> transactions = transactionMapper.selectByCondition(null, transaction.getRound(), transaction.getOwner(), null, null);
        if(transactions == null)
            turn.setPartakeCustomerCount(1);
        else if(transactions.size() == 0)
            turn.setPartakeCustomerCount(1);
        else
            turn.setPartakeCustomerCount(0);
        //1.记录本条投注记录
        Integer count1 = transactionMapper.insert(transaction);
        if(count1 == null || count1 == 0)
            throw new Exception();
        turn.setTotalBetCount(1);
        turn.setTotalBetAmount(new BigDecimal(transaction.getAmount()));
        if(transaction.getAmountWin().equals("0")) {
            turn.setTotalWinCount(0);
            turn.setTotalWinAmount(new BigDecimal("0"));
        }else{
            turn.setTotalWinCount(1);
            turn.setTotalWinAmount(new BigDecimal(transaction.getAmountWin()));
        }
        turn.setRound(transaction.getRound());
        turn.setCurrency(transaction.getCurrency());
        //2.更新本轮统计数据
        Integer count2 = turnMapper.updateStatisticsData(turn);
        if(count2 == null || count2 == 0)
            throw new Exception();
        //3.更新个人统计数据
        PersonalStatistics personalStatistics = new PersonalStatistics(transaction.getRound(), transaction.getOwner(), transaction.getCurrency());
        personalStatistics.setBetCount(1);
        personalStatistics.setBetAmount(new BigDecimal(transaction.getAmount()));
        if(transaction.getAmountWin().equals("0")){
            personalStatistics.setWinCount(0);
            personalStatistics.setWinAmount(new BigDecimal("0"));
        }else{
            personalStatistics.setWinCount(1);
            personalStatistics.setWinAmount(new BigDecimal(transaction.getAmountWin()));
        }
        Integer count3;
        List<PersonalStatistics> personalStatisticsList = personalStatisticsMapper.selectByCondition(transaction.getRound(), transaction.getOwner(), transaction.getCurrency());
        if(personalStatisticsList != null && personalStatisticsList.size() > 0){   //已经统计过
            count3 = personalStatisticsMapper.update(personalStatistics);
        }else{
            count3 = personalStatisticsMapper.insert(personalStatistics);
        }
        if(count3 == null || count3 == 0)
            throw new Exception();
        //4.更新个人挖矿记录
        MiningRecord miningRecord = new MiningRecord(transaction.getRound(), transaction.getOwner(), transaction.getMiningCurrency(), new BigDecimal(transaction.getMining()));
        Integer count4;
        List<MiningRecord> miningRecords = miningRecordMapper.selectByCondition(transaction.getRound(), transaction.getOwner(), transaction.getMiningCurrency());
        if(miningRecords != null && miningRecords.size() > 0){
            count4 = miningRecordMapper.update(miningRecord);
        }else{
            count4 = miningRecordMapper.insert(miningRecord);
        }
        if(count4 == null || count4 == 0)
            throw new Exception();
        //5.更新邀请人的邀请记录
        if(!StringUtils.isEmpty(transaction.getInviterWallet())){   //有邀请人
            InviteRecord inviteRecord = new InviteRecord(transaction.getInviterWallet());
            List<Transaction> t = transactionMapper.selectByCondition(null, null, transaction.getOwner(), null, transaction.getInviterWallet());
            if(t != null && t.size() > 1){   //原本有邀请记录
                inviteRecord.setInviteCount(0);
            }else{   //没有邀请记录的
                inviteRecord.setInviteCount(1);
            }
            inviteRecord.setInviteRewardAmount(new BigDecimal(transaction.getInviterGetAmount()));
            if(transaction.getInviterGetAmount().equals("0")){
                inviteRecord.setInviteRewardCount(0);
            }else{
                inviteRecord.setInviteRewardCount(1);
            }
            Integer count5;
            InviteRecord temp = inviteRecordMapper.select(transaction.getInviterWallet());
            if(temp != null){   //邀请人有邀请记录
                count5 = inviteRecordMapper.update(inviteRecord);
            }else{
                count5 = inviteRecordMapper.insert(inviteRecord);
            }
            if(count5 == null || count5 == 0)
                throw new Exception();
        }
    }

}