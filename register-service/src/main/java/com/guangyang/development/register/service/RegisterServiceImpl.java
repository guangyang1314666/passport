package com.guangyang.development.register.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.constant.SocialConstant;
import com.guangyang.development.key.UserKey;
import com.guangyang.development.register.mapper.UserMapper;
import com.guangyang.development.service.RegisterService;
import com.guangyang.development.utils.CacheUtil;
import com.guangyang.development.utils.encrypt.Encrypt;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.UUID;

@Service
public class RegisterServiceImpl implements RegisterService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    Encrypt encrypt;

    @Autowired
    CacheUtil cacheUtil;

    @Override
    public Result doRegister(User user) {

        // 查询数据库，判断用户是否存在
        User checkUser = userMapper.fingUserByUsername(user.getUsername());

        if (checkUser != null) {
            return Result.builder().setReturnUrl("redirect:http://reg.development.com/register")
                    .setResult(Result.ERROR)
                    .setMsg("用户已存在");
        }

        String salt = UUID.randomUUID().toString();

        // 将用户名和密码进行加密处理
        String encryptPassword = encrypt.encryptData(user.getPassword(), salt);
        User u = new User();
        u.setNickname("游客");
        u.setSex("2");
        u.setChannel(SocialConstant.CHANNEL);
        u.setCreateTime(new Date());
        u.setUsername(user.getUsername());
        u.setSalt(salt);
        u.setPassword(encryptPassword);
        u.setUpdateTime(new Date());

        // 更新数据库
        userMapper.saveUser(u);
        // 更新缓存中
        cacheUtil.set(UserKey.getUsernameKey(user.getUsername()) , u , 60*60*24*2);
        return Result.builder().setMsg("注册成功了")
                .setResult(Result.SUCCESS)
                .setReturnUrl("http://login.development.com?return_url=http://development.com");
    }
}
