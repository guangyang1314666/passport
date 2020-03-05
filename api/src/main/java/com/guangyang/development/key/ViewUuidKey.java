package com.guangyang.development.key;

/**
 * 缓存中的key
 */
public class ViewUuidKey {
    private static String qqBindUserKey = "qq:bind_user_uuid:";
    private static String wechatBindUserKey = "qq:bind_user_uuid:";

    public static String getQqBindUserKey(String uuid) {
        return qqBindUserKey + uuid;
    }

    public static String getWechatBindUserKey(String uuid) {
        return wechatBindUserKey + uuid;
    }
}
