package com.xiao.mall.service.impl;

import com.github.pagehelper.PageInfo;
import com.xiao.mall.dao.OrderItemMapper;
import com.xiao.mall.dao.OrderMapper;
import com.xiao.mall.dao.ProductMapper;
import com.xiao.mall.dao.ShippingMapper;
import com.xiao.mall.enums.OrderStatusEnum;
import com.xiao.mall.enums.PaymentTypeEnum;
import com.xiao.mall.enums.ProductStatusEnum;
import com.xiao.mall.enums.ResponseEnum;
import com.xiao.mall.pojo.*;
import com.xiao.mall.service.ICartService;
import com.xiao.mall.service.IOrderService;
import com.xiao.mall.vo.OrderItemVo;
import com.xiao.mall.vo.OrderVo;
import com.xiao.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author ：降蓝
 * @description：订单业务层实现类
 * @date ：2021/2/17 17:21
 */
@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Autowired
    private ICartService cartService;

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private OrderItemMapper orderItemMapper;

    /**
     * 创建订单
     *
     * @param uid        用户id
     * @param shippingId 购物车id
     * @return
     */
    @Override
    @Transactional
    public ResponseVo<OrderVo> create(Integer uid, Integer shippingId) {
        //1.收货地址校验(总之要查出来的)
        Shipping shipping = shippingMapper.selectByUidAndShippingId(uid, shippingId);
        if (shipping == null) {
            return ResponseVo.error(ResponseEnum.SHIPPING_NOT_EXIST);
        }
        //TODO cartList是什么格式
        //2.1获取购物车，校验(是否有商品、库存)
        List<Cart> cartList = cartService.listForCart(uid).stream()
                .filter(Cart::getProductSelected)
                .collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cartList)) {
            return ResponseVo.error(ResponseEnum.CART_SELECTED_IS_EMPTY);
        }

        //2.2获取cartList里的productIds
        Set<Integer> productIdSet = cartList.stream()
                .map(Cart::getProductId)
                .collect(Collectors.toSet());
        //从数据库中获得数据
        List<Product> productList = productMapper.selectByProductIdSet(productIdSet);
        //将数据以productId,product的格式存入map
        Map<Integer, Product> map = productList.stream()
                .collect(Collectors.toMap(Product::getId, product -> product));

        List<OrderItem> orderItemList = new ArrayList<>();
        Long orderNo = generateOrderNo();


        //购物车的内容(增强for循环是判断的基础)
        for (Cart cart : cartList) {
            //根据 productId 查数据库
            Product product = map.get(cart.getProductId());
            //是否有商品
            if (product == null)
                return ResponseVo.error(ResponseEnum.PRODUCT_NOT_EXIST, "商品不存在. productId = " + cart.getProductId());
            //商品上下架状态
            if (!ProductStatusEnum.ON_SALE.getCode().equals(product.getStatus()))
                return ResponseVo.error(ResponseEnum.PRODUCT_OFF_SALE_OR_DELETE, "商品不是在售状态"+ product.getName());
            //库存是否充足
            if (product.getStock() < cart.getQuantity())
                return ResponseVo.error(ResponseEnum.PRODUCT_STOCK_ERROR, "库存不正确." + product.getName());

            OrderItem orderItem = buildOrderItem(uid, orderNo, cart.getQuantity(), product);
            orderItemList.add(orderItem);

            //减库存
            product.setStock(product.getStock() - cart.getQuantity());
            int row = productMapper.updateByPrimaryKeySelective(product);
            if (row <= 0)
                return ResponseVo.error(ResponseEnum.ERROR); 
        }

        //计算总价,只计算选中的商品
        //生成订单，入库：order和order_item,事务
        Order order = buildOrder(uid, orderNo, shippingId, orderItemList);
        int rowForOder = orderMapper.insertSelective(order);
        if (rowForOder <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }

        int rowForOrderItem = orderItemMapper.batchInsert(orderItemList);

        if (rowForOrderItem <= 0) {
            return ResponseVo.error(ResponseEnum.ERROR);
        }
        //构造orderVo
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);

        //更新购物车(选中的商品)
        //Redis有事务(打包命令)
        for (Cart cart : cartList) {
            cartService.delete(uid, cart.getProductId());
        }

        return ResponseVo.success(orderVo);
    }

    /**
     * 订单List
     *
     * @param uid      用户id
     * @param pageNum  页码
     * @param pageSize 每页记录条数
     * @return
     */
    @Override
    public ResponseVo<PageInfo> list(Integer uid, Integer pageNum, Integer pageSize) {
        //订单表
        List<Order> orderList = orderMapper.selectByUid(uid);
        //根据订单id查出订单明细
        Set<Long> orderNoSet = orderList.stream()
                .map(Order::getOrderNo)
                .collect(Collectors.toSet());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Map<Long, List<OrderItem>> orderItemMap = orderItemList.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderNo));
        //根据订单表中的地址id查询地址
        Set<Integer> shippingIdSet = orderList.stream()
                .map(Order::getShippingId)
                .collect(Collectors.toSet());
        List<Shipping> shippingList = shippingMapper.selectByIdSet(shippingIdSet);
        Map<Integer, Shipping> shippingMap = shippingList.stream()
                .collect(Collectors.toMap(Shipping::getId,shipping -> shipping));


        List<OrderVo> orderVoList = new ArrayList<>();
        for (Order order : orderList) {
            OrderVo orderVo = buildOrderVo(order,
                    orderItemMap.get(order.getOrderNo()),
                    shippingMap.get(order.getShippingId()));
            orderVoList.add(orderVo);
        }
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVoList);
        return ResponseVo.success(pageInfo);
    }

    /**
     * 订单详情
     *
     * @param uid     用户id
     * @param orderNo 订单号
     * @return
     */
    @Override
    public ResponseVo<OrderVo> detail(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        Set<Long> orderNoSet = new HashSet<>();
        orderNoSet.add(order.getOrderNo());
        List<OrderItem> orderItemList = orderItemMapper.selectByOrderNoSet(orderNoSet);
        Shipping shipping = shippingMapper.selectByPrimaryKey(order.getShippingId());
        OrderVo orderVo = buildOrderVo(order, orderItemList, shipping);
        return ResponseVo.success(orderVo);
    }

    /**
     * 订单取消
     *
     * @param uid     用户id
     * @param orderNo 订单号
     * @return
     */
    @Override
    public ResponseVo cancel(Integer uid, Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null || !order.getUserId().equals(uid)) {
            return ResponseVo.error(ResponseEnum.ORDER_NOT_EXIST);
        }
        //只有[未付款]订单可以取消
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode()))
            return ResponseVo.error(ResponseEnum.ORDER_STATUS_ERROR);

        order.setStatus(OrderStatusEnum.CANCELED.getCode());
        order.setCloseTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0) {
            return  ResponseVo.error(ResponseEnum.ERROR);
        }
        return ResponseVo.success();
    }

    @Override
    public void paid(Long orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null ) {
            throw new RuntimeException(ResponseEnum.ORDER_NOT_EXIST.getDesc() + "订单id" + orderNo);
        }
        //只有[未付款]订单可以变成已付款
        if (!order.getStatus().equals(OrderStatusEnum.NO_PAY.getCode()))
            throw new RuntimeException(ResponseEnum.ORDER_STATUS_ERROR.getDesc());

        order.setStatus(OrderStatusEnum.PAID.getCode());
        order.setPaymentTime(new Date());
        int row = orderMapper.updateByPrimaryKeySelective(order);
        if (row <= 0) {
            throw new RuntimeException("将订单更新为已支付状态失败，订单id" + orderNo);
        }
    }

    private OrderVo buildOrderVo(Order order, List<OrderItem> orderItemList, Shipping shipping) {
        OrderVo orderVo = new OrderVo();
        BeanUtils.copyProperties(order, orderVo);

        List<OrderItemVo> orderItemVoList = orderItemList.stream().map(e -> {
            OrderItemVo orderItemVo = new OrderItemVo();
            BeanUtils.copyProperties(e, orderItemVo);
            return orderItemVo;
        }).collect(Collectors.toList());

        orderVo.setOrderItemVoList(orderItemVoList);

        if (shipping != null) {
            orderVo.setShippingId(shipping.getId());
            orderVo.setShippingVo(shipping);
        }


        return orderVo;
    }

    private Order buildOrder(Integer uid, Long orderNo, Integer shippingId, List<OrderItem> orderItemList) {
        BigDecimal payment = orderItemList.stream().map(OrderItem::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = new Order();
        order.setOrderNo(orderNo);
        order.setUserId(uid);
        order.setShippingId(shippingId);
        order.setPayment(payment);
        order.setPaymentType(PaymentTypeEnum.PAY_ONLINE.getCode());
        order.setPostage(0);
        order.setStatus(OrderStatusEnum.NO_PAY.getCode());

        return order;
    }

    /**
     * 生成orderNO
     * 时间 + 随机数
     *
     * @return
     */
    private Long generateOrderNo() {
        return System.currentTimeMillis() + new Random().nextInt(999);
    }


    /**
     * 订单明细表
     *
     * @param uid
     * @param orderNO
     * @param quantity
     * @param product
     * @return
     */
    private OrderItem buildOrderItem(Integer uid, Long orderNO, Integer quantity, Product product) {
        OrderItem item = new OrderItem();
        item.setUserId(uid);
        item.setOrderNo(orderNO);
        item.setProductId(product.getId());
        item.setProductName(product.getName());
        item.setProductImage(product.getMainImage());
        item.setCurrentUnitPrice(product.getPrice());
        item.setQuantity(quantity);
        item.setTotalPrice(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
        return item;
    }
}
