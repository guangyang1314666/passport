package com.guangyang.development.login.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.constant.SocialConstant;
import com.guangyang.development.constant.UserConstant;
import com.guangyang.development.key.QqSocialKey;
import com.guangyang.development.key.UserKey;
import com.guangyang.development.key.ViewUuidKey;
import com.guangyang.development.login.mapper.UserMapper;
import com.guangyang.development.service.QqLoginService;
import com.guangyang.development.social.Qq;
import com.guangyang.development.social.ViewUuid;
import com.guangyang.development.utils.CacheUtil;
import com.guangyang.development.utils.HttpclientUtil;
import com.guangyang.development.utils.QQConnectionUtil;
import com.guangyang.development.utils.encrypt.Encrypt;
import net.bytebuddy.asm.Advice;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * qq登录服务层
 * @version 1.0
 */
@Service
public class QqLoginServiceImpl implements QqLoginService {

    @Autowired
    QQConnectionUtil qqConnectionUtil;

    @Autowired
    CacheUtil cacheUtil;


    @Autowired
    UserMapper userMapper;

    @Autowired
    Encrypt encrypt;

    @Override
    public Result loginUrl(String returnUrl) {
        Qq qq = new Qq();

        String state = (UUID.randomUUID().toString() + UUID.randomUUID().toString()).replaceAll("-","").toUpperCase();

        // 拼接一个uuid，作为用户登录的唯一标识
        String uuid = UUID.randomUUID().toString().replaceAll("-","");
        String codeUrl = qqConnectionUtil.getCodeUrl(state,uuid);

        // 保存返回路径
        qq.setReturnUrl(returnUrl);
        qq.setState(state);
        cacheUtil.set(QqSocialKey.getUuidKey(uuid),qq,60*5);
        return Result.builder().setData(codeUrl);
    }

    @Override
    public Result qqCallback(String state, String code,String uuid) throws JsonProcessingException {
        StringBuilder url = new StringBuilder();

        String accessToken = qqConnectionUtil.getAccessToken(code);
        String openId = qqConnectionUtil.getOpenId(accessToken);

        // 从缓存中获取Qq对象,为了获取回调地址
        Qq qq = cacheUtil.get(QqSocialKey.getUuidKey(uuid), Qq.class);
        Optional<Qq> qqOpt = Optional.ofNullable(qq);
        Optional<Qq> qqOptional = qqOpt.filter(q -> q.getState().equals(state));

        if (!qqOptional.isPresent()) {
            return Result.builder()
                    .setReturnUrl("redirect:http://development.com")
                    .setMsg("受到CSRF攻击或者用户是从绑定页面回退到Qq登录重复登录");
        }

        // 获取用户的回调地址如果没有就设置重定向到首页
        String returnUrl = qqOpt.map(Qq::getReturnUrl).orElse("http://development.com");
        String returnUrlRedirect = "redirect:" + returnUrl;

        // 从缓存中获取User对象比较openId,键值对是，user:openid:openid值 user对象值，如果查到，就
        // 说明用户已经绑定已有的账号
        User user = cacheUtil.get(UserKey.getOpenIdKey(openId), User.class);

        if (user != null) {
            String token = getTokenFromPassport(user);
            return Result.builder().setReturnUrl(returnUrlRedirect + "？token=" + token)
                    .setMsg("用户登录成功")
                    .setData(user)
                    .setResult(Result.SUCCESS);
        }

        // 缓存中没有，查询数据库中的User表
        User dbUser = userMapper.findUserByOpenId(openId);

        if (dbUser != null) {
            // 说明在数据库中查询到用户
            // 更新缓存
            cacheUtil.set(UserKey.getOpenIdKey(openId), dbUser , 60*10);
            String token = getTokenFromPassport(dbUser);
            return Result.builder().setResult(Result.SUCCESS)
                    .setData(dbUser)
                    .setReturnUrl(returnUrlRedirect + "？token=" + token)
                    .setMsg("用户登录成功");
        } else {
            // 向腾讯获取Qq用户信息
            Qq userInfo = qqConnectionUtil.getUserInfo(accessToken, openId);
            userInfo.setReturnUrl(returnUrl);
            userInfo.setOpenid(openId);
            userInfo.setState("");
            cacheUtil.set(QqSocialKey.getUuidKey(uuid) , userInfo,60 * 10);
            // 直接返回，引导用户去绑定已有账号
            return Result.builder().setReturnUrl("redirect:http://login.development.com/go/qq/bind?uuid=" + uuid);
        }
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

    @Override
    public Qq getQqInfoFromCache(String uuid) {
        Qq qq = cacheUtil.get(QqSocialKey.getUuidKey(uuid), Qq.class);
        return qq;
    }

    @Override
    public Result doQqBind(String uuid, User user) {
        // 1. 从缓存中获取用户的信息
        String username = user.getUsername();
        // 从缓存中获取Qq对象
        Qq qq = cacheUtil.get(QqSocialKey.getUuidKey(uuid), Qq.class);

        if (qq == null) {
            // QQ为空，设置状态为成功，使页面发生跳转(前端的玩活)
            return Result.builder().setReturnUrl("http://development.com")
                    .setResult(Result.WARN);
        }

        // 用户的回调地址
        String returnUrl = Optional.ofNullable(qq).map(Qq::getReturnUrl).orElse("http://development.com");

        // 查询缓存User
        User cacheUser = cacheUtil.get(UserKey.getUsernameKey(username), User.class);
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
                return updateUserToCacheAndDb(cacheUser , qq , returnUrl);
            }
        }

