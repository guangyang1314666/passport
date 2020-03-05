package com.guangyang.development.service;

import com.guangyang.development.bean.JsonData;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.social.WeChat;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @version 1.0
 */
public interface WeChatLoginService {
    JsonData loginUrl(String returnUrl) throws UnsupportedEncodingException;

    Result saveUserInfo(String code,String state,String uuid);

    boolean checkWxState(String state);

    WeChat getWeChatFromCache(String uuid);

    Result bingWxUser(User user, String uuid) throws InvocationTargetException, IllegalAccessException;

    String getReturnUrlFromCache(String uuid);
}
