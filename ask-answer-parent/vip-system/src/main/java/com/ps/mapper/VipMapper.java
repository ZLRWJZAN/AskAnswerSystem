package com.ps.mapper;

import com.ps.domain.IntegralDo;
import com.ps.domain.VipDo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/18 21:27
 */
@Mapper
@Repository
public interface VipMapper {
    /**
     * 注册
     * @param vipDo
     * @return
     */
    int register(VipDo vipDo);

    /**
     * 注册成功后,在流水表增加积分记录
     * @param vipDo
     * @return
     */
    int addRegisterIntegral(VipDo vipDo);

    /**
     * 查询积分流水
     * @param integralDo
     * @return
     */
    IntegralDo queryIntegralWater(IntegralDo integralDo);

    /**
     * 回答问卷增加积分
     * @param memberId
     * @param integrals
     * @return
     */
    int addIntegral(Integer memberId, Integer integrals);

    /**
     * 增加流水记录
     * @param memberId
     * @param integrals
     * @return
     */
    int addFlowRecord(Integer memberId, Integer integrals);

    /**
     * 将邀请链接加入数据库
     * @param vipDo
     * @return
     */
    int addInviteLink(VipDo vipDo);

    /**
     * 给邀请人加分
     * @param vipDo
     * @return
     */
    int addInvitePoints(VipDo vipDo);

    /**
     * 生成邀请流水记录
     * @param vipDo
     * @return
     */
    int createInvitedRecordWater(VipDo vipDo);

    /**
     * 注册活动
     * @param value
     */
    void registerActivity(Object value);

    /**
     * 注册活动增加流水记录
     * @param value
     */
    void registerActivityWater(Object value);

    /**
     * 查询积分排行榜前十
     * @return
     */
    List<VipDo> queryTopTen();

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    VipDo queryUser(int id);

    /**
     * 扣积分
     * @param id
     * @param needIntegral
     */
    int subtractIntegral(Integer id, Integer needIntegral);

    /**
     * 增加秒杀积分流水
     * @param integralDo
     */
    void addIntegralWater(IntegralDo integralDo);

    /**
     * 退还用户积分
     * @param userId
     * @param score
     */
    void retreatIntegral(Integer userId, Integer score);

    /**
     * 查出用户扣除的积分
     * @param value
     * @return
     */
    IntegralDo queryUserIntegral(Object value);

    /**
     * 增加退还记录
     * @param userId
     * @param score
     */
    void addReturnWater(Integer userId, Integer score);
}
