package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 20:19
 */
@Data
public class ProblemDo implements Serializable {
    private Integer id;
    private Integer questionnaireId;
    private Integer number;
    private String problem;
}
