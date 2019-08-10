package com.ps.mapper;

import com.ps.domain.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 19:56
 */
@Repository
@Mapper
public interface MarketMapper {
    /**
     * 创建问卷
     *
     * @param problemDo
     * @return
     */
    int createQuestionnaire(ProblemDo problemDo);

    /**
     * 创建标题
     *
     * @param questionnaireDo
     * @return
     */
    int createTitle(QuestionnaireDo questionnaireDo);

    /**
     * 提交答案
     *
     * @param findingsDo
     * @return
     */
    int provideQuestionnaire(FindingsDo findingsDo);

    /**
     * 查询出回答该套问卷的悬赏积分
     * @param questionnaireId
     * @return
     */
    Integer queryIntegral(Integer questionnaireId);

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
     * 注册活动
     * @param value
     */
    void registerActivity(Object value);

    /**
     * 查询商品信息
     * @return
     */
    CommodityDo queryCommodity(Integer commodityId);

    /**
     * 查询是否兑换过
     * @param id
     * @param commodityId
     * @return
     */
    OrderFormDo whetherExchange(Integer id, Integer commodityId);

    /**
     * 减库存
     * @param commodityId
     */
    int subtractInventory(Integer commodityId,Integer version);

    /**
     * 创建订单
     * @param id
     * @param commodityId
     */
    int addOrderForm(Integer id, Integer commodityId);
}
