package com.guangyang.development.login.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.service.LoginService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {
    @Reference
    LoginService loginService;

    /**
     * 登录与注册首页
     * @param returnUrl
     * @return
     */
    @RequestMapping("index")
    public ModelAndView index(@RequestParam(value = "return_url",required = true)String returnUrl){
        ModelAndView mv = new ModelAndView();
        // 将用户的原始地址传到首页中(隐藏起来)
        mv.addObject("return_url",returnUrl);
        mv.setViewName("index");
        return mv;
    }

    /**
     * 使用本网站账号进行登录
     * @param returnUrl
     * @return
     */
    @PostMapping("login")
    @ResponseBody
    public Object login(@RequestParam(value = "return_url",required = true)String returnUrl,
                        User user){
        ModelAndView mv = new ModelAndView();
        Result result = loginService.login(returnUrl,user);
        return result;
    }

}
