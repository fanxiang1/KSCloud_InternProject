package com.ksyun.train;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Test {
    /**
     * 给内部类对象obj的属性赋值
     *
     * @param key
     * @param value
     * @param clz        外部类，用来获取所有的内部类对象
     * @param targetName 要赋值的内部类名称
     * @param obj        # 内部类实体对象
     * @throws Exception
     */
    // 给内部类对象赋值,返回空
    public static void setInnerValue(String key, String value, Class clz, Object targetName, Object obj) throws Exception {
        Class[] classList = clz.getDeclaredClasses();
        for (Class<?> classObj : classList) {
            if (classObj == null) {
                continue;
            }
            String className = classObj.getSimpleName();
            if (className.equals(targetName)) {
                //获取修饰符的整数编码
                int mod = classObj.getModifiers();
                //返回整数编码对应的修饰符的字符串对象
                String modifier = Modifier.toString(mod);
                //找到被private修饰的内部类
                if (modifier.contains("private")) {
                    //根据内部类的特性，需要由外部类来反射获取内部类的构造方法（这里获取的是内部类的默认构造方法）
                    Constructor cc = classObj.getDeclaredConstructor(clz);
                    //由于内部类是私有的，需要强制获取构造方法的访问权限
                    cc.setAccessible(true);
                    //获取内部类的私有成员属性inner
                    Field f = classObj.getDeclaredField(key);
                    Class<?> type = f.getType();
                    // 放入内部类对象中
//                    setvalue(type, value, f, obj);
                }
            }
        }
    }
}

// 如果key中只包含.不包含数字，其为Object类型
//            if (key.contains(".") && !key.matches("(.*)10(.*)")) {
//                    String[] key_split = key.split("\\.");
//                    // 不需要创建新的实例对象，直接给对应属性赋值
////                if(set.contains(key_split[0])){
////
////                }else{
////                    // 获得对应的内部类class
////                    Class innerClass = getInnerClass(key_split[0], declaredClasses);
////                    Object innerInstance = innerClass.newInstance();
////                    Field declaredField = innerClass.getDeclaredField(key_split[1]);
////
////                }
//                    }
//                    String[] key_split = key.split("\\.");
//        Class<?>[] declaredClasses = clz.getDeclaredClasses();
//        for (Class<?> classObj : declaredClasses) {
//        if (classObj == null) {
//        continue;
//        }
//        String className = classObj.getSimpleName();
//        classNameList.add(className);
//        }
