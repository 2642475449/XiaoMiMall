package com.xiao.mall.pojo;

import lombok.Data;

/**
 * @author ：降蓝
 * @description：为保证数据的及时性和有效性，将一部分字段放入MySQL一部分放入Redis,
 *               面对不易改变的字段我们可以将它放入Redis
 * @date ：2021/2/15 9:55
 */
@Data
public class Cart {
    /**
     * 商品id
     */
    private Integer productId;
    /**
     * 数量
     */
    private Integer  quantity;
    /**
     * 是否选中
     */
    private Boolean productSelected;

    public Cart() {
    }

    public Cart(Integer productId, Integer quantity, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productSelected = productSelected;
    }
}
