package com.ps.mapper;

import com.ps.domain.AnswerDo;
import com.ps.domain.QuestionDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 11:14
 */
@Repository
@Mapper
public interface AskMapper {
    /**
     * 发布问题
     *
     * @param questionDo
     * @return
     */
    int postQuestions(QuestionDo questionDo);

    /**
     * 回答问题
     *
     * @param answerDo
     * @return
     */
    int answerIssue(AnswerDo answerDo);

    /**
     * 查询问题以及明细
     *
     * @return
     */
    List<QuestionDo> queryIssueAndDetail();

    /**
     * 采纳问题
     *
     * @param answerDo
     * @return
     */
    int adoptProblem(AnswerDo answerDo);

    /**
     * 采纳之后修改问题表中的状态为  解决
     *
     * @param answerDo
     * @return
     */
    int updateFlag(AnswerDo answerDo);

    /**
     * 根据问题id查询出提问者id与悬赏积分
     *
     * @param answerDo
     * @return
     */
    QuestionDo queryAskIdAndReward(AnswerDo answerDo);

    /**
     * 给回答者加积分
     *
     * @param replierId
     * @param reward
     * @return
     */
    int addReward(@Param("replierId") Integer replierId, @Param("reward") Integer reward);

    /**
     * 给提问者减积分
     *
     * @param questionDo
     * @return
     */
    int subtractReward(QuestionDo questionDo);

    /**
     * 增加发布问题的积分流水记录
     *
     * @param questionDo
     * @return
     */
    int addIssueRecord(QuestionDo questionDo);

    /**
     * 增加回答问题的积分流水记录
     *
     * @param replierId
     * @param reward
     * @return
     */
    int addAnswerRecord(Integer replierId, Integer reward);

    /**
     * 采纳活动
     * @param value
     */
    void acceptActivity(Object value);

    /**
     * 增加采纳活动积分流水记录
     * @param value
     */
    void acceptActivityWater(Object value);
}
