package com.aidilude.betdice.task;

import com.aidilude.betdice.cache.PledgePoolCache;
import com.aidilude.betdice.mapper.PledgeRecordMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Configuration
@EnableScheduling
@Slf4j
public class LoadPledgePoolDataTask {

    @Resource
    private PledgeRecordMapper pledgeRecordMapper;

    @Scheduled(cron = "0 */10 * * * ?")
    public void run(){
        log.info("【加载质押池数据任务】开始执行");

        PledgePoolCache.setTotalWithdrawableAmount(pledgeRecordMapper.selectAllWithdrawAmount(null));

        log.info("【加载质押池数据任务】执行结束");
    }

}