package com.aidilude.betdice.task;

import com.aidilude.betdice.mapper.BaseMapper;
import com.aidilude.betdice.mapper.MiningRecordMapper;
import com.aidilude.betdice.mapper.PersonalStatisticsMapper;
import com.aidilude.betdice.mapper.TransactionMapper;
import com.aidilude.betdice.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
@Slf4j
public class SubTableTask {

    @Resource
    private BaseMapper baseMapper;

    @Resource
    private MiningRecordMapper miningRecordMapper;

    @Resource
    private PersonalStatisticsMapper personalStatisticsMapper;

    @Resource
    private TransactionMapper transactionMapper;

    @Scheduled(cron = "0 0 0 * * ?")   //每日0点0分0秒准时分表
    public void run() throws Exception {
        log.info("【分表任务】开始执行...");
        String yesterday = StringUtils.gainLastRound().replace("-", "");
        baseMapper.renameTable("mining_record", yesterday);
        baseMapper.renameTable("personal_statistics", yesterday);
        baseMapper.renameTable("transaction", yesterday);
        miningRecordMapper.newTable();
        personalStatisticsMapper.newTable();
        transactionMapper.newTable();
        log.info("【分表任务】结束");
    }

}
