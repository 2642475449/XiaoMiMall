package com.xiao.mall.service;

import com.xiao.mall.form.CartAddForm;
import com.xiao.mall.form.CartUpdateForm;
import com.xiao.mall.pojo.Cart;
import com.xiao.mall.vo.CartVo;
import com.xiao.mall.vo.ResponseVo;

import java.util.List;

/**
 * 购物车业务层
 */
public interface ICartService {

    /**
     * 1.添加商品到购物车
     * @param uid Redis表单名称
     * @param form 购物车添加表单(传入参数)
     * @return
     */
    ResponseVo<CartVo> add(Integer uid, CartAddForm form);

    /**
     * 2.购物车列表
     * @param uid
     * @return 购物车列表
     */
    ResponseVo<CartVo> list(Integer uid);

    /**
     * 3.更改购物车
     * @param uid
     * @param productId
     * @param form
     * @return
     */
    ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form);

    /**
     * 4.移除购物车某个产品
     * @param uid
     * @param productId
     * @return
     */
    ResponseVo<CartVo> delete(Integer uid, Integer productId);

    /**
     * 5.全选中
     * @param uid
     * @return
     */
    ResponseVo<CartVo> selectAll(Integer uid);

    /**
     * 6.全不选中
     * @param uid
     * @return
     */
    ResponseVo<CartVo> unselectAll(Integer uid);

    /**
     * 7.获取购物中所有商品数量总和
     * @param uid
     * @return
     */
    ResponseVo<Integer> sum(Integer uid);

    List<Cart> listForCart(Integer uid);
}
