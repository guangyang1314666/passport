package com.guangyang.development.utils;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 缓存模板工具类
 */
@Component
public class RedisTemplateUtil implements CacheUtil{

    @Autowired
    RedisTemplate<Object,Object> myRedisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Override
    public void set(Object key, Object value, long timeOut) {
        myRedisTemplate.opsForValue().set(key,value,timeOut, TimeUnit.SECONDS);
    }

    @Override
    public <T> T get(Object key , Class<T> tClass) {

        Object object = null;
        try {

            object = myRedisTemplate.opsForValue().get(key);

            if (tClass.getSimpleName().equals("String")) {
                T t = tClass.newInstance();
                t = (T) object;
                return t;
            }else {
                return JSON.parseObject(JSON.toJSONString(object),tClass);
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        return null;

    }

    @Override
    public void del(Object key) {
        myRedisTemplate.delete(key);
    }
}
