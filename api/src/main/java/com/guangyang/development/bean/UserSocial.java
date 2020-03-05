package com.guangyang.development.bean;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * 用户社交登录表
 */
public class UserSocial implements Serializable {

  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Id
  private String id;

  private String userId;
  /**
   * 社交平台开放ID
   */
  private String openId;
  /**
   * 平台(1=PC,2=Wap,3=Android,4=iOS)
   */
  private String platform;
  /**
   * 渠道(0=本网站登录,1=QQ,2=SinaWeibo,3=Weixin
   */
  private String channel;
  private java.util.Date createTime;
  private java.util.Date updateTime;


  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }


  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }


  public String getOpenId() {
    return openId;
  }

  public void setOpenId(String openId) {
    this.openId = openId;
  }


  public String getPlatform() {
    return platform;
  }

  public void setPlatform(String platform) {
    this.platform = platform;
  }


  public String getChannel() {
    return channel;
  }

  public void setChannel(String channel) {
    this.channel = channel;
  }


  public java.util.Date getCreateTime() {
    return createTime;
  }

  public void setCreateTime(java.util.Date createTime) {
    this.createTime = createTime;
  }


  public java.util.Date getUpdateTime() {
    return updateTime;
  }

  public void setUpdateTime(java.util.Date updateTime) {
    this.updateTime = updateTime;
  }

}
