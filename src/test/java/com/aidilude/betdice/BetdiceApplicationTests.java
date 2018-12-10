package com.aidilude.betdice;

import com.aidilude.betdice.cache.WithdrawCountCache;
import com.aidilude.betdice.component.ApplicationComponent;
import com.aidilude.betdice.mapper.BaseMapper;
import com.aidilude.betdice.mapper.MiningRecordMapper;
import com.aidilude.betdice.mapper.TransactionMapper;
import com.aidilude.betdice.mapper.TurnMapper;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.property.SystemProperties;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest
public class BetdiceApplicationTests {

	@Resource
	private TransactionMapper transactionMapper;

	@Resource
	private TurnMapper turnMapper;

	@Resource
	private MiningRecordMapper miningRecordMapper;

	@Resource
	private ApiProperties apiProperties;

	@Resource
	private SystemProperties systemProperties;

	@Resource
	private ApplicationComponent applicationComponent;

	@Resource
	private BaseMapper baseMapper;

	@Test
	public void contextLoads() throws InterruptedException {
		WithdrawCountCache.put("123456", 1);
		WithdrawCountCache.put("654321", 2);
		System.out.println(WithdrawCountCache.getCache());
		WithdrawCountCache.clear();
		System.out.println(WithdrawCountCache.getCache());
	}

}