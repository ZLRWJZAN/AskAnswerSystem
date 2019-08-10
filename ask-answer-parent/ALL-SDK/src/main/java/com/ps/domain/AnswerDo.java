package com.ps.domain;

import lombok.Data;
import org.apache.solr.client.solrj.beans.Field;

import java.io.Serializable;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 14:14
 */
@Data
public class AnswerDo implements Serializable {
    private Integer id;
    private Integer questionId;
    private Integer replierId;
    private String content;
    private String replyTime;
    private String accept;
}
