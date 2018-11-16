package com.aidilude.betdice.controller.api;

import com.aidilude.betdice.mapper.RankMapper;
import com.aidilude.betdice.po.Rank;
import com.aidilude.betdice.property.ApiProperties;
import com.aidilude.betdice.util.Result;
import com.aidilude.betdice.util.ResultCode;
import com.aidilude.betdice.util.StringUtils;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/api/rank")
//@CrossOrigin
public class RankController {

    @Resource
    private ApiProperties apiProperties;

    @Resource
    private RankMapper rankMapper;

    @GetMapping("/queryCurrentRank")
    @ApiOperation(value = "查询当前排行", notes = "", response = Result.class)
    public Result queryCurrentRank(){
        String currentRound = StringUtils.gainCurrentRound();
        List<Rank> ranks = rankMapper.selectRanks(currentRound, apiProperties.getRankWinCurrency(), apiProperties.getRankMiningCurrency(), apiProperties.getRankOffset());
        if(ranks == null || ranks.size() == 0)
            return Result.returnMsg(ResultCode.NotFind, "没有排行数据");
        return Result.returnSingleData(ranks);
    }

}