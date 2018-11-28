package com.aidilude.betdice.controller.api;

import com.aidilude.betdice.mapper.InviteRecordMapper;
import com.aidilude.betdice.mapper.MiningRecordMapper;
import com.aidilude.betdice.mapper.PersonalStatisticsMapper;
import com.aidilude.betdice.po.InviteRecord;
import com.aidilude.betdice.po.MiningRecord;
import com.aidilude.betdice.po.PersonalStatistics;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.util.Result;
import com.aidilude.betdice.util.ResultCode;
import com.aidilude.betdice.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personalStatistics")
//@CrossOrigin   //跨域配置只需要配置一次，如果配置了多次，则跨域不起作用。也就是说，如果nginx上设置了跨域，此处就不必再设置跨域了
public class PersonalStatisticsController {

    @Resource
    private ApiProperties apiProperties;

    @Resource
    private PersonalStatisticsMapper personalStatisticsMapper;

    @Resource
    private InviteRecordMapper inviteRecordMapper;

    @Resource
    private MiningRecordMapper miningRecordMapper;

    @GetMapping("/queryCurruent")
    @ApiOperation(value = "查询本轮统计", notes = "", response = Result.class)
    public Result queryCurruent(@ApiParam(name = "walletAddress", value = "钱包地址", required = true) @RequestParam String walletAddress,
                                @ApiParam(name = "currency", value = "币种", required = true) @RequestParam String currency){
        if(StringUtils.isEmpty(walletAddress))
            return Result.returnMsg(ResultCode.InvalidParam, "钱包地址为空");
        if(StringUtils.isEmpty(currency))
            return Result.returnMsg(ResultCode.InvalidParam, "币种为空");
        if(!apiProperties.getCurrencys().contains(currency))
            return Result.returnMsg(ResultCode.InvalidParam, "币种非法");
        String currentRound = StringUtils.gainCurrentRound();
        List<PersonalStatistics> personalStatisticsList = personalStatisticsMapper.selectByCondition(currentRound, walletAddress, currency);
        List<MiningRecord> miningRecords = miningRecordMapper.selectByCondition(currentRound, walletAddress, currency);
        InviteRecord inviteRecord = inviteRecordMapper.select(walletAddress);
        if((personalStatisticsList == null || personalStatisticsList.size() == 0) && (miningRecords == null || miningRecords.size() == 0) && inviteRecord == null)
            return Result.returnMsg(ResultCode.NotFind, "没有统计数据");
        Map<String, Object> result = new HashMap<>();
        result.put("personalStatisticsList", personalStatisticsList);
        result.put("miningRecords", miningRecords);
        result.put("inviteRecord", inviteRecord);
        return Result.returnSingleData(result);
    }

}