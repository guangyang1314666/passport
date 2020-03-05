package com.guangyang.development.config;

import com.guangyang.development.utils.WeChatConnectionUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value="classpath:application.properties")
public class WeChatConnectionUtilConfig {
    /**
     * 公众号的appid
     */
    @Value("${wxpay.appid}")
    private String appid;

    /**
     * 公众号的秘钥
     */
    @Value("${wxpay.appsecret}")
    private String appsecret;

    /**
     * 开放平台的appid
     */
    @Value("${wxopen.appid}")
    private String openAppid;

    /**
     * 开放平台的appsecret
     */
    @Value("${wxopen.appsecret}")
    private String openAppsecret;

    /**
     * 开放平台回调url
     */
    @Value("${wxopen.redirect_url}")
    private String openRedirectUrl;

    @Bean
    public WeChatConnectionUtil getWeChatConnectionUtil(){
        return new WeChatConnectionUtil(appid,appsecret,openAppid,openAppsecret,openRedirectUrl);
    }
}
