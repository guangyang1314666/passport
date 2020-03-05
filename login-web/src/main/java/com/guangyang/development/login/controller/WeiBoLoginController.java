package com.guangyang.development.login.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.service.LoginService;
import com.guangyang.development.service.WeiBoLoginService;
import com.guangyang.development.social.Weibo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * 微博开放平台登录控制层
 */
@Controller
public class WeiBoLoginController {

    @Reference
    WeiBoLoginService weiBoLoginService;

    @Reference
    LoginService loginService;

    /**
     * 拼接授权页面的url,然后重定向到授权页面
     * @param returnUrl
     * @return
     */
    @GetMapping("weibo/login")
    public ModelAndView loginUrl(@RequestParam(value = "return_url",required = true)String returnUrl){
        Result result = weiBoLoginService.loginUrl(returnUrl);
        ModelAndView mv = new ModelAndView();
        mv.setViewName((String) result.getData());
        return mv;
    }

    /**
     * 微博登录成功后的回调
     * @return
     */
    @GetMapping("weibo/login/callback")
    public ModelAndView weiboCallback(@RequestParam(value = "code",required = true)String code,
                                      @RequestParam(value = "state",required = true)String state){
        ModelAndView mv = new ModelAndView();
        // 微博回调服务
        Result result = weiBoLoginService.weiboCallback(code,state,state);
        mv.setViewName(result.getReturnUrl());
        return mv;
    }


    /**
     * 跳转到绑定用户视图
     * @param uuid 本网站使用state做为用户登录的唯一标识
     * @return
     */
    @GetMapping("go/weibo/bing/user")
    public ModelAndView goBindUser(@RequestParam(value = "uuid",required = true)String uuid){
        ModelAndView mv = new ModelAndView();
        // 从缓存中查用户信息（来自微博）
        Weibo weibo = weiBoLoginService.getWeiBOFromCache(uuid);
        mv.addObject("nickname",weibo.getScreenName());
        mv.addObject("headimgurl",weibo.getProfileImageUrl());
        mv.setViewName("weibo-bind");
        mv.addObject("uuid",uuid);
        return mv;
    }

    @PostMapping("do/weibo/bing/user")
    @ResponseBody
    public Result doBindUser(@RequestParam(value = "uuid",required = true)String uuid,
                                   User user){
        Result result = weiBoLoginService.doBindUser(user,uuid);
        return result;
    }

}
