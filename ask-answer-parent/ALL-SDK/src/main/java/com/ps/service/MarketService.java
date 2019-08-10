package com.ps.service;

import com.ps.domain.FindingsDo;
import com.ps.domain.ProblemDo;
import com.ps.domain.QuestionnaireDo;

import java.util.List;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/18 19:41
 */
public interface MarketService {
    /**
     * 创建问卷
     * @param problemDo
     * @return
     */
    void createQuestionnaire(List<ProblemDo> problemDo);

    /**
     * 提交答案
     * @param findingsDo
     * @return
     */
    void provideQuestionnaire(List<FindingsDo> findingsDo);

    /**
     * 创建标题
     * @param questionnaireDo
     */
    void createTitle(QuestionnaireDo questionnaireDo);

    /**
     * 查询出回答该套问卷的悬赏积分
     * @param findingsDo
     * @return
     */
    Integer queryIntegral(List<FindingsDo> findingsDo);

    /**
     * 查询出问卷标题
     * @return
     */
    QuestionnaireDo queryTitle();

    /**
     * 根据问卷标题id查询出问卷内容
     * @param questionnaireDo
     * @return
     */
    List<ProblemDo> queryContent(QuestionnaireDo questionnaireDo);

    /**
     * 抢购商品
     * @param id
     * @param commodityId
     */
    void exchange(int id, int commodityId);
}
