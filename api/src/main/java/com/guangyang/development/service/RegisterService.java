package com.guangyang.development.service;

import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;

/**
 * 用户注册服务
 */
public interface RegisterService {
    /**
     * 完成用户的注册
     * @param user
     * @return
     */
    Result doRegister(User user);
}
