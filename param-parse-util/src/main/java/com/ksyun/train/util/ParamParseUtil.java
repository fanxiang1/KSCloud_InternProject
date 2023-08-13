package com.ksyun.train.util;

import com.sun.xml.internal.ws.util.StringUtils;

import java.lang.reflect.*;
import java.math.BigDecimal;
import java.util.*;

public class ParamParseUtil {
    public static <T> T parse(Class<T> clz, String queryString) throws Exception {
        if (queryString.length() == 0 || queryString == null || queryString.equalsIgnoreCase("null")) {
            return null;
        }
        TreeMap<String, String> map = str2Map(queryString);
        // 通过反射创建对象
        T instance = getInstance(clz);
        // 获取外部类的所有内部类class
        Class[] classList = clz.getDeclaredClasses();
        // 用来判断存储实例对象 ,key为实例对象名，如Container_2,value为实例对象
        HashMap<String, Object> inner_map = new HashMap<>();
        // 用来存储list表类型的数据
        HashMap<String, List<Object>> list = new HashMap<>();

//         使用 For-each 循环遍历 map
        for (Map.Entry<String, String> entry : map.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            // 如果key的首字母是小写，不符合规范，跳过
            if (!isUppercase(key)) {
                continue;
            }
            // 赋值
            reflectionKeyValue(key, value, classList, clz, inner_map, list, instance, "");
        }
        // t is one instance of T
        return instance;
    }

    /**
     * 遍历key，先判断key是否是class名，若是类名，需要再递归遍历，再从class静态内部类中找属性是否相同，若相同，赋值。
     *
     * @param key
     * @param classList
     * @param clz
     * @param inner_map
     * @param instance
     * @throws NoSuchFieldException
     */
    public static <T> void reflectionKeyValue(String key, String value, Class[] classList, Class<T> clz, HashMap<String, Object> inner_map, HashMap<String, List<Object>> list, Object instance, String pre) {
        try {
            // .需要转义
            String[] key_split = key.split("\\.");
            // 只是内部类，不是列表
            if (key.contains(".") && !key_split[0].contains("_")) {
                // 递归遍历
                ArrayList objArray = new ArrayList();
                for (int i = 1; i < key_split.length; i++) {
                    objArray.add(key_split[i]);
                }
                String s = objArray.toString();
                s = s.replaceAll(",", ".").replaceAll(" ", "");
                String substring = s.substring(1, s.length() - 1);//去除中括号。小心java的substring是左闭右开的
                // 判断是否有该内部类实例，没有的话则创建一个实例对象
                boolean flag = inner_map.containsKey(pre + key_split[0]);
                Object innerInstance = null;
                Class innerClass = getInnerClass(key_split[0].replaceAll("\\d+", ""), classList);
                ;
                if (!flag) {
                    innerInstance = getInnerInstance(innerClass);
                    inner_map.put(pre + key_split[0], innerInstance);
                } else {
                    innerInstance = inner_map.get(pre + key_split[0]);
                }
                reflectionKeyValue(substring, value, classList, innerClass, inner_map, list, innerInstance, key_split[0]);
                // 给属性赋值
                Field field = clz.getDeclaredField(lowFrist(key_split[0].replaceAll("\\d+", "")));
                setValue(innerInstance, instance, field, list, pre);
            } else if (key_split[0].contains("_")) {  //列表数据类型
                // 判断是否有该内部类实例，没有的话则创建一个list对象
                String[] s1 = key_split[0].split("_");
                boolean flag = list.containsKey(pre + lowFrist(s1[0]));
                if (!flag) {
                    ArrayList<Object> objects = new ArrayList<>();
                    list.put(pre + lowFrist(s1[0]), objects);
                }
                String substring = key.replace(key_split[0], s1[0] + s1[1]);
                reflectionKeyValue(substring, value, classList, clz, inner_map, list, instance, pre);
            } else if (!key.contains(".") && !key.contains("_")) { // 基本数据类型

                Field declaredField = clz.getDeclaredField(lowFrist(key_split[0].replaceAll("\\d+", "")));
                // 给属性赋值
                setValue(value, instance, declaredField, list, pre);
            }
        } catch (Exception e) {
            System.out.println("本类中没有该属性：" + key);
        }

    }


    /**
     * 给属性赋值
     *
     * @param value
     * @param instance
     * @param declaredField
     * @param <T>
     */
    public static <T> void setValue(Object value, Object instance, Field declaredField, HashMap<String, List<Object>> list, String pre) {
        // 如果该属性被自定义注解修饰，则工具类不去处理该属性
        if (declaredField.isAnnotationPresent(SkipMappingValueAnnotation.class)) {
            return;
        }
        // 获取属性类型
        Class<?> type = declaredField.getType();
        // private字段，直接访问会抛出IllegalAccessException
        if (!Modifier.isPublic(declaredField.getModifiers())) {
            // 强制访问
            declaredField.setAccessible(true);
        }
        // 处理异常情况，给原始类型赋默认值
        if (value == null || value.toString().equalsIgnoreCase("null")) {
            setDefault(type, declaredField, instance);
        } else {
            setvalue(type, value, declaredField, instance, list, pre);
        }
    }


