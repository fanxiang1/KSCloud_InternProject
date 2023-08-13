package com.ksyun.train.generics;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @author: sunjinfu
 * @date: 2023/6/7 17:35
 */
public class DataBox {

    private List<String> dataList;

    private Map<Integer, BigDecimal> dataMap;

    private Box<String> box;

    public static void main(String[] args) {
        Field[] fields = DataBox.class.getDeclaredFields();
        if (fields.length == 0) {
            return;
        }
        for (Field field : fields) {
            System.out.println("\n" + field.getName() + ":");
            Type type = field.getGenericType();
            if (type instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type;
                Type[] types = parameterizedType.getActualTypeArguments();
                for (Type t : types) {
                    System.out.print(t.getTypeName() + " ");
                }
            } else {
                System.out.println(type.getTypeName());
            }
        }
    }
}
