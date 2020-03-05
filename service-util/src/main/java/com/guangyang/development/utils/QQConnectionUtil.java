package com.guangyang.development.utils;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.guangyang.development.social.Qq;
import org.apache.commons.lang3.StringUtils;
import org.apache.zookeeper.Op;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

/**
 * 获取授权码的url = https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101852934&redirect_uri=http://www.passport.com/qqLogin/callback&state=12345
 * @author 沈光阳
 * @version 2.0
 * @create 2020-02-17-18:14
 */
public class QQConnectionUtil {
    public static final Logger logger = LoggerFactory.getLogger(QQConnectionUtil.class);
    private static ObjectMapper objectMapper = new ObjectMapper();

    /** 申请QQ登录成功后，分配给应用的appid*/
    private String appId;
    /** 申请QQ登录成功后，分配给网站的appkey */
    private String appKey;
    /** 用户登录成功后的回调地址 */
    private String redirectURI;

    public QQConnectionUtil(){

    }

    public QQConnectionUtil(String appId, String appKey, String redirectURI) {
        this.appId = appId;
        this.appKey = appKey;
        this.redirectURI = redirectURI;
    }

    /**
     * 获取code的url
     * @param state 用于第三方应用防止CSRF攻击,
     * @param uuid uuid用作用户登录的唯一标识
     */
    public String getCodeUrl(String state,String uuid){
        StringBuilder getCodeUrl = new StringBuilder("https://graph.qq.com/oauth2.0/authorize?");

        getCodeUrl.append("response_type=code")
                .append("&client_id=").append(appId)
                .append("&state=").append(state)
                .append("&redirect_uri=").append(redirectURI);

        if (StringUtils.isNotBlank(uuid)) {
            getCodeUrl.append("?uuid=").append(uuid);
        }
        return getCodeUrl.toString();
    }

    /**
     * 根据授权码获取AccessToken,返回的是一个QQ对象
     * @param code 授权码
     */
    public String getAccessToken(String code){
        StringBuilder url = new StringBuilder("https://graph.qq.com/oauth2.0/token?");

        url.append("grant_type=authorization_code")
                .append("&client_id=").append(appId)
                .append("&client_secret=").append(appKey)
                .append("&redirect_uri=").append(redirectURI)
                .append("&code=").append(code);

        // 我们只需要access_token
        logger.debug("获取AccessToken的url = " + url.toString());

        String returnDate = HttpclientUtil.doGetString(url.toString());

        int beginIndex = returnDate.indexOf("=") + 1;
        int endIndex = returnDate.indexOf("&");
        String accessToken = returnDate.substring(beginIndex,endIndex);
        logger.debug("获取AccessToken时返回的数据 = " + accessToken);

        return accessToken;
    }

    /**
     * 根据token获取用户openid
     * @param AccessToken 使用授权码换取的token
     * @return
     */
    public String getOpenId(String AccessToken) throws JsonProcessingException {
        String url = "https://graph.qq.com/oauth2.0/me?access_token=ACCESS_TOKEN";
        url = url.replaceAll("ACCESS_TOKEN",AccessToken);

        // 获取用户openId返回的包 = callback( {"client_id":"101852934","openid":"79A161A5EF248B94AF0774455B38F79B"} );
        String callbackData = HttpclientUtil.doGetString(url);

        // 截取openId
        int beginIndex = callbackData.indexOf("callback( ") + 10 ;
        int endIndex = callbackData.indexOf(")");
        // 截取为json
        String returnJson = callbackData.substring(beginIndex,endIndex);
        // 读取json字符串
        JsonNode jsonNode = objectMapper.readTree(returnJson);
        return jsonNode.get("openid").asText();
    }

    /**
     * 使用通过token换取的openId对换用户信息
     * @param OpenId
     * @return
     */
    public Qq getUserInfo(String AccessToken , String OpenId) throws JsonProcessingException {
        StringBuilder url = new StringBuilder("https://graph.qq.com/user/get_user_info?");

        url.append("access_token=").append(AccessToken)
                .append("&oauth_consumer_key=").append(appId)
                .append("&openid=").append(OpenId);
        String qqUserInfo = HttpclientUtil.doGetString(url.toString());
        Qq qq = JSON.parseObject(qqUserInfo, Qq.class);
        return qq;
    }

    public static void main(String[] args) throws JsonProcessingException {
        // Step1： 获取AuthorizationCode
//        QQConnectionUtil qqConnectionUtil = new QQConnectionUtil();
//        String codeUrl = qqConnectionUtil.getCodeUrl("1343124123", UUID.randomUUID().toString().replaceAll("-", ""));
//        System.out.println("codeUrl=" + codeUrl);

//        Qq qqLoginInfo = qqConnectionUtil.getQqLoginInfo("A03EA6D1B065D945808A727E3CA6E43B");
//        System.out.println(qqLoginInfo.toString());


//
//        // Step2: 通过Authorization Code获取Access Token(C628C60D058B5EA8208B392598FA4C0C)
//        String accessToken = qqConnectionUtil.getAccessToken("48E1462A4B9A2AAF3603F7475201F9FA");
//        System.out.println("accessToken=" + accessToken);


        // Step3: 获取用户OpenID
//        String openId = getOpenId("C628C60D058B5EA8208B392598FA4C0C");
//        System.out.println("获取用户openId返回的包 = " + openId);
//        // Step3: 获取用户信息
//        JsonNode userInfo = getUserInfo("C628C60D058B5EA8208B392598FA4C0C", openId);
//        System.out.println(userInfo.get("nickname").asText());
    }
}
