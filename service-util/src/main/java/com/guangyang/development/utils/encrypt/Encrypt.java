package com.guangyang.development.utils.encrypt;

/**
 * 加密接口,所有的加密都是不可逆的
 * @version 1.0
 */
public interface Encrypt {
    /**
     * 对数据的加密
     * @param data 要加密的数据
     * @param salt 盐值
     * @return 加密后的数据
     */
    String encryptData(String data,String salt);
}
