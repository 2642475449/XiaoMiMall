package com.xiao.mall.service.impl;

import com.xiao.mall.MallApplicationTests;
import com.xiao.mall.enums.ResponseEnum;
import com.xiao.mall.enums.RoleEnum;
import com.xiao.mall.pojo.User;
import com.xiao.mall.service.IUserService;
import com.xiao.mall.vo.ResponseVo;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImplTest extends MallApplicationTests {
    public static final String USERNAME = "jack";
    public static final String PASSWORD = "123456";

    @Autowired
    private IUserService userService;



    @Before
    public void register() {
        User user = new User(USERNAME,PASSWORD,"jack@qq.com", RoleEnum.ADMIN.getCode());
        userService.register(user);
    }

    @Test
    public void login() {
        ResponseVo<User> responseVo = userService.login(USERNAME, PASSWORD);
        /**
         * 断言,主用于比较测试传递进去的两个参数
         */
        Assert.assertEquals(ResponseEnum.SUCCESS.getCode(),responseVo.getStatus());

    }
}