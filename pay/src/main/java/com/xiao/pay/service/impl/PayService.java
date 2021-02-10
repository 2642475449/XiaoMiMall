package com.xiao.pay.service.impl;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayPlatformEnum;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.enums.OrderStatusEnum;
import com.lly835.bestpay.model.PayRequest;
import com.lly835.bestpay.model.PayResponse;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import com.xiao.pay.dao.PayInfoMapper;
import com.xiao.pay.enums.PayPlatformEnum;
import com.xiao.pay.pojo.PayInfo;
import com.xiao.pay.service.IPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * @author ：降蓝
 * @description：IPayService的实现类
 * @date ：2021/2/6 19:42
 */
@Slf4j
@Service
public class PayService implements IPayService {

    @Autowired
    private BestPayService bestPayService;

    @Autowired
    private PayInfoMapper payInfoMapper;

    /**
     * 创建/发动支付
     *
     * @param orderId 订单号
     * @param amount  订单总额
     * @param bestPayTypeEnum 支付平台
     * @date: 2021/2/7 0:16
     */
    @Override
    public PayResponse create(String orderId, BigDecimal amount, BestPayTypeEnum bestPayTypeEnum) {
        //写入数据库
        PayInfo payInfo = new PayInfo(Long.parseLong(orderId),
                PayPlatformEnum.getByBestPayTypeEnum(bestPayTypeEnum).getCode(),
                OrderStatusEnum.NOTPAY.name(),
                amount);
        payInfoMapper.insertSelective(payInfo);

        PayRequest request = new PayRequest();
        request.setOrderName("9071992-最好的支付sdk");//订单名字
        request.setOrderId(orderId);//订单号
        request.setOrderAmount(amount.doubleValue());//订单总额
        request.setPayTypeEnum(bestPayTypeEnum);//支付方式

        PayResponse response = bestPayService.pay(request);
        log.info("发起支付 response={}", response);

        return response;
    }


    /**
     * 异步通知处理
     *
     * @param notifyData
     */
    @Override
    public String asyncNotify(String notifyData) {
        //1.签名检验
        PayResponse payResponse = bestPayService.asyncNotify(notifyData);
        log.info("异步通知 response={}", payResponse);

        //2.金额校验
        //比较严重(正常情况下是不会发生在) 发出告警：钉钉、短信
        PayInfo payInfo = payInfoMapper.selectByOrderNo(Long.parseLong(payResponse.getOrderId()));
        if (payInfo == null) {
            throw new RuntimeException("通过orderNO查询到的结果是null");
        }

        //如果订单支付状态不是"已支付"
        if (!payInfo.getPlatformStatus().equals(OrderStatusEnum.SUCCESS.name())) {
            //Double类型比较大小，精度1.00 1.0
            if (payInfo.getPayAmount().compareTo(BigDecimal.valueOf(payResponse.getOrderAmount())) != 0){
                //告警
                throw new RuntimeException("异步通知中的金额和数据库里的不一致, orderNO=" + payResponse.getOrderId());
            }
        }

        //3.修改订单支付状态
        payInfo.setPlatformStatus(OrderStatusEnum.SUCCESS.name());
        payInfo.setPlatformNumber(payResponse.getOutTradeNo());
        payInfo.setUpdateTime(null);
        payInfoMapper.updateByPrimaryKeySelective(payInfo);

        //TODO pay发送MQ消息,mall接受MQ消息

        //4.告诉支付平台不要再通知了
        if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.WX) {
            return "<xml>\n" +
                    "  <return_code><![CDATA[SUCCESS]]></return_code>\n" +
                    "  <return_msg><![CDATA[OK]]></return_msg>\n" +
                    "</xml>";
        } else if (payResponse.getPayPlatformEnum() == BestPayPlatformEnum.ALIPAY) {
            return "success";
        }

        throw new RuntimeException("异步通知中错误的支付平台");
    }

    @Override
    public PayInfo queryByOrderId(String orderId) {
        return payInfoMapper.selectByOrderNo(Long.parseLong(orderId));
    }
}
