package com.guangyang.development.utils.encrypt;

public class EncryptImpl implements Encrypt {
    @Override
    public String encryptData(String data, String salt) {
        // 先使用MD5进行加密
        String md5EncryptPassword = MD5Encrypt.encode(data,salt);
        // 使用SHA进行加密
        String shaEncryptPassword = SHAEncrypt.encryptSHA256(md5EncryptPassword);
        return shaEncryptPassword;
    }
}
