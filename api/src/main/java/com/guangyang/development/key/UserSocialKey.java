package com.guangyang.development.key;

/**
 * 操作UserSocial
 */
public class UserSocialKey {

    /**
     * 以openid做主键存UserSocial对象
     */
    private static String openIdKey = "user_social:open_id:";


    public static String getOpenIdKey(String openId) {
        return openIdKey + openId;
    }

}
