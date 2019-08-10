package com.ps.controller;

import com.ps.domain.AnswerDo;
import com.ps.domain.QuestionDo;
import com.ps.domain.ResultDo;
import com.ps.domain.SorlQuestion;
import com.ps.service.AskService;
import org.apache.dubbo.config.annotation.Reference;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.zookeeper.KeeperException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 10:55
 */
@RestController
@RequestMapping("/ask")
public class AskController {

    @Reference(version = "2.0.8")
    private AskService askService;

    ResultDo resultDo = new ResultDo();

    /**
     * 发布问题
     *
     * @param questionDo
     */
    @PostMapping("/postQuestions")
    public ResultDo postQuestions(@RequestBody QuestionDo questionDo) {
        askService.postQuestions(questionDo);

        resultDo.setCode(200);
        resultDo.setMessage("发布成功");
        return resultDo;
    }

    /**
     * 回答问题
     */
    @PostMapping("/answerIssue")
    public ResultDo answerIssue(@RequestBody AnswerDo answerDo) {
        askService.answerIssue(answerDo);

        resultDo.setCode(200);
        resultDo.setMessage("完成回答");
        return resultDo;
    }

    /**
     * 查询问题
     */
    @GetMapping("/queryIssue")
    public ResultDo queryIssue() {
        List<QuestionDo> questionDos = askService.queryIssue();

        resultDo.setCode(200);
        resultDo.setMessage("查询成功");
        resultDo.setBody(questionDos);
        return resultDo;
    }

    /**
     * 查询问题明细
     */
    @GetMapping("/queryIssueDetail")
    public ResultDo queryIssueDetail() {
        List<QuestionDo> questionDos = askService.queryIssueDetail();

        resultDo.setCode(200);
        resultDo.setMessage("查询成功");
        resultDo.setBody(questionDos);
        return resultDo;
    }

    /**
     * 采纳问题
     *
     * @param answerDo
     * @return
     */
    @PostMapping("/adoptProblem")
    public ResultDo adoptProblem(@RequestBody AnswerDo answerDo) throws InterruptedException, IOException, KeeperException {
        askService.adoptProblem(answerDo);

        resultDo.setCode(200);
        resultDo.setMessage("采纳成功");
        return resultDo;
    }

    /**
     * 搜索引擎
     * @param sorlQuestion
     * @return
     */
    @GetMapping("/searchEngine")
    public ResultDo searchEngine(@RequestBody SorlQuestion sorlQuestion) throws IOException, SolrServerException {
        List<SorlQuestion> sorlQuestions = askService.searchEngine(sorlQuestion);
        resultDo.setBody(sorlQuestions);
        resultDo.setCode(200);
        resultDo.setMessage("查询成功");
        return resultDo;
    }
}
