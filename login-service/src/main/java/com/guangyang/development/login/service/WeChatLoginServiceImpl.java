package com.guangyang.development.login.service;

import com.alibaba.druid.sql.visitor.functions.If;
import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.guangyang.development.bean.*;
import com.guangyang.development.constant.SocialConstant;
import com.guangyang.development.key.UserKey;
import com.guangyang.development.key.UserSocialKey;
import com.guangyang.development.key.WeChatSocialKey;
import com.guangyang.development.login.mapper.UserMapper;
import com.guangyang.development.service.WeChatLoginService;
import com.guangyang.development.social.WeChat;
import com.guangyang.development.social.Weibo;
import com.guangyang.development.utils.CacheUtil;
import com.guangyang.development.utils.HttpclientUtil;
import com.guangyang.development.utils.WeChatConnectionUtil;
import com.guangyang.development.utils.encrypt.Encrypt;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * 微信登录服务
 * @version 1.0
 */
@Service
public class WeChatLoginServiceImpl implements WeChatLoginService {
    public static Logger logger = LoggerFactory.getLogger(WeChatLoginServiceImpl.class);

    @Autowired
    WeChatConnectionUtil weChatConnectionUtil;

    @Autowired
    UserMapper userMapper;

    @Autowired
    CacheUtil cacheUtil;

    @Autowired
    Encrypt encrypt;

    @Override
    public JsonData loginUrl(String returnUrl) throws UnsupportedEncodingException {

        /**
         * 由于使用的微信登录账号是别人的，设置的回调地址是http://16webtest.ngrok.xiaomiqiu.cn/，跟本项目的二级域名不
         * 一致，导致当用户扫码登录，微信进行回调wechat/user/callback时候，获取不到原始地址，解决办法将返回地址存放到
         * 缓存中
         * 回调的http://16webtest.ngrok.xiaomiqiu.cn/的url是由本地DNS解析+nginx+EchoSite最终映射到本地
         */
        // 生成随机state,该参数可用于防止csrf攻击（跨站请求伪造攻击）
        // 将state存放到redis缓存中，用于用户扫描登录成功后回调到本网站进行判断
        String state = UUID.randomUUID().toString().replaceAll("-","");

        String uuid = UUID.randomUUID().toString().replaceAll("-","");

        WeChat weChat = new WeChat();
        weChat.setReturnUrl(returnUrl);
        weChat.setState(state);
        cacheUtil.set(WeChatSocialKey.getUuidKey(uuid),weChat,60*60);
        String codeUrl = weChatConnectionUtil.getCodeUrl(state);
        return JsonData.buildSuccess(codeUrl + "?uuid=" + uuid);
    }


    /**
     * 保存用户
     * @param code
     * @param uuid 用户登录过程的唯一标识
     * @return
     */
    @Override
    public Result saveUserInfo(String code,String state , String uuid) {
        // 获取用户的openid
        Map<String, Object> baseMap = weChatConnectionUtil.getAccessToken(code);
        String openid = (String) baseMap.get("openid");

        // 从缓存中获取WeChat对象
        WeChat cacheWeChat = cacheUtil.get(WeChatSocialKey.getUuidKey(uuid), WeChat.class);


        // 验证state
        if (cacheWeChat != null) {
            if (!cacheWeChat.getState().equals(state)) {
                cacheUtil.del(WeChatSocialKey.getUuidKey(uuid));
                return Result.builder().setReturnUrl("redirect:http://development.com")
                        .setMsg("发生CSRF攻击或者是用户从绑定页面回到登录页面，重复登录")
                        .setResult(Result.ERROR);
            }
        }

        String returnUrl = Optional.ofNullable(cacheWeChat).map(WeChat::getReturnUrl).orElse("http://development.com");
        String returnUrlRedirect = "redirect:" + returnUrl;

        // 通过openid查询数据库中是否存在拥有openid用户，如果有说明是老用户，否则是新用户
        // 为了防止同时登陆的人很多，尤其像淘宝这样的网站，在双11的时候，登陆的人非常的多
        // 因此在这里，我先查缓冲，如果缓冲中查到了，就直接返回即可，没有查到再查数据库
        User cacheUser = cacheUtil.get(UserKey.getOpenIdKey(openid), User.class);

        if (cacheUser != null) {
            String token = getTokenFromPassport(cacheUser);
            // 命中缓存
            return Result.builder().setReturnUrl(returnUrlRedirect)
                    .setResult(Result.SUCCESS)
                    .setMsg("用户登录成功")
                    .setToken(token);
        }

        // 查数据库
        // 通过openid查询用户信息
        User dbUser = userMapper.findUserByOpenId(openid);
        if (dbUser != null) {
            String token = getTokenFromPassport(dbUser);
            // 更新缓存
            cacheUtil.set(UserKey.getOpenIdKey(openid) , dbUser , 60*60*24*2);
            return Result.builder().setToken(token)
                    .setReturnUrl(returnUrlRedirect)
                    .setData(dbUser)
                    .setMsg("登录成功");
        }

        // 新用户，获取微信用户信息
        Map<String, Object> userInfo = weChatConnectionUtil.getUserInfo(code);
        WeChat weChat = JSON.parseObject(JSON.toJSONString(userInfo), WeChat.class);
        weChat.setState("");
        weChat.setReturnUrl(returnUrl);
        weChat.setOpenId(openid);
        cacheUtil.set(WeChatSocialKey.getUuidKey(uuid) , weChat , 60*5);
        return Result.builder().setReturnUrl("redirect:http://login.development.com/wx_bing_user?uuid=" + uuid)
                .setMsg("新用户需要绑定已有账号")
                .setResult(Result.WARN);
    }

