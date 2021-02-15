package com.xiao.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/14 19:18
 */
@Data
public class CartProductVo {

    private Integer productId;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 副标题
     */
    private String productSubtitle;

    /**
     * 主图
     */
    private String productMainImage;

    /**
     * 商品价格
     */
    private BigDecimal productPrice;

    /**
     * 商品状态
     */
    private Integer productStatus;

    /**
     * 商品总价 等于数量 * 单价
     */
    private BigDecimal productTotalPrice;

    /**
     * 商品囤积
     */
    private Integer productStock;

    /**
     *商品是否选中
     */
    private Boolean productSelected;

    public CartProductVo(Integer productId, Integer quantity, String productName, String productSubtitle, String productMainImage, BigDecimal productPrice, Integer productStatus, BigDecimal productTotalPrice, Integer productStock, Boolean productSelected) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productSubtitle = productSubtitle;
        this.productMainImage = productMainImage;
        this.productPrice = productPrice;
        this.productStatus = productStatus;
        this.productTotalPrice = productTotalPrice;
        this.productStock = productStock;
        this.productSelected = productSelected;
    }
}
