package com.ps.domain;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;
import java.util.List;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/23 10:56
 */
@Data
public class SorlQuestion implements Serializable {
    @Field("aId")
    private Integer aId;
    @Field("question_id")
    private Integer questionId;
    @Field("replier_id")
    private Integer replierId;
    @Field("content")
    private List<String> content;
    @Field("reply_time")
    private String replyTime;
    @Field("accept")
    private String accept;

    @Field("qId")
    private Integer qId;
    @Field("promulgator_id")
    private Integer promulgatorId;
    @Field("title")
    private List<String> title;
    @Field("contents")
    private List<String> contents;
    @Field("c_time")
    private String cTime;
    @Field("e_time")
    private String eTime;
    @Field("flag")
    private String flag;
    @Field("reward")
    private Integer reward;

    private String context;
}
