package com.aidilude.betdice.controller.api;

import com.aidilude.betdice.util.Result;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/base")
@CrossOrigin
public class BaseController {

    @GetMapping("/serverTime")
    @ApiOperation(value = "获取服务器时间戳", notes = "不需要secret和timestamp", response = Result.class)
    public Result serverTime(){
        Map<String, Object> result = new HashMap<>();
        result.put("serverTime", new Date().getTime());
        return Result.returnSingleData(result);
    }

}