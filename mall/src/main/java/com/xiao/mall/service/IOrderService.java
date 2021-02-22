package com.xiao.mall.service;

import com.github.pagehelper.PageInfo;
import com.xiao.mall.vo.OrderVo;
import com.xiao.mall.vo.ResponseVo;

public interface IOrderService {

    /**
     * 创建订单
     *
     * @param uid 用户id
     * @param shippingId 购物车id
     * @return
     */
    ResponseVo<OrderVo> create(Integer uid, Integer shippingId);

    /**
     *订单List
     *
     * @param uid   用户id
     * @param pageNum 页码
     * @param pageSize 每页记录条数
     * @return
     */
    ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize);

    /**
     *订单详情
     *
     * @param uid       用户id
     * @param orderNo   订单号
     * @return
     */
    ResponseVo<OrderVo> detail(Integer uid, Long orderNo);

    /**
     * 订单取消
     *
     * @param uid       用户id
     * @param orderNo   订单号
     * @return
     */
    ResponseVo cancel(Integer uid, Long orderNo);

    /**
     * 更新订单状态
     * @param orderNo
     */
    void paid(Long orderNo);

}
