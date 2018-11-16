package com.aidilude.betdice.controller.api;

import com.aidilude.betdice.mapper.TransactionMapper;
import com.aidilude.betdice.mapper.TurnMapper;
import com.aidilude.betdice.po.Transaction;
import com.aidilude.betdice.po.Turn;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.property.SystemProperties;
import com.aidilude.betdice.service.TransactionService;
import com.aidilude.betdice.util.Result;
import com.aidilude.betdice.util.ResultCode;
import com.aidilude.betdice.util.StringUtils;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transaction")
//@CrossOrigin
public class TransactionController {

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private ApiProperties apiProperties;

    @Resource
    private TransactionMapper transactionMapper;

    @Resource
    private TurnMapper turnMapper;

    @Resource
    private TransactionService transactionService;

    @PostMapping("/recordTransaction")
    @ApiOperation(value = "记录一条交易记录", notes = "", response = Result.class)
    public Result recordTransaction(@ApiParam(name = "transaction", value = "交易记录", required = true) @RequestBody Transaction transaction){
        if(transaction == null)
            return Result.returnMsg(ResultCode.InvalidParam, "交易记录为空");
        if(!StringUtils.isRound(transaction.getRound()))
            return Result.returnMsg(ResultCode.InvalidParam, "轮次非法");
        if(!apiProperties.getCurrencys().contains(transaction.getCurrency()))
            return Result.returnMsg(ResultCode.InvalidParam, "币种非法");
        if(turnMapper.selectByPrimaryKey(transaction.getRound(), transaction.getCurrency()) == null)
            return Result.returnMsg(ResultCode.NotFind, "本轮游戏尚未开始");
        if(StringUtils.isEmpty(transaction.getOwner()))
            return Result.returnMsg(ResultCode.InvalidParam, "钱包地址为空");
        if(StringUtils.isEmpty(transaction.getTid()))
            return Result.returnMsg(ResultCode.InvalidParam, "交易ID为空");
        List<Transaction> transactions = transactionMapper.selectByCondition(transaction.getTid(), null, null, null, null);
        if(transactions != null && transactions.size() > 0)
            return Result.returnMsg(ResultCode.InvalidParam, "交易ID已存在");
        try {
            transactionService.newTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("记录失败");
        }
        return Result.ok("记录成功");
    }

    @GetMapping("/queryTransaction")
    @ApiOperation(value = "查询交易记录", notes = "筛选查询，结果按交易时间戳降序排列，每页20条", response = Result.class)
    public Result queryTransaction(@ApiParam(name = "tid", value = "交易ID", required = false) @RequestParam(required = false) String tid,
                                   @ApiParam(name = "round", value = "轮次", required = false) @RequestParam(required = false) String round,
                                   @ApiParam(name = "owner", value = "交易人钱包地址", required = false) @RequestParam(required = false) String owner,
                                   @ApiParam(name = "currency", value = "币种", required = false) @RequestParam(required = false) String currency,
                                   @ApiParam(name = "pageCount", value = "页数（从第一页开始）", required = true) @RequestParam Integer pageCount){
        if(pageCount == null || pageCount < 1)
            return Result.returnMsg(ResultCode.InvalidParam, "页数不合法");
        Page<?> page = PageHelper.startPage(pageCount, systemProperties.getPageSize());
        List<Transaction> transactions = transactionMapper.selectByCondition(tid, round, owner, currency, null);
        if(transactions == null)
            return Result.error("查询错误，请重试");
        return Result.returnPagingData(transactions, Integer.valueOf(String.valueOf(page.getTotal())), systemProperties.getPageSize(), pageCount);
    }

    @GetMapping("/checkIsWin")
    @ApiOperation(value = "检查交易是否中奖", notes = "", response = Result.class)
    public Result checkIsWin(@ApiParam(name = "tid", value = "交易ID", required = true) @RequestParam String tid){
        if(StringUtils.isEmpty(tid))
            return Result.returnMsg(ResultCode.InvalidParam, "交易ID为空");
        Map<String, Object> result = new HashMap<>();
        List<Transaction> transactions = transactionMapper.selectByCondition(tid, null, null, null, null);
        if(transactions == null || transactions.size() == 0)
            return Result.returnMsg(ResultCode.NotFind, "交易记录不存在");
        result.put("transaction", transactions.get(0));
        if(transactions.get(0).getAmountWin().equals("0"))
            result.put("isWin", "NO");
        else
            result.put("isWin", "YES");
        return Result.returnSingleData(result);
    }

}