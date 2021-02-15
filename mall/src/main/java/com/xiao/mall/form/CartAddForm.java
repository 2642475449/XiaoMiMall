package com.xiao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ：降蓝
 * @description：购物车添加表单(传入参数)
 * @date ：2021/2/14 19:48
 */
@Data
public class CartAddForm {

    @NotNull
    private Integer productId;
    private Boolean selected = true;

}
