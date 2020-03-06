package com.guangyang.development.login.mapper;

import com.guangyang.development.bean.User;
import com.guangyang.development.provider.UserSqlProvider;
import org.apache.ibatis.annotations.UpdateProvider;


public interface UserMapper {
    /**
     * 通过id查找用户查找用户
     * @param id
     * @return
     */
    User findUserById(String id);

    User fingUserByUsername(String username);

    @UpdateProvider(type = UserSqlProvider.class,method = "updateUser")
    void updateUser(User user);

    /**
     * 通过openId查询数据库
     * @param openId
     * @return
     */
    User findUserByOpenId(String openId);
}
