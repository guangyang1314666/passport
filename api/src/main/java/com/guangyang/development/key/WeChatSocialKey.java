package com.guangyang.development.key;

import java.io.Serializable;

/**
 * 微信用户信息，在缓存中的key
 */
public class WeChatSocialKey implements Serializable {
    /**
     * 使用唯一标识作为用户信息的key
     */
    private static String uuidKey = "wechat_social:uuid:";

    public static String getUuidKey(String uuid) {
        return uuidKey + uuid;
    }
}
