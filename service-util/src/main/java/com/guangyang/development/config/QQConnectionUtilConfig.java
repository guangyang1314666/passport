package com.guangyang.development.config;

import com.guangyang.development.utils.QQConnectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 该类会将工具类整合到spring中，参数在配置文件中进行配置
 * @author 沈光阳
 * @create 2020-02-18-7:54
 */
@Configuration
public class QQConnectionUtilConfig {

    public static final Logger logger = LoggerFactory.getLogger(QQConnectionUtilConfig.class);
    /** 申请QQ登录成功后，分配给应用的appid*/
    @Value("${qqlogin.appId:0}")
    public String appId;

    /** 申请QQ登录成功后，分配给网站的appkey */
    @Value("${qqlogin.appKey:0}")
    public String appKey;

    /** 用户登录成功后的回调地址 */
    @Value("${qqlogin.redirectURI:0}")
    public String redirectURI;

    @Bean
    public QQConnectionUtil getQQConnectionUtil(){
        if (appId.equals("0") && appKey.equals("0") && redirectURI.equals("0")) {
            logger.error("没有设置appId或者appKey或者redirectURI");
            return null;
        }else {
            QQConnectionUtil qqConnectionUtil = new QQConnectionUtil(appId,appKey,redirectURI);
            return qqConnectionUtil;
        }
    }
}
