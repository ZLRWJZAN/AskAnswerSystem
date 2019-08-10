package com.ps.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ps.domain.IntegralDo;
import com.ps.domain.ResultDo;
import com.ps.domain.VipDo;
import com.ps.service.VipService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/18 20:16
 */
@RestController
@RequestMapping("/vip")
public class VipController {

    @Reference(version = "2.0.8")
    private VipService vipService;

    ResultDo resultDo = new ResultDo();
    /**
     * 注册
     * @param vipDo
     */
    @PostMapping("/register")
    public ResultDo register(@RequestBody VipDo vipDo) {
        vipService.register(vipDo);

        resultDo.setCode(200);
        resultDo.setMessage("注册成功");
        return resultDo;
    }

    /**
     * 查询积分流水
     *
     * @param integralDo
     * @return
     */
    @GetMapping("/queryIntegralWater")
    public ResultDo queryIntegralWater(@RequestBody IntegralDo integralDo) {
        IntegralDo integralDo1 = vipService.queryIntegralWater(integralDo);

        resultDo.setBody(integralDo1);
        resultDo.setCode(200);
        resultDo.setMessage("查询成功");
        return resultDo;
    }

    /**
     * 积分排名前10
     */
    @GetMapping("/queryTop")
    public ResultDo<VipDo> queryTopTen(){
        Object o = vipService.queryTopTen();
        resultDo.setBody(o);
        resultDo.setCode(200);
        resultDo.setMessage("查询成功");
        return resultDo;
    }
}
