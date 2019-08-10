package com.ps.serviceImpl;

import com.ps.config.BusinessException;
import com.ps.domain.FindingsDo;
import com.ps.domain.IntegralDo;
import com.ps.domain.VipDo;
import com.ps.mapper.VipMapper;
import com.ps.service.VipService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;
import java.time.Duration;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/18 20:17
 */
@Service(version = "2.0.8")
public class VipImpl implements VipService {

    @Autowired
    private VipMapper vipMapper;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;


    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    @Override
    /**
     * 事务
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void register(VipDo vipDo) {
        /**
         * 注册
         */
        int register = vipMapper.register(vipDo);
        if (register != 1) {
            throw new BusinessException(500, "注册失败");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(2019,6,23);
        long millis = calendar.getTimeInMillis();
        calendar.set(2019,6,25);
        long millis1=calendar.getTimeInMillis();
        if(System.currentTimeMillis()>=millis && System.currentTimeMillis()<=millis1){
            /**
             * 消息队列
             */
            kafkaTemplate.send("vip","register",String.valueOf(vipDo.getId()));

        }
        vipDo.setInviteLink("http://localhost:9090/vip/register?" + vipDo.getId());
        /**
         * 将邀请链接加入数据库
         */
        int i = vipMapper.addInviteLink(vipDo);
        if (i != 1) {
            throw new BusinessException(500, "加入链接失败");
        }

        /**
         * 注册成功的时,在积分流水表加记录
         */
        int i1 = vipMapper.addRegisterIntegral(vipDo);
        if (i1 != 1) {
            throw new BusinessException(500, "增加记录失败");
        }

        //判断是否有邀请人id
        if (vipDo.getInviterId() == null) {

            return;

        } else {
            /**
             * 给邀请人加分
             */
            int i2 = vipMapper.addInvitePoints(vipDo);
            if (i2 != 1) {
                throw new BusinessException(500, "加分失败");
            }

            /**
             * 生成邀请流水记录
             */
            int invitedRecordWater = vipMapper.createInvitedRecordWater(vipDo);
            if (invitedRecordWater != 1) {
                throw new BusinessException(500, "生成记录失败");
            }

            return;

        }

    }

    /**
     * 查询积分流水
     */
    @Override
    public IntegralDo queryIntegralWater(IntegralDo integralDo) {
        IntegralDo integralDo1 = vipMapper.queryIntegralWater(integralDo);
        if (integralDo1 == null) {
            throw new BusinessException(500, "查询积分失败");
        }
        return integralDo1;
    }

    /**
     * 回答问卷增加积分
     *
     * @param findingsDo
     * @param integral
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void addIntegral(List<FindingsDo> findingsDo, int integral) {
        int id = 0;
        for (FindingsDo aDo : findingsDo) {
            id = aDo.getMemberId();
            break;
        }
        /**
         * 回答问卷增加积分
         */
        int i = vipMapper.addIntegral(id, integral);
        if (i != 1) {
            throw new BusinessException(500, "增加积分失败");
        }
        /**
         * 增加流水记录
         */
        int i1 = vipMapper.addFlowRecord(id, integral);
        if (i1 != 1) {
            throw new BusinessException(500, "增加流水记录失败");
        }

    }

    /**
     * 注册活动
     * @param value
     */
    @Override
    public void registerActivity(Object value) {
        vipMapper.registerActivity(value);
        vipMapper.registerActivityWater(value);
    }

    /**
     * 查询前十
     * @return
     */
    @Override
    public Object queryTopTen() {
        Object top = redisTemplate.opsForValue().get("top");
        if(top==null){
            System.out.println("进入数据库");
            List<VipDo> vipDo = vipMapper.queryTopTen();
            redisTemplate.opsForValue().set("top", vipDo, 20, TimeUnit.SECONDS);
        }else{
            System.out.println("缓存");
        }
        return redisTemplate.opsForValue().get("top");
    }

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    @Override
    public VipDo queryUser(int id) {
        System.out.println(id);
        return vipMapper.queryUser(id);
    }

    /**
     * 扣积分
     * @param id
     * @param needIntegral
     */
    @Override
    public IntegralDo subtractIntegral(Integer id, Integer needIntegral) {
        int i = vipMapper.subtractIntegral(id, needIntegral);
        if(i!=1){
            throw new BusinessException(500, "扣积分失败");
        }

        /**
         * 增加秒杀积分流水
         */
        IntegralDo integralDo = new IntegralDo();
        integralDo.setId(id);
        integralDo.setScore(needIntegral);
        vipMapper.addIntegralWater(integralDo);
        return integralDo;
    }

    /**
     * 退还用户积分
     */
    @KafkaListener(topics="rollback")
    public void retreatIntegral(ConsumerRecord<?, ?> cr){
        //根据id查询出被扣除的积分与用户
        IntegralDo integralDo = vipMapper.queryUserIntegral(cr.value());

        //退还用户积分
        vipMapper.retreatIntegral(integralDo.getUserId(),integralDo.getScore());

        //增加退还记录
        vipMapper.addReturnWater(integralDo.getUserId(),integralDo.getScore());
    };
}
