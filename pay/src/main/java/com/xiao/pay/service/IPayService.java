package com.xiao.pay.service;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.xiao.pay.pojo.PayInfo;

import java.math.BigDecimal;

/**
 * @author ：降蓝
 * @date ：2021/2/6 19:34
 */
public interface IPayService {
    /**
     * 创建/发起支付
     */
    PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum);

    /**
     * 异步通知处理
     * @param notifyData
     */
    String  asyncNotify(String notifyData);

    /**
     * 查询支付记录(通过订单号)
     * @param orderId
     * @return
     */
    PayInfo queryByOrderId(String orderId);
}
