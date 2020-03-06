package com.guangyang.development.login;

import com.guangyang.development.social.Qq;

import javax.xml.transform.Source;
import java.util.Optional;

public class Test {



    @org.junit.Test
    public void test1(){
        Qq qq = new Qq();
        Optional<Qq> qqUserInfo1 = Optional.ofNullable(qq);
        qqUserInfo1.map(newQq -> {
            qq.setOpenid("1234");
            System.out.println("非空对象");
            return qq;
        });
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        Qq qq = new Qq();
        qq.setOpenid("123456");
        Optional<Qq> qqOptional = Optional.ofNullable(qq);
        Optional<Qq> cacheQq = qqOptional.filter(optionalQq -> optionalQq.getOpenid().equals("12345"));
        cacheQq.ifPresent(q -> {
            System.out.println("匹配成功");
            System.out.println(q.getOpenid());
        });
    }

}
