package com.xiao.mall.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ：降蓝
 * @description：购物车api
 * @date ：2021/2/14 19:11
 */
@Data
public class CartVo {
    private List<CartProductVo> cartProductVoList;
    private Boolean selectedAll;
    private BigDecimal cartTotalPrice;
    private Integer cartTotalQuantity;
}
