package com.ps.serviceImpl;

import com.ps.config.BusinessException;
import com.ps.domain.*;
import com.ps.mapper.MarketMapper;
import com.ps.service.AskService;
import com.ps.service.MarketService;
import com.ps.service.VipService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.dubbo.config.annotation.Service;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.concurrent.ExecutorService;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 19:56
 */
@Service(version = "2.0.8")
public class MarketImpl implements MarketService {

    @Autowired
    private MarketMapper marketMapper;

    @Reference(version = "2.0.8")
    private VipService vipService;

    @Reference(version = "2.0.8")
    private AskService askService;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    private StringRedisTemplate template;

    @Autowired
    private ExecutorService executorService;

    /**
     * 创建问卷
     */
    @Override
    public void createQuestionnaire(List<ProblemDo> problemDo) {
        for (ProblemDo aDo : problemDo) {
            int questionnaire = marketMapper.createQuestionnaire(aDo);
            if (questionnaire != 1) {
                throw new BusinessException(500, "创建问卷失败");
            }
        }
    }

    /**
     * 提交答案
     */
    @Override
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void provideQuestionnaire(List<FindingsDo> findingsDo) {
        for (FindingsDo aDo : findingsDo) {
            int i = marketMapper.provideQuestionnaire(aDo);
            if (i != 1) {
                throw new BusinessException(500, "提交答案失败");
            }
        }
    }

    /**
     * 创建标题
     *
     * @param questionnaireDo
     */
    @Override
    public void createTitle(QuestionnaireDo questionnaireDo) {
        int title = marketMapper.createTitle(questionnaireDo);
        if (title != 1) {
            throw new BusinessException(500, "标题创建失败");
        }
    }

    /**
     * 查询出回答该套问卷的悬赏积分
     *
     * @param findingsDo
     */
    @Override
    public Integer queryIntegral(List<FindingsDo> findingsDo) {
        int id = 0;
        for (FindingsDo aDo : findingsDo) {
            id = aDo.getQuestionnaireId();
            break;
        }
        Integer integer = marketMapper.queryIntegral(id);
        if (integer == null) {
            throw new BusinessException(500, "查询悬赏积分失败");
        }
        return integer;
    }

    /**
     * 查询出问卷标题
     * @return
     */
    @Override
    public QuestionnaireDo queryTitle() {
        QuestionnaireDo questionnaireDo = marketMapper.queryTitle();
        if (questionnaireDo == null) {
            throw new BusinessException(500, "查询问卷标题失败");
        }
        return  questionnaireDo;
    }

    /**
     * 根据问卷标题id查询出问卷内容
     * @param questionnaireDo
     * @return
     */
    @Override
    public List<ProblemDo> queryContent(QuestionnaireDo questionnaireDo) {
        List<ProblemDo> problemDos = marketMapper.queryContent(questionnaireDo);
        if (problemDos == null) {
            throw new BusinessException(500, "查询问卷内容失败");
        }
        return problemDos;
    }

    /**
     * 抢购商品
     * @param id
     * @param commodityId
     */
    @Override
    //@Transactional(rollbackFor = {Error.class,RuntimeException.class, BusinessException.class})
    public void exchange(int id, int commodityId){

        System.out.println(id);
        System.out.println(commodityId);

        //查询用户信息
        VipDo vipDo = vipService.queryUser(id);
        System.out.println(vipDo);
        if (vipDo == null) {
            throw new BusinessException(500, "此用户不存在");
        }

        //查询商品信息
        CommodityDo commodityDo =marketMapper.queryCommodity(commodityId);
        if (commodityDo == null) {
            throw new BusinessException(500, "此商品不存在");
        }


        if (commodityDo.getNumber()==null){
            throw new BusinessException(500, "此商品库存不足");
        }

        Date date = new Date();
        //判断现在的时间是否在开始的时间之前
        boolean before = date.before(commodityDo.getBeginTime());

        //判断现在的时间是否在结束的时间之后
        boolean after = date.after(commodityDo.getEndTime());

        if( before && after){
            throw new BusinessException(500, "不在活动时间");
        }

        /**
         * 校验积分是否够用
         */
        if(vipDo.getIntegral()-commodityDo.getNeedIntegral()<0){
            throw new BusinessException(500, "积分不足");
        }

        /**
         * 查询是否兑换过
         */
        OrderFormDo orderFormDo = marketMapper.whetherExchange(id,commodityId);
        if(orderFormDo!=null){
            throw new BusinessException(500, "你已经兑换过,不能再次兑换");
        }
        /**
         * 扣积分
         */
        IntegralDo integralDo = vipService.subtractIntegral(id, commodityDo.getNeedIntegral());


        /**
         * 减库存
         */
        int i1 = marketMapper.subtractInventory(commodityId,commodityDo.getVersion());
        if(i1!=1){
            kafkaTemplate.send("rollback","register",String.valueOf(integralDo.getId()));
            throw new BusinessException(500, "减库存失败");
        }

        /**
         * 创建订单
         */
        int i = marketMapper.addOrderForm(id,commodityId);
        if(i!=1){
            throw new BusinessException(500, "创建订单失败");
        }
    }

    /**
     * 注册活动
     */
    @KafkaListener(topics="vip")
    public void registerActivity(ConsumerRecord<?, ?> cr){
        vipService.registerActivity(cr.value());
    };

    /**
     * 采纳活动
     */
    @KafkaListener(topics="ask")
    public void acceptActivity(ConsumerRecord<?, ?> cr){
        askService.acceptActivity(cr.value());
    }


    @PostConstruct
    public void consumeKill() throws InterruptedException {
        while (true){
            if(template.opsForList().size("active:exChange")==0){
                continue;
            }
            System.out.println("有任务了");
            Thread.sleep(10000);

            for (int i = 0; i <10 ; i++) {
                executorService.submit(()->{
                    String value =template.opsForList().rightPop("active:exChange");
                    System.out.println(value);
                    if (value==null){
                        return;
                    }
                    String id=value.split("_")[0];
                    String commodityId=value.split("_")[1];
                    System.out.println("id"+id);
                    System.out.println("commodityId"+commodityId);
                    exchange(Integer.valueOf(id),Integer.valueOf(commodityId));

                    template.opsForValue().set("active:kill:"+id,"3");
                });
            }
        }
    }


}
