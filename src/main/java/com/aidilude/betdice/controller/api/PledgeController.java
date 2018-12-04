package com.aidilude.betdice.controller.api;

import com.aidilude.betdice.dto.PledgeTransaction;
import com.aidilude.betdice.mapper.PledgeRecordMapper;
import com.aidilude.betdice.mapper.ReceivingAccountMapper;
import com.aidilude.betdice.po.PledgeRecord;
import com.aidilude.betdice.po.ReceivingAccount;
import com.aidilude.betdice.property.SystemProperties;
import com.aidilude.betdice.service.PledgeService;
import com.aidilude.betdice.util.Result;
import com.aidilude.betdice.util.ResultCode;
import com.github.pagehelper.util.StringUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pledge")
public class PledgeController {

    @Resource
    private SystemProperties systemProperties;

    @Resource
    private PledgeRecordMapper pledgeRecordMapper;

    @Resource
    private ReceivingAccountMapper receivingAccountMapper;

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
    public Result recordPledgeTransaction(@ApiParam(name = "pledgeTransaction", value = "交易记录", required = true) @Validated @RequestBody PledgeTransaction pledgeTransaction,
                                    BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            String errorMsg = bindingResult.getFieldError().getDefaultMessage();
            return Result.returnMsg(ResultCode.InvalidParam, errorMsg);
        }
        List<PledgeRecord> pledgeRecords = pledgeRecordMapper.selectByCondition(pledgeTransaction.getId(), null, null);
        if(pledgeRecords != null && pledgeRecords.size() != 0)
            return Result.returnMsg(ResultCode.InvalidParam, "交易记录已存在");
        ReceivingAccount receivingAccount = receivingAccountMapper.selectByWalletAddress(pledgeTransaction.getReceivingAccount());
        if(receivingAccount == null)
            return Result.returnMsg(ResultCode.InvalidParam, "收钱账户不存在");
        try {
            pledgeService.recordPledgeTransaction(pledgeTransaction);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("系统错误，请稍后再试");
        }
        return Result.ok("记录成功");
    }

}