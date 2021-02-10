package com.xiao.pay.config;

import com.lly835.bestpay.config.AliPayConfig;
import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.service.BestPayService;
import com.lly835.bestpay.service.impl.BestPayServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author ：降蓝
 * @date ：2021/2/7 15:13
 */
@Component
public class BestPayConfig {

    @Autowired
    private WxAccountConfig wxAccountConfig;

    @Autowired
    private AliPayAccountConfig aliPayAccountConfig;

    @Bean
    public BestPayService bestPayService(WxPayConfig wxPayConfig) {
        //设置支付宝参数
        AliPayConfig aliPayConfig = new AliPayConfig();
        aliPayConfig.setAppId(aliPayAccountConfig.getAppId());
        aliPayConfig.setPrivateKey(aliPayAccountConfig.getPrivateKey());
        aliPayConfig.setAliPayPublicKey(aliPayAccountConfig.getPublicKey());
        aliPayConfig.setNotifyUrl(aliPayAccountConfig.getNotifyUrl());
        aliPayConfig.setReturnUrl(aliPayAccountConfig.getReturnUrl());

        //传入设置
        BestPayServiceImpl bestPayService = new BestPayServiceImpl();
        bestPayService.setWxPayConfig(wxPayConfig);
        bestPayService.setAliPayConfig(aliPayConfig);

        return bestPayService;
    }

    @Bean
    public WxPayConfig wxPayConfig() {
        //设置微信参数
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(wxAccountConfig.getAppId());//公众号appId
        wxPayConfig.setMchId(wxAccountConfig.getMchId());//商户id
        wxPayConfig.setMchKey(wxAccountConfig.getMchKey());//密钥
        wxPayConfig.setNotifyUrl(wxAccountConfig.getNotifyUrl());//异步通知，下单成功后，支付平台服务器通知商户服务，并把这笔订单的状态通知给商户，商户根据返回的这笔订单的状态，修改网站订单的状态
        wxPayConfig.setReturnUrl(wxAccountConfig.getReturnUrl());//跳转页面，买家支付成功后跳转的页面，仅当买家支付成功后跳转一次


//        WxPayConfig wxPayConfig1 = wxPayConfig();
        return wxPayConfig;
    }
}
