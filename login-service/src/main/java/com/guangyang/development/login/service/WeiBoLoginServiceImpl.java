package com.guangyang.development.login.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.bean.UserSocial;
import com.guangyang.development.constant.SocialConstant;
import com.guangyang.development.constant.UserConstant;
import com.guangyang.development.key.UserKey;
import com.guangyang.development.key.UserSocialKey;
import com.guangyang.development.key.ViewUuidKey;
import com.guangyang.development.key.WeiBoSocialKey;
import com.guangyang.development.login.mapper.UserMapper;
import com.guangyang.development.login.mapper.UserSocialMapper;
import com.guangyang.development.service.WeiBoLoginService;
import com.guangyang.development.social.ViewUuid;
import com.guangyang.development.social.Weibo;
import com.guangyang.development.utils.CacheUtil;
import com.guangyang.development.utils.HttpclientUtil;
import com.guangyang.development.utils.WeiBoConnectionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 微博登录服务
 * @version 1.0
 */
@Service
public class WeiBoLoginServiceImpl implements WeiBoLoginService {

    @Autowired
    WeiBoConnectionUtil weiBoConnectionUtil;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    UserSocialMapper userSocialMapper;

    @Autowired
    UserMapper userMapper;

    @Override
    public Result loginUrl(String returnUrl) {
        // 生成一个随机字符串state,该state不仅作为防止CRFP攻击，还作为用户登录本站的唯一标识
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");

        // 获取授权url
        String codeUrl = weiBoConnectionUtil.getCodeUrl(uuid);
        if (StringUtils.isBlank(codeUrl)) {
            return Result.builder().setData("redirect:http://login.development.com")
                    .setResult(Result.ERROR);
        }

        // 将Weibo保存到缓存中
        Weibo weibo = new Weibo();
        weibo.setState(uuid);
        weibo.setReturnUrl(returnUrl);
        cacheUtil.set(WeiBoSocialKey.getUuidKey(uuid),weibo,60*10);
        return Result.builder().setData("redirect:" + codeUrl);
    }

    @Override
    public Result weiboCallback(String code, String state , String uuid) {
        // 获取AccessToken，然后从缓存中验证其是否存在
        Weibo weibo = weiBoConnectionUtil.getAccessToken(code);
        String openid = weibo.getUid();

        // 从缓存中获取WeoBo对象
        Weibo cacheWeibo = cacheUtil.get(WeiBoSocialKey.getUuidKey(uuid), Weibo.class);

        // 验证state
        String weboState = Optional.ofNullable(cacheWeibo).map(Weibo::getState).orElse("state");
        if (weboState.equals("state") || !weboState.equals(state)) {
            // 删除WeiBo对象
            cacheUtil.del(WeiBoSocialKey.getUuidKey(uuid));
            return Result.builder().setReturnUrl("redirect:http://development.com")
                    .setMsg("state校验失败")
                    .setResult(Result.ERROR);
        }

        // 获取用户的回调地址
        String returnUrl = Optional.ofNullable(cacheWeibo).map(Weibo::getReturnUrl).orElse("http://development.com");
        String returnUrlRedirect = "redirect:" + returnUrl;

        // 查询缓存中的User表
        User cacheUser = cacheUtil.get(UserKey.getOpenIdKey(openid), User.class);
        if (cacheUser != null) {
            // 查询到用户
            // 向认证中心获取token
            String token = getTokenFromPassport(cacheUser);
            return Result.builder().setReturnUrl(returnUrlRedirect)
                    .setResult(Result.SUCCESS)
                    .setData(cacheUser)
                    .setMsg("用户登录成功")
                    .setToken(token);
        }

        // 在数据库中获取用户的信息
        User dbUser = userMapper.findUserByOpenId(openid);
        if (dbUser != null) {
            cacheUtil.set(UserKey.getOpenIdKey(openid) , dbUser , 60*60*24*2);
            // 向认证中心获取token
            String token = getTokenFromPassport(dbUser);
            return Result.builder().setReturnUrl(returnUrlRedirect)
                    .setResult(Result.SUCCESS)
                    .setMsg("用户登录成功")
                    .setData(dbUser)
                    .setToken(token);
        }

        // 这是一个新用户，向腾讯要用户的信息
        Weibo userInfo = weiBoConnectionUtil.getUserInfo(weibo);
        userInfo.setUid(openid);
        userInfo.setReturnUrl(returnUrl);
        userInfo.setState("");
        cacheUtil.set(WeiBoSocialKey.getUuidKey(uuid) , userInfo , 60*60*24*2);

        return Result.builder().setResult(Result.WARN)
                .setReturnUrl("redirect:http://login.development.com/go/weibo/bing/user" + "?uuid=" + uuid)
                .setMsg("这是一个新用户");
    }

