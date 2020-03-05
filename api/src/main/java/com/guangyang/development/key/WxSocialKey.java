package com.guangyang.development.key;

/**
 * 管理微信用户，存储在缓存中的key
 *
 */
public class WxSocialKey {
    /**
     * 以uuid用户登录唯一标识作为键
     */
    private static String uuidKey = "wx_social:uuid:";
    private static String openidKey = "wx_social:openid:";
    private static String returnUrlKey = "wx_social:return_url:";
    private static String stateKey = "wx_social:state:";

    public static String getUuidKey(String uuid){
        return uuidKey + uuid;
    }

    public static String getOpenidKey(String uuid){
        return openidKey + uuid;
    }

    public static String getReturnUrlKey(String uuid){
        return returnUrlKey + uuid;
    }

    public static String getStateKey(String state){
        return stateKey + state;
    }
}
