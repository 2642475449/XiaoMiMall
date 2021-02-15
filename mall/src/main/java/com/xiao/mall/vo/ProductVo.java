package com.xiao.mall.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author ：降蓝
 * @description：
 * @date ：2021/2/12 18:10
 */
@Data
public class ProductVo {
    /**
     * 商品id
     */
    private Integer id;

    /**
     * 分类id,对应mall_category表的主键
     */
    private Integer categoryId;

    /**
     * 商品名称
     */
    private String name;

    /**
     * 商品副标题
     */
    private String subtitle;

    /**
     * 产品主图,url相对地址
     */
    private String mainImage;

    /**
     * 商品状态.1-在售 2-下架 3-删除
     */
    private Integer status;

    /**
     * 价格,单位-元保留两位小数
     */
    private BigDecimal price;

}
