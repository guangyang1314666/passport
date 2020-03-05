package com.guangyang.development.utils.encrypt;

import javax.sound.midi.Soundbank;
import java.util.UUID;

/**
 * 测试类
 */
public class Test {
    public static void main(String[] args) {
        Encrypt encrypt = new EncryptImpl();
        String salt = UUID.randomUUID().toString().replaceAll("-","");
        // 26b4e0aa8c65431e9b6ea89f6cc5067e
        System.out.println(salt);
        // 2a8a4bbeb4828b6b011b1836286d62190d9bdf43b8c19ee450f3a7b3ee728a04
        String password = encrypt.encryptData("12345678", "26b4e0aa8c65431e9b6ea89f6cc5067e");
        if (password.equals("2a8a4bbeb4828b6b011b1836286d62190d9bdf43b8c19ee450f3a7b3ee728a04")) {
            System.out.println("密码输入正确");
        }
        System.out.println(password);
    }
}
