package com.xiao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/19 16:49
 */
@Data
public class OrderCreateForm {
    @NotNull
    private Integer shippingId;
}