        // 查询数据库的user表
        User dbUser = userMapper.fingUserByUsername(username);

        if (dbUser != null) {

            // 开始比较用户输入的密码和数据库中的密码是否一致
            String dbPassword = dbUser.getPassword();

            // 不为空说明绑定其他账号，说明该账号已经绑定其他的社交账号了
            if (StringUtils.isNotBlank(dbUser.getOpenid())) {
                return Result.builder().setResult(Result.ERROR)
                        .setMsg("该账号已经绑定其他的账号，请试试其他账号");
            }

            // 对密码进行加密
            String password = encrypt.encryptData(user.getPassword() , dbUser.getSalt()) ;
            if (dbPassword.equals(password)) {
                return updateUserToCacheAndDb(dbUser , qq , returnUrl);
            }
        }

        return Result.builder().setMsg("用户名与密码不匹配")
                .setResult(Result.ERROR);

    }

    /**
     * 更新User表到数据库以及缓存中
     * @param user 用户
     * @param qq QQ用户对象
     * @param returnUrl 回调地址
     * @return
     */
    private Result updateUserToCacheAndDb(User user, Qq qq , String returnUrl) {
        // 将Qq的用户信息，更新到User表中
        User newUser = new User();
        newUser = user;
        if (qq.getGender().equals("男")) {
            newUser.setSex(UserConstant.SEX_F);
        } else if(qq.getGender().equals("女")){
            newUser.setSex(UserConstant.SEX_M);
        } else {
            newUser.setSex(UserConstant.SEX_N);
        }
        newUser.setCity(qq.getCity());
        newUser.setNickname(qq.getNickname());
        newUser.setCreateTime(new Date());
        newUser.setOpenid(qq.getOpenid());
        newUser.setChannel(SocialConstant.CHANNEL_QQ);
        if (StringUtils.isNotBlank(user.getHeadImg())) {
            newUser.setHeadImg(qq.getFigureurl_qq_1());
        }

        // 将用户数据更新到数据库中
        userMapper.updateUser(newUser);
        // 将用户信息更新到缓存中
        cacheUtil.set(UserKey.getOpenIdKey(qq.getOpenid()),newUser,60*12);
        cacheUtil.set(UserKey.getUsernameKey(user.getUsername()),newUser,60*12);

        return Result.builder().setReturnUrl(returnUrl)
                .setMsg("成功绑定账号")
                .setData(newUser)
                .setResult(Result.SUCCESS);
    }

    /**
     * 删除缓存中的ViewUuid，防止用户进行回退操作(非法操作)
     * @param uuid
     */
    @Override
    public void deleteViewUuidFromCache(String uuid) {
        cacheUtil.del(ViewUuidKey.getQqBindUserKey(uuid));
    }

}
