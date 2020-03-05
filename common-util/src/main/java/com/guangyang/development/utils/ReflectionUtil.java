package com.guangyang.development.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 反射工具类
 */
public class ReflectionUtil {
    /**
     * 返回一个类中所有的属性(Attributes)以及值
     * 返回值中是一个List集合，集合中是Map集合，map集合只包含两个键值对
     * name --- 属性名
     * value --- 属性值
     * @param object
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    public static List<Map<String,Object>> getAttributesAndValue(Object object) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        List<Map<String,Object>> list = new ArrayList<>();

        // 获取实体类的所有属性，返回Field数组
        Field[] field = object.getClass().getDeclaredFields();
        for(int i = 0; i < field.length;i++){
            Map<String,Object> map = new HashMap<>();

            // 获取属性的名字
            String name = field[i].getName();
            map.put("name",name);

            // 将属性的首字母大写
            name = name.replaceFirst(name.substring(0,1),name.substring(0,1).toUpperCase());

            // 获取属性类型名字
            String type = field[i].getGenericType().toString();

            if (type.equals("class java.lang.String"))
            {
                // 如果type是类类型，则前面包含"class "，后面跟类名
                Method m = object.getClass().getMethod("get" + name);
                // 调用getter方法获取属性值
                String value = (String) m.invoke(object);
                map.put("value",value);
            }

            if (type.equals("class java.lang.Integer"))
            {
                Method m = object.getClass().getMethod("get" + name);
                Integer value = (Integer) m.invoke(object);
                map.put("value",value);
            }
            if (type.equals("class java.lang.Short"))
            {
                Method m = object.getClass().getMethod("get" + name);
                Short value = (Short) m.invoke(object);
                map.put("value",value);
            }
            if (type.equals("class java.lang.Double"))
            {
                Method m = object.getClass().getMethod("get" + name);
                Double value = (Double) m.invoke(object);
                map.put("value",value);
            }
            if (type.equals("class java.lang.Boolean"))
            {
                Method m = object.getClass().getMethod("get" + name);
                Boolean value = (Boolean) m.invoke(object);
                map.put("value",value);
            }
            if (type.equals("class java.util.Date"))
            {
                Method m = object.getClass().getMethod("get" + name);
                Date value = (Date) m.invoke(object);
                map.put("value",value);
            }

            list.add(map);
        }

        return list;
    }

//    /**
//     * 测试反射，获取类中的所有属性和值
//     */
//    public static void main(String[] args)  throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        Video video = new Video();
//        video.setCoverImg("哈哈哈");
//        video.setPrice("1234");
//        List<Map<String, Object>> list = getAttributesAndValue(video);
//        for (Map<String, Object> stringObjectMap : list) {
//            System.out.println(stringObjectMap.get("name") + " = " + stringObjectMap.get("value"));
//        }
//    }
}
