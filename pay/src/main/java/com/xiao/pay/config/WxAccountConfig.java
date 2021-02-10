package com.xiao.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/9 11:18
 */
@Component //在Spring IOC容器的实例化方法时执行
@ConfigurationProperties(prefix = "wx")
@Data
public class WxAccountConfig {
    private String appId;
    private String mchId;
    private String mchKey;
    private String notifyUrl;
    private String returnUrl;
}
