package com.guangyang.development.config;

import com.alibaba.druid.sql.visitor.functions.If;
import com.guangyang.development.utils.WeiBoConnectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 将微博登录整合到spingboot中
 * @version 1.0
 */
@Configuration
@PropertySource(value="classpath:application.properties")
public class WeiBoConnectionUtilConfig {
    public static Logger logger = LoggerFactory.getLogger(WeiBoConnectionUtil.class);

    /**
     * 申请应用时分配的AppKey。
     */
    @Value("${weibo.app_key}")
    private String appKey;

    /**
     * 授权回调地址，站外应用需与设置的回调地址一致
     */
    @Value("${weibo.redirect_uri}")
    private String redirectUri;

    /**
     * 申请应用时分配的AppSecret。
     */
    @Value("${weibo.app_secrect}")
    private String appSecrect;

    @Bean
    public WeiBoConnectionUtil getWeiBoConnectionUtil(){

        if (StringUtils.isBlank(appKey) || StringUtils.isBlank(appSecrect) || StringUtils.isBlank(redirectUri)) {
            logger.error("你的微博配置不全, appKey = " + appKey + "-------redirect_uri=" + redirectUri
            + "----------appSecrect=" + appSecrect);
        }
        return new WeiBoConnectionUtil(appKey,redirectUri,appSecrect);
    }
}
