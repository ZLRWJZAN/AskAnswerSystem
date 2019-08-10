package com.ps.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * 作者：ZLRWJSAN
 * 创建于 2019/6/24 22:58
 * @author Administrator
 */

@Data
public class ResultDo<T> implements Serializable {
    private T body;
    private Integer code;
    private String message;

    public ResultDo(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public ResultDo(){}
}
