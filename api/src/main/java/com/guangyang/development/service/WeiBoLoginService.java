package com.guangyang.development.service;

import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.social.Weibo;

/**
 * 微博登录服务接口层
 * @version 1.0
 */
public interface WeiBoLoginService {
    /**
     * 拼接授权页面的url
     * @return
     */
    Result loginUrl(String returnUrl);

    /**
     * 微博回调
     * @param code
     * @param state
     * @return
     */
    Result weiboCallback(String code, String state,String uuid);

    /**
     * 从缓存中查找WeiBo对象
     * @param uuid
     * @return
     */
    Weibo getWeiBOFromCache(String uuid);

    /**
     * 完成用户的绑定
     * @param user
     * @param uuid
     * @return
     */
    Result doBindUser(User user, String uuid);
}
