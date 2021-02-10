package com.xiao.pay.controller;

import com.lly835.bestpay.config.WxPayConfig;
import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.lly835.bestpay.model.PayResponse;
import com.xiao.pay.pojo.PayInfo;
import com.xiao.pay.service.impl.PayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ：降蓝
 * @date ：2021/2/7 11:18
 */


@Slf4j
@Controller
@RequestMapping("/pay")
public class PayController {

    @Autowired
    private PayService payService;

    @Autowired
    private WxPayConfig wxPayConfig;

    /**
     * @description:
     * @return 视图(传入参数生成qrcode)
     */
    @GetMapping("/create")
    public ModelAndView create(@RequestParam("orderId") String orderId,
                               @RequestParam("amount") BigDecimal amount,
                               @RequestParam("payType") BestPayTypeEnum bestPayTypeEnum
                                ){

        PayResponse response = payService.create(orderId, amount, bestPayTypeEnum);

        //支付方式不同，渲染不同，WXPAY_NATIVE使用codeUrl，ALIPAY_PC使用body
        Map<String,String> map = new HashMap<>();
        if (bestPayTypeEnum == BestPayTypeEnum.WXPAY_NATIVE) {
            map.put("codeUrl",response.getCodeUrl());
            map.put("orderId",orderId);
            map.put("returnUrl",wxPayConfig.getReturnUrl());
            return new ModelAndView("createForWxNative",map);
        } else if (bestPayTypeEnum == BestPayTypeEnum.ALIPAY_PC){
            map.put("body", response.getBody());
            return new ModelAndView("createForAlipayPc",map);
        }
        throw new RuntimeException("暂不支持的支付类型");
    }

    /**
     *  微信异步通知
     * @param notifyData
     */
    @ResponseBody
    @PostMapping("/notify")
    public String asyncNotify(@RequestBody String notifyData) {
        return   payService.asyncNotify(notifyData);
    }

    /**
     * 根据订单号查询订单
     * @param orderId 订单号
     * @return JSON格式的订单
     */
    @ResponseBody
    @GetMapping("/queryByOrderId")
    public PayInfo queryByOrderId(@RequestParam String orderId) {
        log.info("查询支付记录...");
        return payService.queryByOrderId(orderId);
    }

}
