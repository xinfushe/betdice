package com.aidilude.betdice.task;

import com.aidilude.betdice.mapper.RankMapper;
import com.aidilude.betdice.po.Rank;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.util.StringUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableScheduling
@Slf4j
public class CreateRankTask {

    @Resource
    private ApiProperties apiProperties;

    @Resource
    private RankMapper rankMapper;

    @Scheduled(cron = "0 59 23 * * ?")   //每日23点59分排行统计截止
    public void run(){
        String todayRound = StringUtils.gainCurrentRound();
        log.info("【轮次：" + todayRound + "生成排行榜】任务开始...");
        String[] temp = apiProperties.getRankWinCurrency().split("\\|");
        List<String> rankCurrencys = Arrays.asList(temp);
        rankCurrencys.forEach(currency -> {
            List<Rank> yesterdayRanks = rankMapper.selectRanksWithAllColumn(todayRound, currency, apiProperties.getRankMiningCurrency(), apiProperties.getRankOffset());
            yesterdayRanks.forEach(rank -> {
                Integer count = rankMapper.insert(rank);
                if(count == null || count == 0) {
                    log.error("【轮次：" + todayRound + "生成排行榜】失败，当前记录：【" + JSON.toJSONString(rank) + "】");
                }
            });
        });
        log.info("【轮次：" + todayRound + "生成排行榜】任务结束...");
    }

}