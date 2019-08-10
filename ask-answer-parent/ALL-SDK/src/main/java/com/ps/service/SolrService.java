package com.ps.service;

import com.ps.domain.AnswerDo;

import java.util.List;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/23 9:31
 */
public interface SolrService {
    List<AnswerDo> queryAns();
}