    /**
     * 校验微信的state,从缓存中获取
     * @param state
     * @return
     */
    @Override
    public boolean checkWxState(String state) {
        WeChat weChat = getWeChatFromCache(state);
        if (weChat != null) {
            return true;
        }
        return false;
    }

    /**
     * 从缓存中取微信用户信息
     * @param uuid
     * @return
     */
    @Override
    public WeChat getWeChatFromCache(String uuid) {
        WeChat weChat = cacheUtil.get(WeChatSocialKey.getUuidKey(uuid), WeChat.class);
        return weChat;
    }

    /**
     * 绑定微信用户
     * @param user
     * @return
     */
    @Override
    public Result bingWxUser(User user,String uuid) throws InvocationTargetException, IllegalAccessException {
        Result result = new Result();

        // 从缓存中获取微信用户信息，封装对象,保存到数据库中并且更新缓存
        WeChat cacheWeChat = getWeChatFromCache(uuid);

        String returnUrl = Optional.ofNullable(cacheWeChat).map(WeChat::getReturnUrl).orElse("http://development.com");
        String returnUrlRedirect = "redirect:" + returnUrl;

        // 如果wxUserInfoMap为空，很有可能是用户已经绑定了账号，这里直接将用户踢回登录首页
        if (cacheWeChat == null) {
            return Result.builder().setReturnUrl(returnUrl)
                    .setResult(Result.SUCCESS);
        }

        // 查询缓存
        User cacheUser = cacheUtil.get(UserKey.getUsernameKey(user.getUsername()), User.class);
        if (cacheUser != null) {
            // 判断用户是否已经绑定其他的账号
            if (StringUtils.isNotBlank(cacheUser.getOpenid())) {
                return new Result()
                        .setMsg("用户已经绑定其他账号")
                        .setResult(Result.ERROR);
            }

            // 比较密码
            boolean b = encrypt.encryptData(user.getPassword(), cacheUser.getSalt()).equals(cacheUser.getPassword());
            if (b) {
                // 将Qq的用户信息，更新到User表中
                return updateUserToCacheAndDb(cacheUser , cacheWeChat , returnUrl);
            }
        }

        // 查询数据库User表
        User dbUser = userMapper.fingUserByUsername(user.getUsername());

        if (dbUser != null) {

            // 判断openid是否存在
            if (StringUtils.isNotBlank(dbUser.getOpenid())) {
                return Result.builder().setResult(Result.ERROR)
                        .setMsg("该账号已经绑定其他的微信账号");
            }

            if (!dbUser.getPassword().equals(user.getPassword())) {
                return Result.builder().setMsg("密码错误")
                        .setResult(Result.ERROR);
            }
            // 更新User到缓存和数据库中
            return updateUserToCacheAndDb(dbUser , cacheWeChat , returnUrl);
        }
        return Result.builder().setResult(Result.ERROR)
                .setReturnUrl("")
                .setMsg("用户名不存在，可以去创建哦");
    }

    /**
     * 更新用户信息到缓存和数据库中
     * @param user
     * @param weChat
     * @param returnUrl
     * @return
     */
    private Result updateUserToCacheAndDb(User user, WeChat weChat, String returnUrl) {
        user.setChannel(SocialConstant.CHANNEL_WEIBO);
        user.setOpenid(weChat.getOpenId());
        user.setHeadImg(weChat.getHeadimgurl());
        user.setCreateTime(new Date());
        user.setNickname(weChat.getNickname());
        user.setCity(weChat.getCity());
        user.setPlatform(SocialConstant.PLATFORM_PC);

        // 更新缓存和数据库
        userMapper.updateUser(user);
        cacheUtil.set(UserKey.getUsernameKey(user.getUsername()) , user , 60*60*24*2);
        cacheUtil.set(UserKey.getOpenIdKey(weChat.getOpenId()) , user , 60*60*24*2);
        String token = getTokenFromPassport(user);
        return Result.builder().setReturnUrl(returnUrl)
                .setResult(Result.SUCCESS)
                .setMsg("用户登录成功")
                .setToken(token);
    }


    /**
     * 从缓存中获取用户的原始路径
     * @param uuid
     * @return
     */
    @Override
    public String getReturnUrlFromCache(String uuid) {
        WeChat weChat = getWeChatFromCache(uuid);
        if(weChat != null){
            // 删除存放在缓存中的用户信息
            cacheUtil.del("we_chat:" + uuid);
            return weChat.getReturnUrl();
        }
        return null;
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
