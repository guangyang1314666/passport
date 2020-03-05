package com.guangyang.development.bean;

/**
 * 常量类
 */
public final class Constant {
    /**
     * code + CLIENT_ID + CLIENT_SECRET 换取access_token
     */
    public static final String CLIENT_ID = "2680402628";
    public static final String CLIENT_SECRET = "ccfa8b9ec81964fb6fc4e5b365bbf52e" ;

    /**盐值*/
    public static final String SALT = "";
    /**服务器key*/
    public static final String KEY = "2019gmall0129";

    /**
     * 用户登录来源
     * 0 表示本网站登录
     * 1 表示微博登录
     * 2 表示qq登录
     */
    public static final String GMALL_LOGIN = "0";
    public static final String V_LOGIN = "1";
    public static final String QQ_LOGIN = "2";

    /**
     * 表示男还是女
     * 性别，m：男、f：女、n：未知
     */
    public static final Integer GENDER_M = 0;
    public static final Integer GENDER_F = 1;
    public static final Integer GENDER_N = 2;
}
