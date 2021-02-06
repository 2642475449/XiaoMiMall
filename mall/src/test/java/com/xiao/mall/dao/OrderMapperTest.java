package com.xiao.mall.dao;

import com.xiao.mall.MallApplicationTests;
import com.xiao.mall.pojo.Order;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.*;

public class OrderMapperTest extends MallApplicationTests {

    @Autowired
    private OrderMapper orderMapper;

    @Test
    public void deleteByPrimaryKey() {
    }

    @Test
    public void insert() {
    }

    @Test
    public void insertSelective() {
    }

    @Test
    public void selectByPrimaryKey() {
        Order order = orderMapper.selectByPrimaryKey(1);
        System.out.println(order.toString());
    }

    @Test
    public void updateByPrimaryKeySelective() {
    }

    @Test
    public void updateByPrimaryKey() {
    }
}