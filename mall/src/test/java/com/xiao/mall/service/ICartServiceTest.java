package com.xiao.mall.service;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xiao.mall.MallApplicationTests;
import com.xiao.mall.enums.ResponseEnum;
import com.xiao.mall.form.CartAddForm;
import com.xiao.mall.form.CartUpdateForm;
import com.xiao.mall.vo.CartVo;
import com.xiao.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
@Slf4j
public class ICartServiceTest extends MallApplicationTests {
    @Autowired
    private ICartService cartService;

    private Gson gson =new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void add() {
        CartAddForm form = new CartAddForm();
        form.setProductId(26);
        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.add(1, form);
        log.info("list={}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void list() {
        ResponseVo<CartVo> responseVo = cartService.list(1);
        log.info("list={}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void update() {
        CartUpdateForm form = new CartUpdateForm();
        form.setQuantity(5);
//        form.setSelected(true);
        ResponseVo<CartVo> responseVo = cartService.update(1, 26,form);
        log.info("result={}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }

    @Test
    public void delete() {
        ResponseVo<CartVo> responseVo = cartService.delete(1, 26);
        log.info("result={}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }

    @Test
    public void selectAll() {
        ResponseVo<CartVo> responseVo = cartService.selectAll(1);
        log.info("result={}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }

    @Test
    public void unselectAll() {
        ResponseVo<CartVo> responseVo = cartService.unselectAll(1);
        log.info("result={}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }

    @Test
    public void sum() {
        ResponseVo<Integer> responseVo = cartService.sum(1);
        log.info("result={}",gson.toJson(responseVo));
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

}