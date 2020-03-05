package com.guangyang.development.key;

/**
 * 微博开放平台数据，存在缓存中的key管理
 */
public class WeiBoSocialKey {
    private static String stateKey = "weibo:";
    private static String uuidKey = "weibo_social:uuid:";

    public static String getStateKey(String state) {
        return stateKey + state;
    }

    public static String getUuidKey(String uuid) {
        return uuidKey + uuid;
    }
}
