package com.guangyang.development.social;

import com.guangyang.development.bean.User;

import java.util.Map;

/**
 * 社交登录缓存工具类
 * T:传入的是一个用户社交登录对象
 */
public interface SocialCacheHelper<T> {

    /**
     * 保存用户社交登录信息到缓存中
     * @param t 社交登录实体类
     * @param openid 社交平台的openid
     */
    void saveUserSocial(T t, String openid);

    /**
     * 从缓存中获取用户社交登录信息
     * @param openid
     * @return
     */
    T getUserSocial(String openid);

    /**
     * 保存开放平台的用户信息到缓存中
     * @param code 开放平台的授权码
     * @param uuid 用户登录的唯一标识
     */
    void saveSocialUserInfo(String code,String uuid);

    /**
     * 通过用户登录唯一标识删除社交用户的信息
     * @param uuid 用户使用社交登录的唯一标识
     */
    void deleteSocialUserInfo(String uuid);

    /**
     * 保存开放平台的openid到缓存中
     * @param openId 开放平台的openid
     * @param uuid 用户登录过程的唯一标识
     */
    void saveOpenId(String openId , String uuid);

    /**
     * 从缓存中获取社交平台用户信息
     * @param uuid 用户登录过程的唯一标识
     * @return
     */
    Map<String,Object> getSocialUserInfo(String uuid);

    /**
     * 通过用户名从缓存中查找用户，查询的是user表
     * @param username
     * @return
     */
    Map<String,Object> getUserByUsername(String username);

    /**
     * 保存用户到缓存中，保存的是User表
     * @param user
     */
    void saveUserByUsername(User user);

    /**
     * 获取开放平台的openid
     * @param uuid 用户登录的唯一标识
     * @return
     */
    String getOpenId(String uuid);

    /**
     * 更新用户信息到缓存中
     * @param u
     */
    void updateUser(User u);

    /**
     * 通过id从缓存中获取用户的信息
     * @param userId
     * @return
     */
    User getUserById(String userId);

    /**
     * 保存用户信息
     * @param userId
     */
    void saveUserById(User user , String userId);

    /**
     * 获取回调地址，并删除
     * @param uuid 用户登录的唯一标识
     */
    String getReturnUrl(String uuid);

    /**
     * 保存回调地址到缓存中
     * @param uuid 用户登录的唯一标识
     */
    void saveReturnUrl(String returnUrl , String uuid);

    /**
     * 保存社交登录的state到缓存中
     * @param state
     */
    void saveState(String state);

    /**
     * 校验state，校验通过就删除state
     * @param state
     * @return 返回true：校验通过
     */
    boolean checkState(String state);
}
