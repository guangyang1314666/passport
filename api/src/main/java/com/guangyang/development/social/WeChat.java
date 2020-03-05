package com.guangyang.development.social;

import com.alibaba.fastjson.JSON;
import com.guangyang.development.bean.JsonData;
import org.apache.ibatis.annotations.Insert;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 微信用户信息实体类
 * @version 1.0 本类将被存储到缓存中，使用的数据结构 k = we_chat:uuid(值), v = WeChat对象
 */
public class WeChat implements Serializable {
    /**
     * 用户原始地址
     */
    private String returnUrl;

    /**
     * 普通用户的标识，对当前开发者帐号唯一
     */
    private String openId;

    /**
     *
     */
    private String state;

    /**
     * 错误代码
     */
    private Integer errcode;

    /**
     * 错误信息
     */
    private String errmsg;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 普通用户性别，1为男性，2为女性
     */
    private String sex;

    /**
     * 普通用户个人资料填写的省份
     */
    private String province;

    /**
     * 普通用户个人资料填写的城市
     */
    private String city;

    /**
     * 国家，如中国为CN
     */
    private String country;

    /**
     * 户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，
     * 0代表640*640正方形头像），用户没有头像时该项为空
     */
    private String headimgurl;

    /**
     * 用户统一标识。针对一个微信开放平台帐号下的应用，同一用户的unionid是唯一的。
     * 开发者最好保存用户unionID信息，以便以后在不同应用中进行用户信息互通。
     */
    private String unionid;

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getErrcode() {
        return errcode;
    }

    public void setErrcode(Integer errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public static void main(String[] args) {
        Map<String,Object> map = new HashMap<>();
        map.put("errcode",1234);
        map.put("errmsg","你的appkey过期了");
        WeChat weChat = JSON.parseObject(JSON.toJSONString(map), WeChat.class);
        System.out.println(weChat.toString());
    }
}
