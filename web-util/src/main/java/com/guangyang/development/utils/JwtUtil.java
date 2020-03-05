package com.guangyang.development.utils;

import io.jsonwebtoken.*;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    // 过期时间为一周
    private static final long EXPIRE = 1000*60*60*24*7;
    /**
     * 加密(默认过期时间为一周)
     * @param key 服务器公共密钥
     * @param param 用户信息
     * @param salt 盐值(ip+time)
     * @return token
     */
    public static String encode(String key, Map<String,Object> param, String salt){
        if(salt!=null){
            key+=salt;
        }
        JwtBuilder jwtBuilder = Jwts.builder().signWith(SignatureAlgorithm.HS256,key)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE ));

        jwtBuilder = jwtBuilder.setClaims(param);

        String token = jwtBuilder.compact();
        return token;

    }


    /**
     * 解密，salt是key的盐值
     * @param token
     * @param key
     * @param salt
     * @return
     */
    public  static Map<String,Object> decode(String token ,String key,String salt){
        Claims claims=null;
        if (salt!=null){
            key+=salt;
        }
        try {
            claims= Jwts.parser().setSigningKey(key).parseClaimsJws(token).getBody();
        } catch ( JwtException e) {
           return null;
        }
        return  claims;
    }
}