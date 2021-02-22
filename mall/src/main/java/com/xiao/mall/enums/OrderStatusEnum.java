package com.xiao.mall.enums;

import lombok.Getter;

/**
 * @author ：降蓝
 * @description：订单状态
 * @date ：2021/2/18 14:41
 */
@Getter
public enum  OrderStatusEnum {
    CANCELED(0,"取消"),
    NO_PAY(10,"未付款"),
    PAID(20,"已付款"),
    SHIPPED(40,"已发货"),
    TRADE_SUCCESS(50,"交易成功"),
    TRADE_CLOSE(60,"交易关闭"),
    ;

    Integer code;

    String desc;

    OrderStatusEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
