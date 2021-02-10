package com.xiao.pay.enums;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import lombok.Getter;

/**
 * @author 降蓝
 *
 */
@Getter
public enum  PayPlatformEnum {

    //1-支付宝，2-微信
    ALIPAY(1),
    WX(2),
    ;

    Integer code;

    PayPlatformEnum(Integer code) {
        this.code = code;
    }

    public static PayPlatformEnum getByBestPayTypeEnum(BestPayTypeEnum bestPayTypeEnum) {
//        if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.ALIPAY.name())) {
//            return PayPlatformEnum.ALIPAY;
//        } else if (bestPayTypeEnum.getPlatform().name().equals(PayPlatformEnum.WX.name())) {
//            return PayPlatformEnum.WX;
//        }

        for (PayPlatformEnum payPlatformEnum : PayPlatformEnum.values()) {
            if (bestPayTypeEnum.getPlatform().name().equals(payPlatformEnum.name())) {
                return payPlatformEnum;
            }
        }
        throw new RuntimeException("错误的支付平台："+bestPayTypeEnum.name());
    }
}
