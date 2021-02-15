package com.xiao.mall.service;

import com.github.pagehelper.PageInfo;
import com.xiao.mall.MallApplicationTests;
import com.xiao.mall.enums.ResponseEnum;
import com.xiao.mall.vo.ProductDetailVo;
import com.xiao.mall.vo.ProductVo;
import com.xiao.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class IProductServiceTest extends MallApplicationTests {

    @Autowired
    private IProductService productService;

    @Test
    public void list() {
        ResponseVo<PageInfo> responseVo = productService.list(null, 2, 3);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());
    }

    @Test
    public void detail () {
        ResponseVo<ProductDetailVo> responseVo = productService.detail(26);
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }
}