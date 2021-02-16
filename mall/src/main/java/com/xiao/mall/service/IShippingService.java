package com.xiao.mall.service;

import com.github.pagehelper.PageInfo;
import com.xiao.mall.form.ShippingForm;
import com.xiao.mall.vo.ResponseVo;

import java.util.Map;

/**
 * 收货地址业务层
 */
public interface IShippingService {

    /**
     * 添加地址
     * @param uid
     * @param form
     * @return
     */
    ResponseVo<Map<String, Integer>> add(Integer uid, ShippingForm form);

    /**
     * 删除地址
     * @param uid
     * @param shippingId
     * @return
     */
    ResponseVo delete(Integer uid, Integer shippingId);

    /**
     * 更新地址
     * @param uid
     * @param shippingId
     * @param form
     * @return
     */
    ResponseVo update(Integer uid, Integer shippingId, ShippingForm form);

    /**
     * 地址列表
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     */
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);


}
