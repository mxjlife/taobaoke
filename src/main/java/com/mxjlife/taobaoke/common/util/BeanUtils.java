package com.mxjlife.taobaoke.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * description:
 * author mxj
 * email mengxiangjie@hualala.com
 * date 2020/2/28 10:48
 */
public class BeanUtils {
    /**
     * map2javabean 把map对象转换为javabean
     */
    public  static <T> T map2bean(Map<String, Object> map, Class<T> beantype){
        T object = null;
        try {
            //创建对象
            object = beantype.newInstance();
            //获取类的属性描述器
            BeanInfo beaninfo= Introspector.getBeanInfo(beantype,Object.class);
            //获取类的属性集
            PropertyDescriptor[] pro=beaninfo.getPropertyDescriptors();
            for (PropertyDescriptor property : pro) {
                //获取属性的名字
                String name=property.getName();
                //得到属性name在map中对应的value。
                Object value=map.get(name);
                //得到属性的set方法
                Method set=property.getWriteMethod();
                //接下来将map的value转换为属性的value
                //执行set方法
                set.invoke(object, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    /**
     * 将javabean转换为map
     */
    public static Map javabean2map(Object bean, int resType){
        Map map = null;
        if(resType == 1){
            map = new HashMap<String, String>();
        }else {
            map = new HashMap<String, Object>();
        }
        try {
            //获取类的属性描述器
            BeanInfo beaninfo = Introspector.getBeanInfo(bean.getClass(), Object.class);
            //获取类的属性集
            PropertyDescriptor[] pro = beaninfo.getPropertyDescriptors();
            for (PropertyDescriptor property : pro) {
                //得到属性的name
                String key = property.getName();
                Method get = property.getReadMethod();
                //执行get方法得到属性的值
                Object value = get.invoke(bean);
                if(value == null){
                    continue;
                }
                if(resType == 1){
                    map.put(key, String.valueOf(value));
                }else {
                    map.put(key, value);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
