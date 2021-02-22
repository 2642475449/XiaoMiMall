package com.xiao.mall.service;

import com.github.pagehelper.PageInfo;
import com.xiao.mall.vo.ProductDetailVo;
import com.xiao.mall.vo.ResponseVo;

public interface IProductService {

    /**
     *商品列表
     * @param categoryId category_id
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    /**
     *商品详情
     * @param productId
     * @return
     */
    ResponseVo<ProductDetailVo> detail(Integer productId);

}
