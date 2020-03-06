package com.guangyang.development.register.mapper;

import com.guangyang.development.bean.User;
import com.guangyang.development.provider.UserSqlProvider;
import org.apache.ibatis.annotations.UpdateProvider;


public interface UserMapper {
    @UpdateProvider(type = UserSqlProvider.class,method = "updateUser")
    void updateUser(User user);

    /**
     * 通过用户名查找用户
     * @param username
     * @return
     */
    User fingUserByUsername(String username);

    /**
     * 保存用户
     * @param u
     */
    void saveUser(User u);
}
