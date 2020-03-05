package com.guangyang.devlopment.passport.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.guangyang.development.bean.Constant;
import com.guangyang.development.bean.User;
import com.guangyang.development.service.LoginService;
import com.guangyang.development.utils.JwtUtil;
import com.guangyang.development.utils.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class PassportController {
    @Reference
    LoginService loginService;

    /**
     * 制作token
     * @param request
     * @param user
     * @return 返回token
     */
    @GetMapping("get_token")
    @ResponseBody
    public String getToken(HttpServletRequest request, User user) {
        String token;
        // 使用jwt制作token
        String id = user.getId();
        String nickname = user.getNickname();

        Map<String , Object> userMap = new HashMap<>();
        userMap.put("memberId",id); //是报错数据库主键返回策略生成的id
        userMap.put("nickname",nickname);

        //通过nginx转发后获取客户端的ip
        String ip = request.getHeader("x-forwarded-for");
        //如果ip为空说明，该请求没有经过nginx,而是直接访问服务端
        if(StringUtils.isBlank(ip)){
            ip = request.getRemoteAddr();
            //如果此时的ip还是为空，说明nginx转发出现问题
            if (StringUtils.isBlank(ip)) {
                ip = "127.0.0.1";
            }
        }

        //按照设计的算法对参数进行加密，然后生成token
        ip = MD5Util.encode(ip,Constant.SALT);
        String key = MD5Util.encode(Constant.KEY, Constant.SALT);
        token = JwtUtil.encode(key, userMap, ip);


        //将token存在redis中一份
        loginService.saveUserTokenToCache(token,user);

        Map<String,Object> map = new HashMap<>();
        map.put("token",token);
        return JSON.toJSONString(map);
    }

    /**
     * 验证token
     * @return
     * @param currentIp 用户请求ip
     * @param token 浏览器中的旧token
     */
    @RequestMapping("verify")
    @ResponseBody
    public String verify(String token,String currentIp){
        //通过jwt校验token真假
        Map<String,String> map = new HashMap<>();

        //对密钥和盐值进行MD5加密
        String key = MD5Util.encode(Constant.KEY,Constant.SALT);
        currentIp = MD5Util.encode(currentIp,Constant.SALT);
        Map<String, Object> decode = JwtUtil.decode(token, key, currentIp);

        if (decode != null) {
            map.put("status","success");
            map.put("memberId", (String) decode.get("memberId"));
            map.put("nickname",(String) decode.get("nickname"));
        } else {
            map.put("status","fail");
        }

        return JSON.toJSONString(map);
    }
}
