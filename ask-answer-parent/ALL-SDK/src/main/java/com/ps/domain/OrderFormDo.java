package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author：ZLRWJSAN
 * @date 2019/8/3 16:47
 */
@Data
public class OrderFormDo implements Serializable {
    //订单id
    private Integer id;
    //商品id
    private Integer commodityId;
    //用户id
    private Integer memberId;
    //创建时间
    private String time;
}
