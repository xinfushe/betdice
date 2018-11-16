package com.aidilude.betdice.task;

import com.aidilude.betdice.mapper.BaseMapper;
import com.aidilude.betdice.mapper.MiningRecordMapper;
import com.aidilude.betdice.mapper.PersonalStatisticsMapper;
import com.aidilude.betdice.mapper.TransactionMapper;
import com.aidilude.betdice.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;

@Configuration
@EnableScheduling
public class SubTableTask {

    private static final Logger logger = LoggerFactory.getLogger(SubTableTask.class);

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
        logger.info("【分表任务】开始执行...");
        String yesterday = StringUtils.gainLastRound().replace("-", "");
        baseMapper.renameTable("mining_record", yesterday);
        baseMapper.renameTable("personal_statistics", yesterday);
        baseMapper.renameTable("transaction", yesterday);
        miningRecordMapper.newTable();
        personalStatisticsMapper.newTable();
        transactionMapper.newTable();
        logger.info("【分表任务】结束");
    }

}
