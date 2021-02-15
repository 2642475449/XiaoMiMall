package com.xiao.mall.form;

import lombok.Data;

/**
 * @author ：降蓝
 * @description：购物车更新
 * @date ：2021/2/15 21:14
 */
@Data
public class CartUpdateForm {
    private Integer quantity;
    private Boolean selected;
}
