package com.guangyang.development.bean;

public class CacheKeyBuilder {

    /**
     * 微信登录时，state的key,后面拼接的是state
     */
    public static final String WX_LOGIN_STATE_KEY = "wx:state";

    /**
     * 微信登录时候，openid，后面拼接的是openid
     */
    public static final String WX_LOGIN_OPENID = "wx:openid";

    /**
     * 微信用户信息的key,后面拼接的是uuid
     */
    public static final String WX_USER_INFO_KEY = "wx:userinfo";

    /**
     * 用户信息的key,后面拼接的是用户名，对应到value是用户对象的信息
     */
    public static final String USER_INFO_KEY = "user:username";

    /**
     * 微信openid将被存到缓存中，结尾拼接uuid
     */
    public static final String WX_OPENID_KEY = "wx:openid";

    /**
     * 用户进入登录页面的原始地址，结尾拼接uuid用户登录的唯一标识
     */
    public static final String RETURN_URL_KEY = "returnurl:state";

    /**
     * 用户token,结尾拼接用户id，再拼接token值
     */
    public static final String USER_TOKEN = "user:token";

    /**
     * 获取key
     * @param format
     * @param data
     * @return
     */
    public static String getKey(String format,String... data){
        StringBuilder stringBuilder = new StringBuilder();
        StringBuilder key = stringBuilder.append(format);

        for (String str : data) {
            key.append(":").append(str);
        }

        return key.toString();
    }

    public static void main(String[] args) {
        String key = CacheKeyBuilder.getKey(CacheKeyBuilder.WX_LOGIN_STATE_KEY, "123");
        String key1 = CacheKeyBuilder.getKey(CacheKeyBuilder.WX_LOGIN_STATE_KEY, "123");
        String key3 = CacheKeyBuilder.getKey(CacheKeyBuilder.WX_LOGIN_OPENID, "23424dftgwert4rt34r");
        String openIdKey = CacheKeyBuilder.getKey(CacheKeyBuilder.WX_LOGIN_OPENID,"123123");
        System.out.println("openIdKey=" + openIdKey);
        System.out.println(key);
        System.out.println(key1);
        System.out.println(key3);

        String openIdKey1 = CacheKeyBuilder.getKey(CacheKeyBuilder.WX_LOGIN_OPENID,"123123","hahahha");
        System.out.println(openIdKey1);
    }
}
