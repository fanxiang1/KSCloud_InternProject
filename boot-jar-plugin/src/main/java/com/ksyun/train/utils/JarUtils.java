package com.ksyun.train.utils;


import java.io.*;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

public class JarUtils {
    static final int BUFFER_SIZE = 2 * 1024;

    // manifest是文件
    // classesDirectory是目录,获取其下面的文件和文件夹
    public static void createJar(File bootJar, Manifest manifest, File classesDirectory) {
        // 先将manifest直接写入
        try (JarOutputStream jos = new JarOutputStream(new FileOutputStream(bootJar), manifest)) {
            // 将classes里面文件夹放入jar包中
            add("", classesDirectory, jos);
        } catch (Exception e) {
            throw new RuntimeException("error creating JAR file", e);
        }
    }

    private static void add(String name, File basepath, JarOutputStream jos) throws Exception {
        File[] files = basepath.listFiles();
        if (files != null) {
            for (File file : files) {
                //将目录下所有的文件打进jar包中
                if (file.isFile()) {
                    addFileToJar(name + file.getName(), file, jos);
                } else if (file.isDirectory()) {
                    //递归添加
                    add(name + file.getName() + "/", file, jos);
                }
            }
        }
    }

    //将文件添加到jar文件中
    private static void addFileToJar(String entryName, File file, JarOutputStream jos) throws Exception {
        JarEntry entry = new JarEntry(entryName);
        jos.putNextEntry(entry);
        writeFile(jos, file);
        jos.closeEntry();
    }

    public static void writeFile(OutputStream os, File file) throws Exception {
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] buf = new byte[BUFFER_SIZE];
            int len;
            while ((len = fis.read(buf)) != -1) {
                os.write(buf, 0, len);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
