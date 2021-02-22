package com.xiao.mall;

import com.xiao.mall.consts.MallConst;
import com.xiao.mall.exception.UserLoginException;
import com.xiao.mall.pojo.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author ：降蓝
 * @description：SpringMVC拦截器，使用HandlerInterceptor基于URL
 * @date ：2021/2/10 19:23
 */

@Slf4j
public class UserLoginInterceptor implements HandlerInterceptor {

    /**
     * true 表示继续流程， false表示中断
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("perHandle.....");
        //判断是否登陆
        User user = (User)request.getSession().getAttribute(MallConst.CURRENT_USER);
        if (user == null) {
            log.info("user==null");
            throw new UserLoginException();
//            return false;
//            return ResponseVo.error(NEED_LOGIN);
        }
        return true;
    }
}
