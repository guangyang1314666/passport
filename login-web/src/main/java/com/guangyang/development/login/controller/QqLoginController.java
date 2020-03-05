package com.guangyang.development.login.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.guangyang.development.bean.JsonData;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.service.LoginService;
import com.guangyang.development.service.QqLoginService;
import com.guangyang.development.social.Qq;
import com.guangyang.development.utils.HttpclientUtil;
import jdk.nashorn.internal.ir.IfNode;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;

/**
 * qq登录控制层
 * @version 1.0
 */
@Controller
public class QqLoginController {

    @Reference
    QqLoginService qqLoginService;

    @Reference
    LoginService loginService;

    /**
     * 先拼接访问微信登录的url,然后直接重定向到微信登录
     * @param ReturnUrl
     * @return
     */
    @GetMapping("qq/login")
    @ResponseBody
    public ModelAndView loginUrl(@RequestParam(value = "return_url",required = true)String ReturnUrl,
                                 HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        // 保存用户的回调地址到cookie，当用户从商品详情页点击登录后，登录成功就返回原始地址
        //CookieUtil.setCookie(request,response,"ReturnUrl",ReturnUrl,60*60,true);

        // 获取获取code的url
         Result result = qqLoginService.loginUrl(ReturnUrl);

        // 重定向qq登录页面
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:" + result.getData());

        return mv;
    }

    /**
     * qq登录回调
     * @return
     */
    @GetMapping("qq/callback")
    public ModelAndView qqCallback(@RequestParam(value = "state",required = true)String state,
                                   @RequestParam(value = "code",required = true)String code,
                                   @RequestParam(value = "uuid",required = true)String uuid) throws JsonProcessingException {
        ModelAndView mv = new ModelAndView();

        Result result = qqLoginService.qqCallback(state,code,uuid);
        mv.setViewName(result.getReturnUrl());
        return mv;
    }

    /**
     * 去qq绑定页面
     * @param uuid 用户登录的唯一标识
     * @return
     */
    @GetMapping("go/qq/bind")
    public ModelAndView goBind(@RequestParam(value = "uuid",required = true)String uuid){
        ModelAndView mv = new ModelAndView();
        // 从缓存中查询qq用户的信息
        Qq qq = qqLoginService.getQqInfoFromCache(uuid);
        // 删除cache中的ViewUuid
        qqLoginService.deleteViewUuidFromCache(uuid);
        if (qq != null) {
            mv.setViewName("qq-bind");
            mv.addObject("nickname", qq.getNickname());
            mv.addObject("figureurlQq1", qq.getFigureurl_qq_1());
            mv.addObject("uuid", uuid);
        } else {
            mv.setViewName("redirect:http://development.com");
        }

        return mv;
    }

    /**
     *
     * @param state
     * @return
     */
    @PostMapping("do/qq/bind")
    @ResponseBody
    public Result doQqBind(@RequestParam(value = "uuid",required = true)String state,
                           User user){
        Result result = qqLoginService.doQqBind(state,user);
        return result;
    }

}
