package com.guangyang.development.utils;

import com.alibaba.fastjson.JSON;
import com.guangyang.development.social.Weibo;
import org.apache.commons.lang3.StringUtils;

import javax.sound.midi.Soundbank;
import java.sql.SQLOutput;
import java.util.Map;
import java.util.Optional;

/**
 * 微博互联工具类
 * @version 1.0
 */
public class WeiBoConnectionUtil {

    /**
     * 申请应用时分配的AppKey。
     */
    private String appKey;

    /**
     * 授权回调地址，站外应用需与设置的回调地址一致
     */
    private String redirectUri;

    /**
     * 申请应用时分配的AppSecret。
     */
    private String appSecrect;

    public WeiBoConnectionUtil() {
    }

    public WeiBoConnectionUtil(String appKey, String redirectUri, String appSecrect) {
        this.appKey = appKey;
        this.redirectUri = redirectUri;
        this.appSecrect = appSecrect;
    }

    /**
     * 获取code的url
     * @param state 这个参数可用于防止跨站请求伪造（CSRF）攻击,同时作为本网站的用户登录唯一标识
     * @return
     */
    public String getCodeUrl(String state){
        StringBuilder url = new StringBuilder("https://api.weibo.com/oauth2/authorize?");

        url.append("client_id=").append(appKey)
                .append("&state=").append(state)
                .append("&redirect_uri=").append(redirectUri);

        return url.toString();
    }

    /**
     * 获取AccessToken
     * 返回的对象中，包含 access_token和 uid属性
     * @param code
     * @return
     */
    public Weibo getAccessToken(String code){
        StringBuilder url = new StringBuilder("https://api.weibo.com/oauth2/access_token?");

        url.append("client_id=").append(appKey)
                .append("&client_secret=").append(appSecrect)
                .append("&grant_type=authorization_code")
                .append("&code=").append(code)
                .append("&redirect_uri=").append(redirectUri);

        Map<String, Object> returnData = HttpclientUtil.doPost(url.toString(), null);

        return JSON.parseObject(JSON.toJSONString(returnData),Weibo.class);
    }

    /**
     * 获取用户信息
     * @return
     */
    public Weibo getUserInfo(Weibo weibo){
        // java8特性，判断空值
        Optional<Weibo> weiboOpt = Optional.ofNullable(weibo);
        String accessToken = weiboOpt.map(Weibo::getAccess_token).orElse("Access_token为空，原因weibo对象为空");
        String uid =  weiboOpt.map(Weibo::getUid).orElse("123456");

        StringBuilder url = new StringBuilder("https://api.weibo.com/2/users/show.json?");
        url.append("access_token=").append(accessToken)
                .append("&uid=").append(uid);

        Map<String, Object> returnData = HttpclientUtil.doGet(url.toString());
        Weibo weibo1 = JSON.parseObject(JSON.toJSONString(returnData), Weibo.class);

        Optional<Weibo> weiboOpt1 = Optional.ofNullable(weibo1);
        weiboOpt1.ifPresent(weibo2 -> {
            weibo2.setState(weibo.getState());
            weibo2.setReturnUrl(weibo.getReturnUrl());
            weibo2.setAccess_token(weibo.getAccess_token());
            weibo2.setUid(weibo.getUid());
        });

        return Optional.ofNullable(weibo1).orElse(new Weibo());
    }

    public static void main(String[] args) {
        // 1、获取授权url
        WeiBoConnectionUtil weiBoConnectionUtil = new WeiBoConnectionUtil();
        String codeUrl = weiBoConnectionUtil.getCodeUrl("123124124");
        System.out.println("codeUrl= " + codeUrl);

        // 2. 获取access_token
//        Weibo accessToken = weiBoConnectionUtil.getAccessToken("e7f9ee51d1d2fe9d090220c92146a38f");
//        System.out.println(accessToken);
//
//        Weibo userInfo = weiBoConnectionUtil.getUserInfo(accessToken);
//        System.out.println(userInfo.toString());
    }
}
