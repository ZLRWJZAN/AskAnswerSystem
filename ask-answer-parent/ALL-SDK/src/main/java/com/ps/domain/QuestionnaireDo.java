package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 20:28
 */
@Data
public class QuestionnaireDo implements Serializable {
    private Integer id;
    private String title;
    private Integer integral;
}
