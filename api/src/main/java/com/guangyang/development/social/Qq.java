package com.guangyang.development.social;

import java.io.Serializable;

/**
 * QQ用户信息实体类
 */
public class Qq  implements Serializable {

    /**
     * 用户登录过程中的唯一标识，可以防止用户在绑定用户的时候，进行回退到社交
     * 登录页面，如果用户回退，就直接将用户踢到用户中心
     */
    private String uuid = "uuid";

    /**
     * state在本网站作为用户登录的唯一标识
     */
    private String state;

    /**
     * 用户的返回路径,非重定向
     */
    private String returnUrl;

    /**
     * 返回码
     */
    private Integer ret;
    /**
     * 用户openid
     */
    private String openid;

    /**
     * 使用code换取的token
     */
    private String access_token;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 性别，官方直接传男
     * @return
     */
    private String gender;

    /**
     * 大小为30×30像素的QQ空间头像URL。
     * @return
     */
    private String figureurl;

    /**
     * 大小为50×50像素的QQ空间头像URL。
     */
    private String figureurl_1;

    /**
     * 大小为100×100像素的QQ空间头像URL。
     */
    private String figureurl_2;

    /**
     * 大小为40×40像素的QQ头像URL。
     */
    private String figureurl_qq_1;

    /**
     * 大小为100×100像素的QQ头像URL。需要注意，不是所有的用户都拥有QQ的100x100的头像，但40x40像素则是一定会有。
     */
    private String figureurl_qq_2;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 用户出生年
     */
    private String year;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReturnUrl() {
        return returnUrl;
    }

    public void setReturnUrl(String returnUrl) {
        this.returnUrl = returnUrl;
    }

    public Integer getRet() {
        return ret;
    }

    public void setRet(Integer ret) {
        this.ret = ret;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFigureurl() {
        return figureurl;
    }

    public void setFigureurl(String figureurl) {
        this.figureurl = figureurl;
    }

    public String getFigureurl_1() {
        return figureurl_1;
    }

    public void setFigureurl_1(String figureurl_1) {
        this.figureurl_1 = figureurl_1;
    }

    public String getFigureurl_2() {
        return figureurl_2;
    }

    public void setFigureurl_2(String figureurl_2) {
        this.figureurl_2 = figureurl_2;
    }

    public String getFigureurl_qq_1() {
        return figureurl_qq_1;
    }

    public void setFigureurl_qq_1(String figureurl_qq_1) {
        this.figureurl_qq_1 = figureurl_qq_1;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
