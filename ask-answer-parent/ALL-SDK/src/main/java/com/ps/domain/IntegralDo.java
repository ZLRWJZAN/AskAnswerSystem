package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 9:15
 */
@Data
public class IntegralDo implements Serializable {
    private Integer id;
    private Integer userId;
    private Integer score;
    private String type;
    private String cTime;
}
