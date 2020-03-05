package com.guangyang.development.service;

import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;

import java.util.Map;

/**
 * 登录服务
 */
public interface LoginService {

    void saveUserTokenToCache(String token, User user);

    Map<String, Object> getTokenFromPassport(User user);

    /**
     * 用户回调地址设置，包含在回调地址中拼接token
     * @param result
     * @return
     */
    Result returnUrl(Result result);
}
