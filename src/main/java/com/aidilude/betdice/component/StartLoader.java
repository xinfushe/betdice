package com.aidilude.betdice.component;

import com.aidilude.betdice.cache.PledgePoolCache;
import com.aidilude.betdice.cache.WithdrawCountCache;
import com.aidilude.betdice.mapper.PledgeRecordMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;

@Component
@Order(value=1)
public class StartLoader implements ApplicationRunner {

    @Resource
    private PledgeRecordMapper pledgeRecordMapper;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("#######################################系统初始化#######################################");
        loadSysParams();
        System.out.println("#######################################初始化完成#######################################");
    }

    public void loadSysParams(){
        System.out.println("【系统参数】加载中...");
        WithdrawCountCache.init();
        loadTotalWithdrawableAmount();
        System.out.println("【系统参数】加载完成...");
    }

    public void loadTotalWithdrawableAmount(){
        System.out.println("**********加载质押池可提现总量**********");
        PledgePoolCache.setTotalWithdrawableAmount(pledgeRecordMapper.selectAllWithdrawAmount(null));
        System.out.println("**********加载成功**********");
    }

}