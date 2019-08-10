package com.ps.controller;

import com.ps.domain.FindingsDo;
import com.ps.domain.ProblemDo;
import com.ps.domain.QuestionnaireDo;
import com.ps.domain.ResultDo;
import com.ps.service.MarketService;
import com.ps.service.VipService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;


import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 19:55
 */
@RestController
@RequestMapping("/market")
public class MarketController {
    @Reference(version = "2.0.8")
    private MarketService marketService;

    @Reference(version = "2.0.8")
    private VipService vipService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    ResultDo resultDo = new ResultDo();

    @Value("${redis.redisSize}")
    private Integer redisSize;
    /**
     * 创建问卷
     *
     * @return
     */
    @PostMapping("/createQuestionnaire")
    public ResultDo createQuestionnaire(@RequestBody List<ProblemDo> problemDo) {
        marketService.createQuestionnaire(problemDo);

        resultDo.setCode(200);
        resultDo.setMessage("创建成功");
        return resultDo;
    }

    /**
     * 创建标题
     *
     * @param questionnaireDo
     * @return
     */
    @PostMapping("/createTitle")
    public ResultDo createTitle(@RequestBody QuestionnaireDo questionnaireDo) {
        marketService.createTitle(questionnaireDo);

        resultDo.setCode(200);
        resultDo.setMessage("创建成功");
        return resultDo;
    }

    /**
     * 提交答案
     *
     * @param findingsDo
     * @return
     */
    @PostMapping("/provideQuestionnaire")
    public ResultDo provideQuestionnaire(@RequestBody List<FindingsDo> findingsDo) {
        /**
         * 提交答案
         */
        marketService.provideQuestionnaire(findingsDo);


        /**
         * 查询出回答该套问卷的悬赏积分
         */
        Integer i = marketService.queryIntegral(findingsDo);

        /**
         * 回答问卷增加积分
         */
        vipService.addIntegral(findingsDo, i);

        resultDo.setCode(200);
        resultDo.setMessage("创建成功");
        return resultDo;
    }

    /**
     * 查询出问卷标题
     */
    @GetMapping("/queryTitle")
    public ResultDo queryTitle() {

        resultDo.setBody(marketService.queryTitle());
        resultDo.setCode(200);
        resultDo.setMessage("查询成功");
        return resultDo;
    }

    /**
     * 根据问卷标题id查询出问卷内容
     *
     * @param questionnaireDo
     * @return
     */
    @GetMapping("/queryContent")
    public ResultDo queryContent(@RequestBody QuestionnaireDo questionnaireDo) {

        resultDo.setBody(marketService.queryContent(questionnaireDo));
        resultDo.setCode(200);
        resultDo.setMessage("查询成功");
        return resultDo;
    }

    /**
     * 抢购商品
     * @param id
     * @param commodityId
     * @return
     */
    @PostMapping("/exchange")
    public ResultDo exchange(@RequestParam int id,@RequestParam int commodityId){
        Object status = redisTemplate.opsForValue().get("active:kill:" + id);

        if(status==null){
            Boolean res=addQueue(id,commodityId);
            if(res){
                redisTemplate.opsForValue().set("active:kill:"+id,"2",1,TimeUnit.DAYS);
                status="2";
            }else{
                redisTemplate.opsForValue().set("active:kill:"+id,"1",1,TimeUnit.SECONDS);
                status="1";
            }
        }

        if("2".equals(status)){
            resultDo.setMessage("排队成功");
        }
        /**
         * 可限流
         */
        if("1".equals(status)){
            resultDo.setMessage("当前人数过多");
        }


        return  resultDo;

        /**
         * 限流
         */
        /*if(redisTemplate.hasKey("key:"+id)) {
            resultDo.setCode(200);
            resultDo.setMessage("请求成功");
            return resultDo;
        }
        redisTemplate.opsForValue().set("key:"+id,id, 5, TimeUnit.SECONDS);
        marketService.exchange(id,commodityId);
        resultDo.setCode(200);
        resultDo.setMessage("抢购成功");
        return resultDo;*/
    }

    private Boolean addQueue(int id,int commodityId) {
        Long size = redisTemplate.opsForList().size("active:exChange");
        System.out.println(size+"-=-=-=-=-=-=-=-=-=");
        if(size>=redisSize){
            return false;
        }

        redisTemplate.opsForList().leftPush("active:exChange",id+"_"+commodityId);
        return true;
    }
}
