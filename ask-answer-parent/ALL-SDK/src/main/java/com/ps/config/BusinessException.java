package com.ps.config;

import lombok.Data;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 10:02
 * 统一异常处理
 */
@Data
public class BusinessException extends RuntimeException {
    private Integer code;
    private String message;

    public BusinessException(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
