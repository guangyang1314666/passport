package com.guangyang.development.utils;

/**
 * 缓存工具类接口
 */
public interface CacheUtil {

    /**
     * @param key 键
     * @param value 值
     * @param timeOut 超时时间
     */
    void set(Object key,Object value,long timeOut);

    /**
     * @param key 键
     * @return 返回的只有两种情况，第一种就是字符串类型，第二中就是map类型(在缓存中存放的是对象)
     */
    <T> T get(Object key , Class<T> tClass);

    /**
     * 删除一个键值对
     * @param key
     */
    void del(Object key);
}
