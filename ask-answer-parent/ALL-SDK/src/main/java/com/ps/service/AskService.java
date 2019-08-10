package com.ps.service;

import com.ps.domain.AnswerDo;
import com.ps.domain.QuestionDo;
import com.ps.domain.SorlQuestion;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/18 19:40
 */
public interface AskService {
    /**
     * 查询问题
     * @return
     */
    List<QuestionDo> queryIssue();

    /**
     * 查询问题明细
     * @return
     */
    List<QuestionDo> queryIssueDetail();

    /**
     * 发布问题
     * @param questionDo
     */
    void postQuestions(QuestionDo questionDo);

    /**
     * 回答问题
     * @param answerDo
     */
    void answerIssue(AnswerDo answerDo);

    /**
     * 采纳问题
     * @param answerDo
     * @throws InterruptedException
     * @throws IOException
     * @throws KeeperException
     */
    void adoptProblem(AnswerDo answerDo) throws InterruptedException, IOException, KeeperException;

    /**
     * 搜索引擎
     * @param sorlQuestion
     */
    List<SorlQuestion> searchEngine(SorlQuestion sorlQuestion) throws IOException, SolrServerException;

    /**
     * 采纳活动
     * @param value
     */
    void acceptActivity(Object value);
}
