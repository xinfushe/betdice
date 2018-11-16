package com.aidilude.betdice.controller.api;

import com.aidilude.betdice.mapper.TransactionMapper;
import com.aidilude.betdice.mapper.TurnMapper;
import com.aidilude.betdice.po.Turn;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.util.HttpUtils;
import com.aidilude.betdice.util.Result;
import com.aidilude.betdice.util.ResultCode;
import com.aidilude.betdice.util.StringUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@RestController
@RequestMapping("/api/turn")
//@CrossOrigin
public class TurnController {

    @Resource
    private ApiProperties apiProperties;

    @Resource
    private TurnMapper turnMapper;

    @GetMapping("/queryCurrentTurn")
    @ApiOperation(value = "查询本轮", notes = "", response = Result.class)
    public synchronized Result queryCurrentTurn(@ApiParam(name = "currency", value = "本轮币种", required = true) @RequestParam String currency){
        if(StringUtils.isEmpty(currency))
            return Result.returnMsg(ResultCode.InvalidParam, "币种为空");
        if(!apiProperties.getCurrencys().contains(currency))
            return Result.returnMsg(ResultCode.InvalidParam, "币种非法");
        String currentRound = StringUtils.gainCurrentRound();
        Turn turn = turnMapper.selectByPrimaryKey(currentRound, currency);
        if(turn != null){
            turn.setOfficialWalletSecret(null);
            turn.setTotalWinAmount(null);
            turn.setTotalWinCount(null);
            turn.setPartakeCustomerCount(null);
            turn.setTotalBetAmount(null);
            turn.setTotalBetCount(null);
            turn.setJackpotAmount(null);
            return Result.returnSingleData(turn);
        }
        String url = apiProperties.getChainRequestProtocol() + apiProperties.getChainIP() + ":" + apiProperties.getChainPort() + apiProperties.getPreffix() + apiProperties.getNewWalletAddress();
        String newWalletAddress = null;
        try {
            newWalletAddress = HttpUtils.doGet(url, null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("网络异常，请稍后再试");
        }
        if(StringUtils.isEmpty(newWalletAddress))
            return Result.error("网络异常，请稍后再试");
        JSONObject jsonObject = JSONObject.parseObject(newWalletAddress);
        if(jsonObject.getBooleanValue("success") != true)
            return Result.error("网络请求异常，请稍后再试");
        turn = new Turn();
        turn.setRound(currentRound);
        turn.setCurrency(currency);
        turn.setOfficialWalletAddress(jsonObject.getString("address"));
        turn.setOfficialWalletSecret(jsonObject.getString("secret"));
        Integer count = turnMapper.insert(turn);
        if(count == null || count == 0)
            return Result.error("创建轮次异常，请稍后再试");
        turn.setOfficialWalletSecret(null);
        return Result.returnSingleData(turn);
    }

    @GetMapping("/queryTurnStatistics")
    @ApiOperation(value = "查询轮次统计数据", notes = "", response = Result.class)
    public Result queryTurnStatistics(@ApiParam(name = "round", value = "轮次", required = true) @RequestParam String round,
                                      @ApiParam(name = "currency", value = "币种", required = true) @RequestParam String currency){
        if(!StringUtils.isRound(round))
            return Result.returnMsg(ResultCode.InvalidParam, "轮次非法");
        if(StringUtils.isEmpty(currency))
            return Result.returnMsg(ResultCode.InvalidParam, "币种为空");
        if(!apiProperties.getCurrencys().contains(currency))
            return Result.returnMsg(ResultCode.InvalidParam, "币种非法");
        Turn turn = turnMapper.selectByPrimaryKey(round, currency);
        if(turn == null)
            return Result.returnMsg(ResultCode.NotFind, "轮次不存在");
        turn.setOfficialWalletSecret(null);
        return Result.returnSingleData(turn);
    }

}