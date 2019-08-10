package com.ps.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author：ZLRWJSAN
 * @date 2019/8/3 16:49
 */
@Data
public class CommodityDo implements Serializable {

    //商品ID
    private Integer id;

    //商品名字
    private String name;

    //商品数量
    private Integer number;

    //需要积分
    private Integer needIntegral;

    //商品价值
    private Integer money;

    //商品描述
    private String describes;

    //开始时间
    private Date beginTime;

    //结束时间
    private Date endTime;

    //版本
    private Integer version;
}
