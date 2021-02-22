package com.xiao.mall.enums;


import lombok.Getter;

/**
 * 响应枚举
 */
@Getter
public enum ResponseEnum {

    ERROR(-1,"服务端错误"),
    SUCCESS(0,"成功"),
    PASSWORD_ERROR(1,"密码错误"),
    USERNAME_EXIST(2,"用户名已存在"),
    PARAM_ERROR(3,"参数错误"),
    EMAIL_EXIST(4,"邮箱已存在"),
    NEED_LOGIN(10,"用户未登录，请先登录"),
    USERNAME_OR_PASSWORD_ERROR(11,"用户名或密码错误"),
    PRODUCT_OFF_SALE_OR_DELETE(12,"商品下架或删除"),
    PRODUCT_NOT_EXIST(13,"商品不存在"),
    PRODUCT_STOCK_ERROR(14,"库存不正确"),
    CART_PRODUCT_NOT_EXIST(15,"购物车中无此商品"),
    DELETE_SHIPPING_FAIL(16,"删除收货地址失败"),
    DELETE_SHIPPING_SUCCESSFUL(17,"删除收货地址成功"),
    NEW_ADDRESS_SUCCESSFUL(18,"新建地址成功"),
    NEW_ADDRESS_SUCCESSFUL_FAIL(19,"新建地址失败"),
    UPDATE_ADDRESS_SUCCESSFUL(20,"更新地址成功"),
    QUERY_FAIL(21,"请登录之后查询"),
    SHIPPING_NOT_EXIST(22,"收货地址不存在"),
    CART_SELECTED_IS_EMPTY(23,"请选择商品后下单"),
    ORDER_NOT_EXIST(19,"订单不存在"),
    ORDER_STATUS_ERROR(20,"订单状态有误"),

    ;
    Integer code;

    String desc;

    ResponseEnum(Integer code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
