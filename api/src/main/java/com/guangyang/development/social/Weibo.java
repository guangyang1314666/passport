package com.guangyang.development.social;

import java.io.Serializable;

/**
 * 微博用户信息实体类
 */
public class Weibo implements Serializable {

    /**
     * 用户的原始地址
     */
    private String returnUrl;

    /**
     * 用户防止CSRF攻击，同时本网站也将他作为用户登录过程中的唯一标识
     */
    private String state;

    /**
     * 用户在微博中的id,相当于openid
     */
    private String uid;

    /**
     * 用户token,相当于开放平台的openId，默认是9天过期
     */
    private String access_token;

    /**
     * 用户昵称
     */
    private String screenName;

    /**
     * 用户所在地
     */
    private String location;

    /**
     * 用户头像地址（中图），50×50像素
     */
    private String profileImageUrl;

    /**
     * 用户头像地址（高清），高清头像原图
     */
    private String avatarHd;

    /**
     * 用户头像地址（大图），180×180像素
     */
    private String avatarLarge;

    /**
     * 性别，m：男、f：女、n：未知
     */
    private String gender;


    public String getUid() {
        return uid;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getAvatarHd() {
        return avatarHd;
    }

    public void setAvatarHd(String avatarHd) {
        this.avatarHd = avatarHd;
    }

    public String getAvatarLarge() {
        return avatarLarge;
    }

    public void setAvatarLarge(String avatarLarge) {
        this.avatarLarge = avatarLarge;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
