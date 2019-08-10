package com.ps.serviceImpl;

import com.ps.config.BusinessException;
import com.ps.config.Zklock;
import com.ps.domain.AnswerDo;
import com.ps.domain.QuestionDo;
import com.ps.domain.SorlQuestion;
import com.ps.mapper.AskMapper;
import com.ps.service.AskService;
import org.apache.dubbo.config.annotation.Service;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;
import org.apache.zookeeper.KeeperException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 11:07
 */
@Service(version = "2.0.8")
public class AskImpl implements AskService {

    private Zklock lock;

    @Autowired
    private AskMapper askMapper;

    @Autowired
    private KafkaTemplate<String,String> kafkaTemplate;

    /**
     * 查询问题
     */
    @Override
    public List<QuestionDo> queryIssue() {
        List<QuestionDo> questionDos = askMapper.queryIssueAndDetail();
        if (questionDos == null) {
            throw new BusinessException(500, "查询失败");
        }
        return questionDos;
    }

    /**
     * 查询问题明细
     */
    @Override
    public List<QuestionDo> queryIssueDetail() {
        List<QuestionDo> questionDos = askMapper.queryIssueAndDetail();
        if (questionDos == null) {
            throw new BusinessException(500, "查询失败");
        }
        return questionDos;
    }

    /**
     * 发布问题
     */
    @Override
    public void postQuestions(QuestionDo questionDo) {
        int i = askMapper.postQuestions(questionDo);
        if (i != 1) {
            throw new BusinessException(500, "发布失败");
        }
    }

    /**
     * 回答问题
     *
     * @param answerDo
     */
    @Override
    public void answerIssue(AnswerDo answerDo) {
        int i = askMapper.answerIssue(answerDo);
        if (i != 1) {
            throw new BusinessException(500, "回答失败");
        }
    }

    /**
     * 采纳问题
     */
    @Override
    /**
     * 事务
     */
    @Transactional(rollbackFor = {RuntimeException.class, Error.class})
    public void adoptProblem(AnswerDo answerDo) throws InterruptedException, IOException, KeeperException {
        /**
         * 采纳问题
         */
        int i = askMapper.adoptProblem(answerDo);
        if (i != 1) {
            throw new BusinessException(500, "采纳失败");
        }

        /**
         * 采纳之后修改问题表中的状态为  解决
         */
        int i1 = askMapper.updateFlag(answerDo);
        if (i1 != 1) {
            throw new BusinessException(500, "修改状态失败");
        }

        /**
         * 根据问题id查询出提问者id与悬赏积分
         */
        QuestionDo questionDo = askMapper.queryAskIdAndReward(answerDo);
        if (questionDo == null) {
            throw new BusinessException(500, "查询失败");
        }

        /**
         * 给提问者减积分
         */
        int i3 = askMapper.subtractReward(questionDo);
        if (i3 != 1) {
            throw new BusinessException(500, "扣除积分失败");
        }


        /**
         * 增加发布问题的积分流水记录
         */
        int i4 = askMapper.addIssueRecord(questionDo);
        if (i4 != 1) {
            throw new BusinessException(500, "增加流水记录失败");
        }


        /**
         *  给回答者加积分
         */
        int i2 = askMapper.addReward(answerDo.getReplierId(), questionDo.getReward());
        if (i2 != 1) {
            throw new BusinessException(500, "增加积分失败");
        }

        /**
         * 增加回答问题的积分流水记录
         */
        int i5 = askMapper.addAnswerRecord(answerDo.getReplierId(), questionDo.getReward());
        if (i5 != 1) {
            throw new BusinessException(500, "增加流水记录失败");
        }

        /**
         * 奖励时间
         */
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
            kafkaTemplate.send("ask","register",String.valueOf(answerDo.getReplierId()));
        }
    }

    /**
     * 搜索引擎
     * @param sorlQuestion
     */
    @Override
    public List<SorlQuestion> searchEngine(SorlQuestion sorlQuestion) throws IOException, SolrServerException {
        HttpSolrClient build = new HttpSolrClient.Builder("http://192.168.3.210:8983/solr").build();
        Map<String, String> map = new HashMap<>();

        map.put("q","all:"+sorlQuestion.getContext());

        QueryResponse response=build.query("qa",new MapSolrParams(map));
        List<SorlQuestion> beans = response.getBeans(SorlQuestion.class);
        System.out.println(beans);
        return beans;
    }

    /**
     * 采纳活动
     * @param value
     */
    @Override
    public void acceptActivity(Object value) {
        askMapper.acceptActivity(value);
        askMapper.acceptActivityWater(value);
    }
}
