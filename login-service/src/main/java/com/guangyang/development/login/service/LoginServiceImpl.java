package com.guangyang.development.login.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.guangyang.development.bean.CacheKeyBuilder;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.service.LoginService;
import com.guangyang.development.utils.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    RedisTemplate<Object,Object> myRedisTemplate;

    /**
     * 将用户的token保存到缓存中
     * @param token
     * @param user
     */
    @Override
    public void saveUserTokenToCache(String token, User user) {
        String key = CacheKeyBuilder.getKey(CacheKeyBuilder.USER_TOKEN, user.getId(), token);
        myRedisTemplate.opsForValue().set(key,token,60*60*7, TimeUnit.SECONDS);
    }

    /**
     * 像认证中心要token
     * @return
     */
    @Override
    public Map<String, Object> getTokenFromPassport(User user) {
        // 向认证中心要token
        StringBuilder url = new StringBuilder("http://passport.development.com/get_token?");
        url.append("id=").append(user.getId()).append("&nickname=").append(user.getNickname());
        Map<String, Object> tokenMap = HttpclientUtil.doGet(url.toString());
        return tokenMap;
    }

    @Override
    public Result returnUrl(Result result) {
        if (result.getResult().equals(Result.SUCCESS)) {

            // 向认证中心要token
            Map<String, Object> tokenMap = getTokenFromPassport((User) result.getData());
            String token = (String) tokenMap.get("token");

            // 从cookie中获取原始地址
            // String cookieValue = CookieUtil.getCookieValue(request, CookieNameManager.RESTURN, true);
            if (StringUtils.isNotBlank(result.getReturnUrl())) {
                // 重定向到原始地址
                result.setData("http://" + result.getReturnUrl() + "?token=" + token);
            }else {
                result.setData("http://" + "www.baidu.com" + "?token=" + token);
            }
        }
        return result;
    }
}
