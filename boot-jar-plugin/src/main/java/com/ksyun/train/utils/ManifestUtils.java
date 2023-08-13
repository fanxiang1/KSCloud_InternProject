package com.ksyun.train.utils;

import java.util.Objects;
import java.util.jar.Manifest;

public class ManifestUtils {
    /**
     * 创建manifest文件,存入对应的数据即可
     * @param mainClass 程序的主类
     * @param classPath 运行主类所依赖的外部类的路径
     * @return
     */
    public static Manifest createManifest(String mainClass, String classPath){
        Manifest manifest = new Manifest(); // 新建manifest类
        // 存入对应的数据
        manifest.getMainAttributes().putValue("Manifest-Version","1.0");
        manifest.getMainAttributes().putValue("Created-By","fanxiang1");
        // 看一下需要的内容是否存在，若存在则写入manifest
        if(Objects.nonNull(mainClass) && mainClass.length()>0){
            manifest.getMainAttributes().putValue("Main-Class",mainClass);
        }
        if(Objects.nonNull(classPath) && classPath.length()>0){
            manifest.getMainAttributes().putValue("Class-Path",classPath);
        }
        return manifest;
    }
}
