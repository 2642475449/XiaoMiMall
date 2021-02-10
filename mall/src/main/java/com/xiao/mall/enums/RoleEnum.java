package com.xiao.mall.enums;

import lombok.Getter;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/9 17:01
 */
@Getter
public enum  RoleEnum {
    ADMIN(0),
    CUSTOMER(1),
    ;
    Integer  Code;

    RoleEnum(Integer code) {
        Code = code;
    }
}
