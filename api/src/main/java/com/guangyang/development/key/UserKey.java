package com.guangyang.development.key;

/**
 * 操作保存user对象的键
 */
public class UserKey {
    /**
     * 以openid做主键存UserSocial对象
     */
    private static String openIdKey = "user:open_id:";
    private static String idKey = "user:id:";
    private static String usernameKey = "user:username:";


    public static String getOpenIdKey(String openId) {
        return openIdKey + openId;
    }

    public static String getIdKey(String id) {
        return idKey + id;
    }

    public static String getUsernameKey(String username) {
        return usernameKey + username;
    }
}
