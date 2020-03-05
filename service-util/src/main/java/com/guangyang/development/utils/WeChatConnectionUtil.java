package com.guangyang.development.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * 微信互联工具类
 */
public class WeChatConnectionUtil {
    /**
     * 公众号的appid
     */
    private String appid;

    /**
     * 公众号的秘钥
     */
    private String appsecret;

    /**
     * 开放平台的appid
     */
    private String openAppid;

    /**
     * 开放平台的appsecret(秘钥)
     */
    private String openAppsecret;

    /**
     * 开放平台回调url
     */
    private String openRedirectUrl;

    /**
     * 请求code URL，该链接会调整到用户登录授权界面
     */
    private final String WECHAT_REDIRECT_URL = "https://open.weixin.qq.com/connect/qrconnect?appid=%s&response_type=code&scope=snsapi_login&state=%s&redirect_uri=%s";

    /**
     * 通过code获取access_token的地址
     */
    private final String GET_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";

    private final String GET_USER_INFO_URL = "https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s";

    public WeChatConnectionUtil(String appid, String appsecret, String openAppid, String openAppsecret, String openRedirectUrl) {
        this.appid = appid;
        this.appsecret = appsecret;
        this.openAppid = openAppid;
        this.openAppsecret = openAppsecret;
        this.openRedirectUrl = openRedirectUrl;
    }

    /**
     * 获取code的urL
     * @param state
     * @return
     */
    public String getCodeUrl(String state) throws UnsupportedEncodingException {
        // 进行编码
        String callbackUrl = URLEncoder.encode(openRedirectUrl, "utf-8");
        // 格式化字符串，一定要将callbackUrl放在最后，因为后期要拼接一个参数
        String getCodeUrl = String.format(WECHAT_REDIRECT_URL, openAppid, state,callbackUrl);
        return getCodeUrl;
    }

    /**
     * 通过code获取access_token
     * 返回的map中包含很多字段
     * {
     *      "access_token":"ACCESS_TOKEN",
     *      "expires_in":7200,
     *      "refresh_token":"REFRESH_TOKEN",
     *      "openid":"OPENID",
     *      "scope":"SCOPE",
     *      "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     * @param code
     * @return
     */
    public Map<String,Object> getAccessToken(String code){
        String getAccessTokenUrl = String.format(GET_ACCESS_TOKEN_URL, this.openAppid, this.openAppsecret, code);
        Map<String, Object> baseMap = HttpclientUtil.doGet(getAccessTokenUrl);
        return baseMap;
    }

    /**
     * 获取用户信息
     * @param code
     * @return
     */
    public Map<String,Object> getUserInfo(String code){
        // 通过code获取access_token
        Map<String, Object> baseMap = getAccessToken(code);

        // 通过access_token和openid获取用户信息
        String getUserInfoUrl = String.format(GET_USER_INFO_URL, baseMap.get("access_token"), baseMap.get("openid"));
        Map<String, Object> userInfoMap = HttpclientUtil.doGet(getUserInfoUrl);
        return userInfoMap;
    }
}
