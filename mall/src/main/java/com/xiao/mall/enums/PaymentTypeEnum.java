package com.xiao.mall.enums;

import lombok.Getter;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/18 14:36
 */
@Getter
public enum PaymentTypeEnum {
    PAY_ONLINE(1),
    ;
    Integer code;

    PaymentTypeEnum(Integer code) {
        this.code = code;
    }
}
