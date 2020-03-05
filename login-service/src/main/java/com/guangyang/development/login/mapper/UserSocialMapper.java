package com.guangyang.development.login.mapper;

import com.guangyang.development.bean.UserSocial;
import org.apache.ibatis.annotations.Param;

public interface UserSocialMapper {

    /**
     * 通过社交平台id以及渠道字段，获取用户社交信息
     * @param openId
     * @param channel
     * @return
     */
    UserSocial findUserSocialByOpenIdAndChannel(@Param(value = "openId") String openId,
                                                @Param(value = "channel") String channel);

    /**
     * 更新用户社交登录表
     * @param userSocial
     */
    void saveUserSocial(UserSocial userSocial);

    /**
     * 通过用户id查询用户是否绑定其他账号
     * @param id
     * @return
     */
    UserSocial findUserSocialByUserId(@Param(value = "userId")String id);
}
