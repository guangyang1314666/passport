package com.guangyang.development.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.social.Qq;

/**
 * qq登录接口
 * @version 1.0
 */
public interface QqLoginService {

    /**
     * 拼接跳转到qq登录的url
     * @param returnUrl
     * @return
     */
    Result loginUrl(String returnUrl);

    Result qqCallback(String state, String code,String uuid) throws JsonProcessingException;

    Qq getQqInfoFromCache(String uuid);

    Result doQqBind(String uuid, User user);

    void deleteViewUuidFromCache(String uuid);
}
