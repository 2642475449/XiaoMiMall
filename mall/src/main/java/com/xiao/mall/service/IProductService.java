package com.xiao.mall.service;

import com.github.pagehelper.PageInfo;
import com.xiao.mall.vo.ProductDetailVo;
import com.xiao.mall.vo.ResponseVo;

public interface IProductService {

    /**
     *
     * @param categoryId category_id
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer categoryId, Integer pageNum, Integer pageSize);

    ResponseVo<ProductDetailVo> detail(Integer productId);

}
