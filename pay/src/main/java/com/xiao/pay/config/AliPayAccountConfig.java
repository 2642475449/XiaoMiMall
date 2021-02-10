package com.xiao.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.swing.*;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/9 12:51
 */

@ConfigurationProperties(prefix = "alipay")
@Component
@Data
public class AliPayAccountConfig {
    private String appId;
    private String privateKey;
    private String publicKey;
    private String notifyUrl;
    private String returnUrl;
}
