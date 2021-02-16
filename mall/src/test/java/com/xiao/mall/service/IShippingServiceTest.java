package com.xiao.mall.service;

import com.xiao.mall.MallApplicationTests;
import com.xiao.mall.enums.ResponseEnum;
import com.xiao.mall.form.ShippingForm;
import com.xiao.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.Assert.*;

@Slf4j
public class IShippingServiceTest extends MallApplicationTests {

    @Autowired
    private IShippingService shippingService;

    private Integer uid = 1;

    private ShippingForm form;

    Integer shippingId;

    @Before
    public void before() {
        ShippingForm form = new ShippingForm();
        form.setReceiverName("肖航");
        form.setReceiverPhone("000000");
        form.setReceiverMobile("1323242343");
        form.setReceiverProvince("湖北");
        form.setReceiverCity("湖北市");
        form.setReceiverDistrict("襄阳");
        form.setReceiverAddress("谷城");
        form.setReceiverZip("10000");
        this.form = form;

        add();
    }


    public void add() {
        ResponseVo<Map<String, Integer>> responseVo = shippingService.add(uid,form);
        log.info("result={}",responseVo);
        this.shippingId = responseVo.getData().get("shippingId");
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @After
    public void delete() {

        ResponseVo responseVo = shippingService.delete(uid, shippingId);
        log.info("result={}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void update() {
        form.setReceiverCity("武汉");
        ResponseVo responseVo = shippingService.update(uid,shippingId,form);
        log.info("result={}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo responseVo = shippingService.list(uid,1,10);
        log.info("result={}",responseVo);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }
}