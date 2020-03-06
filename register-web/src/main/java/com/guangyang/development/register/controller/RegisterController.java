package com.guangyang.development.register.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.guangyang.development.bean.Result;
import com.guangyang.development.bean.User;
import com.guangyang.development.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

/**
 * 注册
 * @version 1.0
 */
@Controller
public class RegisterController {

    @Reference
    RegisterService registerService;

    /**
     * 进入到用户注册页面
     * @return
     */
    @GetMapping("register")
    public ModelAndView index(){
        ModelAndView mv = new ModelAndView();
        mv.setViewName("register");
        return mv;
    }

    @PostMapping("do/register")
    public ModelAndView register(User user){
        ModelAndView mv = new ModelAndView();
        Result result = registerService.doRegister(user);
        if (result.getResult().equals(Result.SUCCESS)) {
            mv.setViewName("redirect:" + result.getReturnUrl());
        } else {
            mv.setViewName("redirect:http://reg.development.com/register");
        }

        return mv;
    }
}