    /**
     * 校验state
     * @param cacheWeibo
     * @return
     */
    private boolean checkState(Weibo cacheWeibo, String state) {
        Optional<Weibo> cacheWeibo1 = Optional.ofNullable(cacheWeibo);
        return cacheWeibo1.map(c -> {
            return c.getState().equals(state);
        }).orElse(false);
    }

    @Override
    public Weibo getWeiBOFromCache(String uuid) {
        Weibo cacheWeiBo = cacheUtil.get(WeiBoSocialKey.getUuidKey(uuid), Weibo.class);
        return cacheWeiBo;
    }

    @Override
    public Result doBindUser(User user, String uuid) {

        // 从缓存中获取WeiBo数据
        Weibo cacheWeiBo = getWeiBOFromCache(uuid);

        if (cacheWeiBo == null) {
            return Result.builder().setReturnUrl("http://development.com")
                    .setResult(Result.SUCCESS);
        }

        // 获取用户的回调地址
        String returnUrl = cacheWeiBo.getReturnUrl();

        // 开始查询数据库
        User dbUser = userMapper.fingUserByUsername(user.getUsername());
        if (dbUser != null) {
            // 查看该用户是否已经绑定其他社交账号
            if (StringUtils.isNotBlank(dbUser.getOpenid())) {
                return Result.builder().setResult(Result.WARN)
                        .setMsg("该账号已经绑定其他微博账号");
            }

            // 匹配密码
            if (!dbUser.getPassword().equals(user.getPassword())) {
                return Result.builder().setMsg("密码不正确")
                        .setResult(Result.ERROR);
            }

            // 向微博获取用户信息
            Weibo userInfo = weiBoConnectionUtil.getUserInfo(cacheWeiBo);
            dbUser.setUpdateTime(new Date());
            dbUser.setSex("1");
            dbUser.setHeadImg(userInfo.getAvatarLarge());
            dbUser.setChannel(SocialConstant.CHANNEL_WEIBO);
            dbUser.setCity(userInfo.getLocation());
            dbUser.setNickname(userInfo.getScreenName());
            dbUser.setOpenid(userInfo.getUid());

            // 将用户更新到缓存以及数据库中
            userMapper.updateUser(dbUser);
            cacheUtil.set(UserKey.getOpenIdKey(userInfo.getUid()) , dbUser ,60*60*24*2);
            // 向认证中心获取token
            String token = getTokenFromPassport(dbUser);
            return Result.builder().setMsg("绑定成功")
                    .setReturnUrl(returnUrl)
                    .setResult(Result.SUCCESS)
                    .setToken(token);
        }

        return Result.builder().setResult(Result.ERROR)
                .setMsg("用户名不存在，没有用户名可以去创建哦");
    }

    /**
     * 向认证中心要token
     * @return token
     */
    private String getTokenFromPassport(User user) {
        StringBuilder url = new StringBuilder("http://passport.development.com/get_token?");
        url.append("id=").append(user.getId())
                .append("&nickname=").append(user.getNickname());
        Map<String, Object> token = HttpclientUtil.doGet(url.toString());
        return (String) token.get("token");
    }
}
