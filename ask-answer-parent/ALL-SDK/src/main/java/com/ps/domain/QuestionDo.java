package com.ps.domain;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 10:57
 */
@Data
public class QuestionDo implements Serializable{
    private Integer id;
    private Integer promulgatorId;
    private String title;
    private String content;
    private String cTime;
    private String eTime;
    private String flag;
    private Integer reward;
    private VipDo vipDo;
    private AnswerDo answerDo;
}
