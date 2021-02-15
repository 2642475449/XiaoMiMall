package com.xiao.mall.controller;



import com.xiao.mall.consts.MallConst;
import com.xiao.mall.form.CartAddForm;
import com.xiao.mall.form.CartUpdateForm;
import com.xiao.mall.pojo.User;
import com.xiao.mall.service.ICartService;
import com.xiao.mall.vo.CartVo;
import com.xiao.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/14 19:51
 */
@RestController
public class CartController {

    @Autowired
    private ICartService cartService;

    /**
     * 购物车List列表
     * @param session
     * @return
     */
    @GetMapping("/carts")
    public ResponseVo<CartVo> list(HttpSession session) {
        User user = (User)session.getAttribute(MallConst.CURRENT_USER);

        return cartService.list(user.getId());
    }

    /**
     * 添加商品到购物车
     * @param cartAddForm
     * @param session
     * @return
     */
    @PostMapping("/carts")
    public ResponseVo<CartVo> add(@Valid @RequestBody CartAddForm cartAddForm, HttpSession session) {
        User user = (User)session.getAttribute(MallConst.CURRENT_USER);

        return cartService.add(user.getId(),cartAddForm);
    }

    /**
     * 更新购物车
     * @param productId
     * @param cartAddForm
     * @param session
     * @return
     */
    @PutMapping("/carts/{productId}")
    public ResponseVo<CartVo> update(@PathVariable Integer productId,
                                     @Valid @RequestBody CartUpdateForm cartAddForm,
                                     HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.update(user.getId(),productId, cartAddForm);
    }

    /**
     * 移除购物车某个产品
     * @param productId
     * @param session
     * @return
     */
    @DeleteMapping("/carts/{productId}")
    public ResponseVo<CartVo> delete(@PathVariable Integer productId,
                                     HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.delete(user.getId(),productId);
    }

    /**
     * 全选
     * @param productId
     * @param session
     * @return
     */
    @PutMapping("/carts/selectAll")
    public ResponseVo<CartVo> selectAll(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.selectAll(user.getId());
    }

    /**
     * 全不选
     * @param productId
     * @param session
     * @return
     */
    @PutMapping("/carts/unselectAll")
    public ResponseVo<CartVo> unSelectAll(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.unselectAll(user.getId());
    }

    @GetMapping("/carts/products/sum")
    public ResponseVo<CartVo> sum(HttpSession session) {
        User user = (User) session.getAttribute(MallConst.CURRENT_USER);
        return cartService.sum(user.getId());
    }
}
