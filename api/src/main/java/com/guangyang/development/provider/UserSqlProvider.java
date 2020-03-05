package com.guangyang.development.provider;

import com.google.common.base.CaseFormat;
import com.guangyang.development.bean.User;
import com.guangyang.development.utils.ReflectionUtil;
import org.apache.ibatis.jdbc.SQL;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

public class UserSqlProvider {
    public String updateUser(final User u) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return new SQL(){{
            UPDATE("user");
            List<Map<String, Object>> attributesAndValue = ReflectionUtil.getAttributesAndValue(u);
            for (Map<String, Object> stringObjectMap : attributesAndValue) {
                if (stringObjectMap.get("value") != null) {
                    String name = (String) stringObjectMap.get("name");
                    // 使用谷歌的驼峰转换工具进行转换，比如属性名为testDate,转换结果为test_date
                    String caseFormat = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name);
                    SET(caseFormat + "=#{" + name + "}");
                    System.out.println(name);
                }
            }
            WHERE("username=#{username}");
        }}.toString();
    }
}
