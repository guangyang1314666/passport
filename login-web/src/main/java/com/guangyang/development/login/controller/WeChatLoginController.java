package com.guangyang.development.login.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guangyang.development.bean.JsonData;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.service.LoginService;
import com.guangyang.development.service.WeChatLoginService;
import com.guangyang.development.social.WeChat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

/**
 * 功能介绍：微信登录控制层
 * @version 1.0
 */
@Controller
public class WeChatLoginController {

    @Reference
    LoginService loginService;

    @Reference
    private WeChatLoginService weChatLoginService;

    /**
     * 先拼接访问微信登录的url,然后直接重定向到微信登录
     * @param ReturnUrl
     * @return
     */
    @GetMapping("wx/login")
    @ResponseBody
    public ModelAndView loginUrl(@RequestParam(value = "return_url",required = true)String ReturnUrl,
                             HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // 保存用户的回调地址到cookie，当用户从商品详情页点击登录后，登录成功就返回原始地址
        //CookieUtil.setCookie(request,response,"ReturnUrl",ReturnUrl,60*60,true);

        // 获取获取code的url
        JsonData jsonData = weChatLoginService.loginUrl(ReturnUrl);
        // 重定向微信登录页面
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:" + jsonData.getData());

        return mv;
    }

    /**
     * 微信登录成功后的回调
     * @param code
     * @return
     */
    @GetMapping("wechat/user/callback")
    public ModelAndView weChatUserCallback(@RequestParam(value = "code",required = true) String code,
                                           @RequestParam(value = "state",required = true) String state,
                                           @RequestParam(value = "uuid",required = true) String uuid,
                                           HttpServletResponse response, HttpServletRequest request){
        ModelAndView mv = new ModelAndView();

        // 如果返回Result对象中result属性是ERROR说明，这是一个新用户(保存用户失败)
        Result result = weChatLoginService.saveUserInfo(code,state , uuid);
        mv.setViewName(result.getReturnUrl());
        return mv;
    }

    /**
     * 跳转到绑定用户视图
     * @return
     */
    @GetMapping("wx_bing_user")
    public ModelAndView bindUserView(@RequestParam(value = "uuid",required = true)String uuid){
        ModelAndView mv = new ModelAndView();
        // 从缓存中查用户信息（来自微信）
        WeChat weChat = weChatLoginService.getWeChatFromCache(uuid);
        mv.addObject("nickname",weChat.getNickname());
        mv.addObject("headimgurl",weChat.getHeadimgurl());
        mv.setViewName("wechat-bind");
        mv.addObject("uuid",uuid);
        return mv;
    }

    /**
     * 完成绑定用户,需要传来缓存中用户标识uuid，以便获取用户信息
     * 前端通过ajax请求该方法
     */
    @PostMapping("bind/wx/user")
    @ResponseBody
    public Result doBindUser(@RequestParam(value = "uuid",required = true)String uuid,
                             User user, HttpServletRequest request) throws InvocationTargetException, IllegalAccessException {

        Result result = weChatLoginService.bingWxUser(user,uuid);
        return result;
    }
}
