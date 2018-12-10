package com.aidilude.betdice.controller.api;

import com.aidilude.betdice.cache.WithdrawCountCache;
import com.aidilude.betdice.dto.PledgeTransactionDto;
import com.aidilude.betdice.dto.WithdrawDto;
import com.aidilude.betdice.mapper.PledgeRecordMapper;
import com.aidilude.betdice.mapper.ReceivingAccountMapper;
import com.aidilude.betdice.mapper.WithdrawRecordMapper;
import com.aidilude.betdice.po.PledgeRecord;
import com.aidilude.betdice.po.ReceivingAccount;
import com.aidilude.betdice.po.WithdrawRecord;
import com.aidilude.betdice.property.SystemProperties;
import com.aidilude.betdice.service.PledgeService;
import com.aidilude.betdice.util.Result;
import com.aidilude.betdice.util.ResultCode;
import com.aidilude.betdice.util.StringUtils;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pledge")
@Slf4j
public class PledgeController {

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private PledgeRecordMapper pledgeRecordMapper;

    @Resource
    private ReceivingAccountMapper receivingAccountMapper;

    @Resource
    private WithdrawRecordMapper withdrawRecordMapper;

    @Resource
    private PledgeService pledgeService;

    @GetMapping("/queryReceivingAccount")
    @ApiOperation(value = "查询收钱账户", notes = "", response = Result.class)
    public Result queryReceivingAccount(@ApiParam(name = "plegorAccount", value = "质押人账户", required = true) @RequestParam String plegorAccount){
        if(StringUtil.isEmpty(plegorAccount))
            return Result.returnMsg(ResultCode.InvalidParam, "质押人账户为空");
        List<PledgeRecord> pledgeRecords = pledgeRecordMapper.selectByCondition(null, plegorAccount, null);
        Map<String, Object> result = new HashMap<>();
        if(pledgeRecords != null && pledgeRecords.size() > 0){
            result.put("receivingAccount", pledgeRecords.get(0).getReceivingAccount());
        }else{
            ReceivingAccount receivingAccount = receivingAccountMapper.selectMinPledgeCountAccount();
            if(receivingAccount == null){
                receivingAccount = pledgeService.newReceivingAccount();
                if(receivingAccount == null)
                    return Result.error("系统错误，请稍后再试");
                result.put("receivingAccount", receivingAccount.getWalletAddress());
            }else{
                if(receivingAccount.getPledgorCount() >= systemProperties.getMaxReceivingPledgorCount()){
                    receivingAccount = pledgeService.newReceivingAccount();
                    if(receivingAccount == null)
                        return Result.error("系统错误，请稍后再试");
                    result.put("receivingAccount", receivingAccount.getWalletAddress());
                }else{
                    result.put("receivingAccount", receivingAccount.getWalletAddress());
                }
            }
        }
        return Result.returnSingleData(result);
    }

    @PostMapping("/recordPledgeTransaction")
    @ApiOperation(value = "记录质押交易", notes = "", response = Result.class)
    public Result recordPledgeTransaction(@ApiParam(name = "pledgeTransactionDto", value = "交易记录", required = true) @Validated @RequestBody PledgeTransactionDto pledgeTransactionDto,
                                    BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return Result.returnMsg(ResultCode.InvalidParam, errorMsg);
        }
        List<PledgeRecord> pledgeRecords = pledgeRecordMapper.selectByCondition(pledgeTransactionDto.getId(), null, null);
        if(pledgeRecords != null && pledgeRecords.size() != 0)
            return Result.returnMsg(ResultCode.InvalidParam, "交易记录已存在");
        ReceivingAccount receivingAccount = receivingAccountMapper.selectByWalletAddress(pledgeTransactionDto.getReceivingAccount());
        if(receivingAccount == null)
            return Result.returnMsg(ResultCode.InvalidParam, "收钱账户不存在");
        try {
            pledgeService.recordPledgeTransaction(pledgeTransactionDto);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误，请稍后再试");
        }
        return Result.ok("记录成功");
    }

    @GetMapping("/queryWhetherCanWithdraw")
    @ApiOperation(value = "查询是否能提现", response = Result.class)
    public Result queryWhetherCanWithdraw(@ApiParam(name = "pledgorAccount", value = "质押人账户", required = true) @RequestParam String pledgorAccount){
        if(StringUtils.isEmpty(pledgorAccount))
            return Result.returnMsg(ResultCode.InvalidParam, "质押人账户为空");
        Map<String, Object> result = new HashMap<>();
        BigDecimal allWithdrawAmount = pledgeRecordMapper.selectAllWithdrawAmount(pledgorAccount);
        if(allWithdrawAmount.compareTo(new BigDecimal("0")) == 0) {
            result.put("whetherCanWithdraw", "NO");
            result.put("msg", "没有质押记录/没有提现余额/质押时间不足1天");
        }else{
            result.put("whetherCanWithdraw", "YES");
            result.put("allWithdrawAmount", allWithdrawAmount);
        }
        return Result.returnSingleData(result);
    }

    @PostMapping("/withdraw")
    @ApiOperation(value = "提现", response = Result.class)
    public Result withdraw(@ApiParam(name = "withdrawDto", value = "提现封装类", required = true) @Validated @RequestBody WithdrawDto withdrawDto,
                           BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return Result.returnMsg(ResultCode.InvalidParam, errorMsg);
        }
        Integer todayWithdrawCount = WithdrawCountCache.get(withdrawDto.getPledgorAccount());
        if(todayWithdrawCount != null && todayWithdrawCount >= 2){
            return Result.returnMsg(ResultCode.ExceedWithdrawCount, "今日提现次数用完");
        }
        List<WithdrawRecord> withdrawRecords = withdrawRecordMapper.selectByCondition(null, withdrawDto.getTransactionId(), null);
        if(withdrawRecords != null && withdrawRecords.size() != 0)
            return Result.returnMsg(ResultCode.InvalidParam, "交易ID已存在");
        boolean isWithdrawSuccess = false;
        try {
            isWithdrawSuccess = pledgeService.withdraw(withdrawDto);
        } catch (Exception e) {
            log.error("提现异常", e);
            return Result.error("提现异常，请稍后再试");
        }
        Map<String, Object> result = new HashMap<>();
        if(!isWithdrawSuccess){
            result.put("isWithdrawSuccess", "NO");
            result.put("msg", "没有质押记录/没有提现余额/质押时间不足1天/提现余额不足");
        }else{
            result.put("isWithdrawSuccess", "YES");
            result.put("msg", "提现成功，请查看账户");
        }
        return Result.returnSingleData(result);
    }

    @GetMapping("/bonusTransfer")
    @ApiOperation(value = "质押分红转账", response = Result.class)
    public Result bonusTransfer(){
        pledgeService.pledgeBonusTransfer();
        return Result.ok("执行");
    }


}