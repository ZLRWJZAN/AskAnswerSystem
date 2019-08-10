package com.ps.service;

import com.ps.domain.FindingsDo;
import com.ps.domain.IntegralDo;
import com.ps.domain.VipDo;

import java.util.List;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/18 19:40
 */

public interface VipService {
    /**
     * 注册
     * @param vipDo
     */
    void register(VipDo vipDo);

    /**
     * 查询积分流水
     * @param integralDo
     * @return
     */
    IntegralDo queryIntegralWater(IntegralDo integralDo);

    /**
     * 回答问卷增加积分
     * @param findingsDo
     * @param integral
     */
    void addIntegral(List<FindingsDo> findingsDo,int integral);

    /**
     * 注册活动
     * @param value
     */
    void registerActivity(Object value);

    /**
     * 查询排行榜前十
     * @return
     */
    Object queryTopTen();

    /**
     * 查询用户信息
     * @param id
     */
     VipDo queryUser(int id);

    /**
     * 扣积分
     * @param id
     * @param needIntegral
     */
    IntegralDo subtractIntegral(Integer id, Integer needIntegral);
}
