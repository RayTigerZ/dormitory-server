package com.ray.dormitory.z;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author : Ray
 * @date : 2020.03.21 0:10
 */
public class IgnoreTest {
    public static void main(String[] args) throws IllegalAccessException, InstantiationException {
        Foo<String> foo = new Foo<String>() {
        };
        // 在类的外部这样获取
        Type type = ((ParameterizedType) foo.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        System.out.println(type);
        Type string = type.getClass().newInstance();
        // 在类的内部这样获取
        System.out.println(foo.getTClass());
    }
}


abstract class Foo<T> {
    public Class<T> getTClass() {
        Class<T> tClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return tClass;
    }
}
