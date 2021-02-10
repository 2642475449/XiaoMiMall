package com.xiao.pay.service.impl;

import com.lly835.bestpay.enums.BestPayTypeEnum;
import com.xiao.pay.PayApplicationTests;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class PayServiceTest extends PayApplicationTests {
    @Autowired
    private PayService payService;

    @Test
    public void create() {
        payService.create("1323432513", BigDecimal.valueOf(0.01), BestPayTypeEnum.WXPAY_NATIVE);
    }
}