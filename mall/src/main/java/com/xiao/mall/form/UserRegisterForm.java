package com.xiao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ：降蓝
 * @description：将UserCOntroller.register中传回的参数添加
 * @date ：2021/2/9 23:56
 */
@Data
public class UserRegisterForm {
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "邮箱不能为空")
    private String email;
}
