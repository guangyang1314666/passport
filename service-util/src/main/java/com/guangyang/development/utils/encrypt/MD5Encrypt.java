package com.guangyang.development.utils.encrypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Random;
import java.util.UUID;

/**
 * @author 沈光阳
 * @create 2019-11-24-9:20
 */
public class MD5Encrypt {



    /**
     * 根据明文和盐值得到密文
     * @param plaintext 明文
     * @param salt 盐
     * @return 密文
     */
    public static String encode(String plaintext,String salt){

        try {
            return md5(md5(plaintext)+salt);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据明文，盐和密文验证密码是否正确
     * @param plaintext 明文
     * @param salt 盐
     * @param miwen 密文
     * @return
     */
    public static boolean validate(String plaintext,String salt,String miwen){
        return miwen.equals(encode(plaintext,salt));
    }

    /**
     * 对字符串进行MD5加密
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            byte[] b = md.digest();
            StringBuffer buf = new StringBuffer("");

            for(int offset = 0; offset < b.length; ++offset) {
                int i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }

                buf.append(Integer.toHexString(i));
            }

            str = buf.toString();
        } catch (Exception var6) {
            var6.printStackTrace();
        }

        return str;
    }

    public static String md5(File file) {
        FileInputStream fis = null;

        try {
            fis = new FileInputStream(file);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            boolean var4 = true;

            int length;
            while((length = fis.read(buffer, 0, 1024)) != -1) {
                md.update(buffer, 0, length);
            }

            BigInteger bigInt = new BigInteger(1, md.digest());
            String var6 = bigInt.toString(16);
            return var6;
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            try {
                fis.close();
            } catch (IOException var15) {
                var15.printStackTrace();
            }

        }

        return null;
    }

    public static void main(String[] args) {
        String uuid = UUID.randomUUID().toString();
        String s1 = md5(md5(uuid) + uuid);
        System.out.println(s1);
        System.out.println(md5("123456"));
        System.out.println(md5("asdfalkjdsflj"));
    }
}
