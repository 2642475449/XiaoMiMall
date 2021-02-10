package com.xiao.mall.exception;

import com.xiao.mall.enums.ResponseEnum;
import com.xiao.mall.vo.ResponseVo;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author ：降蓝
 * @description：捕获Controller层运行时异常
 * @date ：2021/2/10 11:35
 */
@ControllerAdvice
public class RuntimeExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseBody
//    @ResponseStatus(HttpStatus.FORBIDDEN)//更改状态码为403
    public ResponseVo handle(RuntimeException e) {
        return ResponseVo.error(ResponseEnum.ERROR,e.getMessage());
    }
   @ExceptionHandler(UserLoginException.class)
    @ResponseBody
    public ResponseVo userLoginHandle() {
       return ResponseVo.error(ResponseEnum.NEED_LOGIN);
   }

}
