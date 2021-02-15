package com.xiao.mall.controller;

import com.xiao.mall.consts.MallConst;
import com.xiao.mall.form.UserLoginForm;
import com.xiao.mall.form.UserRegisterForm;
import com.xiao.mall.pojo.User;
import com.xiao.mall.service.IUserService;
import com.xiao.mall.vo.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Objects;

import static com.xiao.mall.enums.ResponseEnum.PARAM_ERROR;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/9 19:16
 */
@RestController
@Slf4j
public class UserController {

    @Autowired
    private IUserService userService;

    /**
     * 注册
     */
    @PostMapping("/user/register")
    public ResponseVo register(@Valid @RequestBody UserRegisterForm userRegisterForm) {
        User user = new User();
        BeanUtils.copyProperties(userRegisterForm,user);
        return userService.register(user);
    }

    /**
     * 用户登陆
     * @param userLoginForm
     * @param bindingResult
     * @param session
     * @return
     */
    @PostMapping("/user/login")
    public ResponseVo<User> login(@Valid @RequestBody UserLoginForm userLoginForm, HttpSession session) {

        ResponseVo<User> userResponseVo = userService.login(userLoginForm.getUsername(), userLoginForm.getPassword());
        
        //设置Session
        session.setAttribute(MallConst.CURRENT_USER,userResponseVo.getData());
        log.info("/login sessionId={}"+session.getId());
        return userResponseVo;
    }

    /**
     *获取登录用户信息
     * session保存在内存里，改进版：token+redis
     */
    @GetMapping("/user")
    public ResponseVo<User> userInfo(HttpSession session) {
        log.info("/user sessionId={}"+session.getId());
        User user = (User)session.getAttribute(MallConst.CURRENT_USER);

        return ResponseVo.success(user);
    }

    //TODO 判断登录状态
    @PostMapping("/user/logout")
    /**
     * {@link org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory}
     */
    public ResponseVo<User> logout(HttpSession session) {
        log.info("/user/logout sessionId={}",session.getId());

        session.removeAttribute(MallConst.CURRENT_USER);
        return ResponseVo.success();
    }

}
