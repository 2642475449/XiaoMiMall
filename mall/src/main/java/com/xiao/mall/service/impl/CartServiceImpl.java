package com.xiao.mall.service.impl;

import com.google.gson.Gson;
import com.xiao.mall.dao.ProductMapper;
import com.xiao.mall.enums.ProductStatusEnum;
import com.xiao.mall.enums.ResponseEnum;
import com.xiao.mall.form.CartAddForm;
import com.xiao.mall.form.CartUpdateForm;
import com.xiao.mall.pojo.Cart;
import com.xiao.mall.pojo.Product;
import com.xiao.mall.service.ICartService;
import com.xiao.mall.vo.CartProductVo;
import com.xiao.mall.vo.CartVo;
import com.xiao.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author ：降蓝
 * @description：购物车业务
 * @date ：2021/2/14 21:43
 */
@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;
    //商品数量
    Integer quantity = 1;
    private final static String CART_REDIS_KEY_TEMPLATE = "cart_%d";
    //序列化：用来在 Java 对象和 JSON 数据之间进行映射
    private Gson gson = new Gson();


    /**
     * 购物车添加业务
     * @param uid
     * @param form 购物车添加表单(传入参数)
     * @return
     */
    @Override
    public ResponseVo<CartVo> add(Integer uid, CartAddForm form) {

        Product product = productMapper.selectByPrimaryKey(form.getProductId());

        /**
         * 表单统一验证处理
         * 商品是否存在、商品是否正常在售、商品库存是否充足
         */
        //商品是否存在
        if (product == null) {
            return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST);
        }

        //商品是否正常在售
        if (!product.getStatus().equals(ProductStatusEnum.ON_SALE.getCode())) {
            return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE);
        }

        //商品库存是否充足
        if (product.getStock() <= 0) {
            return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR);
        }
        /**
         *  写入到redis,新增一个字符串类型的值,以Map的格式存入
         *  key: cart_1
         * H redis中的KEY
         * HK 集合key
         * HV 集合值
         */
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();

        //Redis中的表单名称
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);

        //redis中的value
        String value = opsForHash.get(redisKey, String.valueOf(product.getId()));

        Cart cart = new Cart();
        if (StringUtils.isEmpty(value)) {
            //没有该商品，新增
            cart = new Cart(product.getId(), quantity, form.getSelected());
        } else {
            //已经有了，数量+1
            cart = gson.fromJson(value, Cart.class);//反序列化
            cart.setQuantity(cart.getQuantity() + quantity);
        }
        opsForHash.put(redisKey, String.valueOf(product.getId()), gson.toJson(cart));
        return list(uid);
    }

    /**
     * 返回购物车列表
     * @param uid
     * @return
     */
    @Override
    public ResponseVo<CartVo> list(Integer uid) {
//        //保存当前购物车商品的ID
//        Set<Integer> productIdSet = new HashSet<>();
//        boolean selectAll = true;
//        Integer cartTotalQuantity = 0;
//        BigDecimal cartTotalPrice = BigDecimal.ZERO;
//        CartVo cartVo = new CartVo();
//        List<CartProductVo> cartProductVoList = new ArrayList<>();
//        //当前购物车商品的数量
//        HashMap<Integer, Integer> quantityTotal = new HashMap<>();
//        //当前购物车是否选中
//        HashMap<Object, Boolean> isSelected = new HashMap<>();
//
//        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
//        //得到Redis 购物车id
//        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
//        // 获取变量中的键值对
//        Map<String, String> entries = opsForHash.entries(redisKey);
//
////        遍历购物车
//        for (Map.Entry<String, String> entry : entries.entrySet()) {
//            //得到id
//            Integer productId = Integer.valueOf(entry.getKey());
//            //得到value
//            Cart cart = gson.fromJson(entry.getValue(), Cart.class);
//            productIdSet.add(productId);
//            //当前购物车是否选中
//            isSelected.put(productId,cart.getProductSelected());
//            //当前购物车商品的数量
//            quantityTotal.put(productId,cart.getQuantity());
////            //TODO 需要优化，使用mysql里的in
////            Product product = productMapper.selectByPrimaryKey(productId);
////            if (product != null) {
////                //有MySQL 和 redis 的数据
////                CartProductVo cartProductVo = new CartProductVo(productId,
////                        cart.getQuantity(),
////                        product.getName(),
////                        product.getSubtitle(),
////                        product.getMainImage(),
////                        product.getPrice(),
////                        product.getStatus(),
////                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
////                        product.getStock(),
////                        cart.getProductSelected()
////                        );
////
////                cartProductVoList.add(cartProductVo);
////                //是否全选
////                if (!cart.getProductSelected()) {
////                    selectAll = false;
////                }
////
////                cartTotalQuantity += cart.getQuantity();
////
////
////            }
//        }
//        //保存ID
//        List<Product> products = productMapper.selectByProductIdSet(productIdSet);
//        //根据购物车的所有商品id查询出商品详情
//        for (Product product : products) {
//            if (product.getId() != null) {
//                CartProductVo cartProductVo = new CartProductVo(product.getId(),
//                        quantityTotal.get(product.getId()),
//                        product.getName(),
//                        product.getSubtitle(),
//                        product.getMainImage(),
//                        product.getPrice(),
//                        product.getStatus(),
//                        product.getPrice().multiply(BigDecimal.valueOf(quantityTotal.get(product.getId()))),
//                        product.getStock(),
//                        isSelected.get(product.getId()));
//
//                //查询后加入List
//                cartProductVoList.add(cartProductVo);
//                //购物车中商品总数量
//                cartTotalQuantity += cartProductVo.getQuantity();
//                //计算总价(只计算选中的)
//                if (isSelected.get(product.getId())) {
//                    cartTotalPrice =  cartTotalPrice.add(cartProductVo.getProductTotalPrice());
//                }
//                //是否全选
//                if (!isSelected.get(product.getId())) {
//                    selectAll = false;
//                }
//
//            }
//        }
//
//        //有一个没有选中，就不是全选
//        cartVo.setSelectedAll(selectAll);
//        //购物车总金额
//        cartVo.setCartTotalPrice(cartTotalPrice);
//        //购物车中商品总数量
//        cartVo.setCartTotalQuantity(cartTotalQuantity);
//        //将list加入购物车api
//        cartVo.setCartProductVoList(cartProductVoList);
//        return ResponseVo.success(cartVo);
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        String redisKey  = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);

        boolean selectAll = true;
        Integer cartTotalQuantity = 0;
        BigDecimal cartTotalPrice = BigDecimal.ZERO;
        CartVo cartVo = new CartVo();
        List<CartProductVo> cartProductVoList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            Integer productId = Integer.valueOf(entry.getKey());
            Cart cart = gson.fromJson(entry.getValue(), Cart.class);

            //TODO 需要优化，使用mysql里的in
            Product product = productMapper.selectByPrimaryKey(productId);
            if (product != null) {
                CartProductVo cartProductVo = new CartProductVo(productId,
                        cart.getQuantity(),
                        product.getName(),
                        product.getSubtitle(),
                        product.getMainImage(),
                        product.getPrice(),
                        product.getStatus(),
                        product.getPrice().multiply(BigDecimal.valueOf(cart.getQuantity())),
                        product.getStock(),
                        cart.getProductSelected()
                );
                cartProductVoList.add(cartProductVo);

                if (!cart.getProductSelected()) {
                    selectAll = false;
                }

                //计算总价(只计算选中的)
                if (cart.getProductSelected()) {
                    cartTotalPrice = cartTotalPrice.add(cartProductVo.getProductTotalPrice());
                }
            }

            cartTotalQuantity += cart.getQuantity();
        }

        //有一个没有选中，就不叫全选
        cartVo.setSelectedAll(selectAll);
        cartVo.setCartTotalQuantity(cartTotalQuantity);
        cartVo.setCartTotalPrice(cartTotalPrice);
        cartVo.setCartProductVoList(cartProductVoList);
        return ResponseVo.success(cartVo);
    }

    /**
     * 更新
     * @param uid
     * @param productId
     * @param form
     * @return
     */
    @Override
    public ResponseVo<CartVo> update(Integer uid, Integer productId, CartUpdateForm form) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //Redis中的表单名称
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        //redis中的value
        String value = opsForHash.get(redisKey, String.valueOf(productId));

        if (StringUtils.isEmpty(value)) {
            //没有该商品，报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //已经有了，修改内容
        Cart cart = gson.fromJson(value, Cart.class);//反序列化
        if (form.getQuantity() != null && form.getQuantity() >= 0) {
            cart.setQuantity(form.getQuantity());
        }
        if (form.getSelected() != null) {
            cart.setProductSelected(form.getSelected());
        }

        opsForHash.put(redisKey,String.valueOf(productId),gson.toJson(cart));
        return list(uid);
    }

    /**
     * 移除购物车某个产品
     * @param uid
     * @param productId
     * @return
     */
    @Override
    public ResponseVo<CartVo> delete(Integer uid, Integer productId) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //Redis中的表单名称
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        //redis中的value
        String value = opsForHash.get(redisKey, String.valueOf(productId));

        if (StringUtils.isEmpty(value)) {
            //没有该商品，报错
            return ResponseVo.error(ResponseEnum.CART_PRODUCT_NOT_EXIST);
        }
        //已经有了，修改内容
        opsForHash.delete(redisKey,String.valueOf(productId));
        return list(uid);
    }

    /**
     * 5.全选中
     * @param uid
     * @return
     */
    @Override
    public ResponseVo<CartVo> selectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //得到Redis 购物车id
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);
        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(true);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }
        return list(uid);
    }

    /**
     * 6.全不选中
     * @param uid
     * @return
     */
    @Override
    public ResponseVo<CartVo> unselectAll(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //得到Redis 购物车id
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);
        for (Cart cart : listForCart(uid)) {
            cart.setProductSelected(false);
            opsForHash.put(redisKey,String.valueOf(cart.getProductId()),gson.toJson(cart));
        }
        return list(uid);
    }

    /**
     * 7.获取购物中所有商品数量总和
     * @param uid
     * @return
     */
    @Override
    public ResponseVo<Integer> sum(Integer uid) {
        Integer sum = listForCart(uid).stream()
                .map(Cart::getQuantity)
                .reduce(0, Integer::sum);
        return ResponseVo.success(sum);
    }

    public List<Cart> listForCart(Integer uid) {
        HashOperations<String, String, String> opsForHash = redisTemplate.opsForHash();
        //得到Redis 购物车id
        String redisKey = String.format(CART_REDIS_KEY_TEMPLATE, uid);
        Map<String, String> entries = opsForHash.entries(redisKey);
        List<Cart> cartList = new ArrayList<>();
        for (Map.Entry<String, String> entry : entries.entrySet()) {
            cartList.add(gson.fromJson(entry.getValue(),Cart.class));
        }
        return cartList;
    }
}
