package com.guangyang.development.login.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.guangyang.development.bean.CacheKeyBuilder;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.key.UserKey;
import com.guangyang.development.login.mapper.UserMapper;
import com.guangyang.development.service.LoginService;
import com.guangyang.development.utils.CacheUtil;
import com.guangyang.development.utils.HttpclientUtil;
import com.guangyang.development.utils.encrypt.Encrypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    RedisTemplate<Object,Object> myRedisTemplate;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    Encrypt encrypt;

    @Autowired
    UserMapper userMapper;

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

    /**
     * 使用本网站账号登录
     * @param returnUrl
     * @return
     */
    @Override
    public Result login(String returnUrl,User user) {
        // 查询缓存
        User cacheUser = cacheUtil.get(UserKey.getUsernameKey(user.getUsername()), User.class);

        if (cacheUser != null) {
            // 对密码进行加密验证
            String encryptPassword = encrypt.encryptData(user.getPassword(), cacheUser.getSalt());
            if (!encryptPassword.equals(cacheUser.getPassword())) {
                return Result.builder().setMsg("用户名与密码不匹配")
                        .setResult(Result.ERROR);
            }

            // 从认证中心获取token
            Map<String, Object> token = getTokenFromPassport(user);

            return Result.builder().setResult(Result.SUCCESS)
                    .setMsg("登录成功")
                    .setReturnUrl(returnUrl)
                    .setToken((String) token.get("token"));
        }

        // 缓存中没有，开始查询数据库
        User dbUser = userMapper.fingUserByUsername(user.getUsername());
        if (dbUser != null) {
            // 将用户更新到缓存中
            cacheUtil.set(UserKey.getUsernameKey(dbUser.getUsername()) , dbUser , 60*60*24*2);

            // 对用户密码加密
            String encryptPassword = encrypt.encryptData(user.getPassword(), dbUser.getSalt());
            if (!encryptPassword.equals(dbUser.getPassword())) {
                return Result.builder().setMsg("用户名与密码不匹配")
                        .setResult(Result.ERROR);
            }

            // 从认证中心获取token
            Map<String, Object> token = getTokenFromPassport(user);
            return Result.builder().setToken((String) token.get("token"))
                    .setReturnUrl(returnUrl)
                    .setMsg("用户登录成功")
                    .setResult(Result.SUCCESS);
        }
        return Result.builder().setResult(Result.ERROR)
                .setMsg("用户名与密码不匹配");
    }

}