    /**
     * 切分字符串
     *
     * @param queryString 传入的需要切分的字符串
     * @return 以map形式存储切分的字符
     */
    public static TreeMap<String, String> str2Map(String queryString) {
        // 读取queryString并分割成map
        String delimeter = "&";  // 指定分割字符
        String[] parts = queryString.split(delimeter);
        // 将parts字符数组划分成map保存，key保存属性名，value保存属性值
        TreeMap<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj1.compareTo(obj2);
                    }
                });
        for (String part : parts) {
            String[] data = part.split("=");
            if(data.length<2){ // 意思是不包含等号，就是乱码
                continue;
            }
            String param_key = data[0].trim();
            String param_value = data[1].trim();
            if (param_key.contains(".")) {
                String[] split = param_key.split("\\.");
                String key = split[0];
                for (int i = 0; i < split.length - 1; i++) {
                    if (!isNumeric(split[i]) && isNumeric(split[i + 1])) {
                        key += "_" + split[i + 1];
                    } else {
                        key += "." + split[i + 1];
                    }
                }
                // 加入到map中
                map.put(key, param_value);
            } else {
                map.put(param_key, param_value);
            }
        }
        return map;
    }

    /**
     * 判断str是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断字符的首字母是否大写
     *
     * @param str 需要判断的字符串
     * @return
     */
    public static boolean isUppercase(String str) {
        char c = str.charAt(0);
        return Character.isUpperCase(c);
    }

    /**
     * 将字符串首字母转小写
     *
     * @param str 需要转换的字符串
     * @return
     */
    public static String lowFrist(String str) {
        char[] cs = str.toCharArray();
        cs[0] += 32;
        return String.valueOf(cs);
    }

    /**
     * 获得clz的实例化对象
     *
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> T getInstance(Class<T> clz) {
        Constructor<T> constructor = null;
        try {
            constructor = clz.getDeclaredConstructor();
            if (Objects.isNull(constructor)) {
                throw new RuntimeException("not found no args constructor");
            }
            // 构造方法是private修饰符，则禁用安全检查开关，否则访问时会抛出IllegalAccessException
            if (!Modifier.isPublic(constructor.getModifiers())) {
                constructor.setAccessible(true);
            }
            // 实例化对象
            T instance = constructor.newInstance();
            return instance;
        } catch (Exception e) {
            System.out.println("创建实例对象失败");
            throw new RuntimeException();
        }
    }

    /**
     * 获得内部类的class
     *
     * @param key
     * @param classList
     * @return
     * @throws Exception
     */
    public static Class getInnerClass(String key, Class[] classList) throws Exception {
        for (Class<?> classObj : classList) {
            if (classObj == null) {
                continue;
            }
            String className = classObj.getSimpleName();
            if (className.equals(key)) {
                return classObj;
            }
        }
        return null;
    }

    /**
     * 获得内部类实例
     *
     * @param classObj
     * @return
     * @throws Exception
     */
    public static <T> Object getInnerInstance(Class classObj) throws Exception {
        // 静态内部类直接创建即可
        Object innerInstance = classObj.newInstance();
        return innerInstance;
    }

    /**
     * 给不同的基本数据类型赋值
     *
     * @param type
     * @param value
     * @param declaredField
     * @param instance
     * @param <T>
     * @throws IllegalAccessException
     */
    public static <T> void setvalue(Class type, T value, Field declaredField, T instance, HashMap<String, List<Object>> list, String pre) {
        try {
            // 放入字符串类型
            if (type == String.class) {
                declaredField.set(instance, value);
            } else if (type == byte.class || type == Byte.class) {
                declaredField.set(instance, Byte.parseByte((String) value));
            } else if (type == short.class || type == Short.class) {
                declaredField.set(instance, Short.parseShort((String) value));
            } else if (type == int.class || type == Integer.class) {
                declaredField.set(instance, Integer.parseInt((String) value));
            } else if (type == long.class || type == Long.class) {
                declaredField.set(instance, Long.parseLong((String) value));
            } else if (type == float.class || type == Float.class) {
                declaredField.set(instance, Float.parseFloat((String) value));
            } else if (type == double.class || type == Double.class) {
                declaredField.set(instance, Double.parseDouble((String) value));
            } else if (type == char.class || type == Character.class) {
                declaredField.set(instance, value);
            } else if (type == boolean.class || type == Boolean.class) {
                declaredField.set(instance, Boolean.parseBoolean((String) value));
            } else if (type == BigDecimal.class) {
                declaredField.set(instance, new BigDecimal((String) value));
            } else if (type == List.class) {
                if (list.containsKey(pre + declaredField.getName())) {
                    List<Object> objects = list.get(pre + declaredField.getName());
                    if (!objects.contains(value)) {
                        objects.add(value);
                    }
                    declaredField.set(instance, objects);
                }
            } else {
                declaredField.set(instance, value);
            }
        } catch (Exception e) {
            System.out.println("属性赋值失败,请检查参数类型：" + type);
        }
    }

    /**
     * 给不同的数据类型赋初始值
     *
     * @param type
     * @param declaredField
     * @param instance
     * @param <T>
     * @throws IllegalAccessException
     */
    public static <T> void setDefault(Class type, Field declaredField, T instance) {
        try {
            // 放入字符串类型
            if (type == String.class) {
                declaredField.set(instance, "");
            } else if (type == byte.class || type == short.class || type == int.class || type == long.class) {
                declaredField.set(instance, 0);
            } else if (type == float.class) {
                declaredField.set(instance, 0.0f);
            } else if (type == double.class) {
                declaredField.set(instance, 0.0d);
            } else if (type == boolean.class) {
                declaredField.set(instance, false);
            } else if (type == char.class) {
                declaredField.set(instance, 0);
            } else if (type == Byte.class || type == Short.class || type == Integer.class || type == Long.class || type == Float.class || type == Double.class || type == Character.class || type == Boolean.class) {
                declaredField.set(instance, null);
            } else { // 其他类型都置空即可
                declaredField.set(instance, null);
            }
        } catch (Exception e) {
            System.out.println("属性初始化失败，请检查对应的数据类型：" + type);
        }

    }
}

