package com.ps.config;

import com.ps.domain.ResultDo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/19 10:05
 */
@ControllerAdvice
public class ExceptionAdvice {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResultDo businessExceotion(BusinessException b) {
        //当捕获到此异常时,返回Message对象到前台-------所以加上@ResponseBody注解
        return new ResultDo(b.getCode(), b.getMessage());
    }
}
