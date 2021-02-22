package com.xiao.mall.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/17 16:40
 */
@Data
public class ShippingFormUpdate {

    private String receiverName;

    private String receiverPhone;

    private String receiverMobile;

    private String receiverProvince;

    private String receiverCity;

    private String receiverDistrict;

    private String receiverAddress;

    private String receiverZip;
}
