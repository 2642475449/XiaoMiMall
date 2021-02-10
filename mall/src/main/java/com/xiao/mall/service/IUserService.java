package com.xiao.mall.service;

import com.xiao.mall.pojo.User;
import com.xiao.mall.vo.ResponseVo;

public interface IUserService {
    /**
     * 注册
     */
    ResponseVo<User> register(User user);

    /**
     * 登陆
     */
    ResponseVo<User> login(String username, String password);
}
