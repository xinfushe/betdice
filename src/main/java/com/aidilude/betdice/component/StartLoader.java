package com.aidilude.betdice.component;

import com.aidilude.betdice.cache.WithdrawCountCache;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(value=1)
public class StartLoader implements ApplicationRunner {

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("#######################################系统初始化#######################################");
        loadSysParams();
        System.out.println("#######################################初始化完成#######################################");
    }

    public void loadSysParams(){
        System.out.println("【系统参数】加载中...");
        WithdrawCountCache.init();
        System.out.println("【系统参数】加载完成...");
    }

}