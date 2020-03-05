package com.guangyang.development.key;

/**
 * QQ互联，key管理(缓存中的key)
 */
public class QqSocialKey {
    private static String uuidKey = "qq_social:uuid:";

    public static String getUuidKey(String uuid) {
        return uuidKey + uuid;
    }
}
