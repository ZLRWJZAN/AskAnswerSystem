package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 20:38
 */
@Data
public class FindingsDo implements Serializable {
    private Integer id;
    private Integer questionnaireId;
    private Integer memberId;
    private Integer problemId;
    private String options;
}
