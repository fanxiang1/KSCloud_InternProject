package com.ksyun.train.utils;

import java.io.*;
import java.util.List;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {
    static final int  BUFFER_SIZE = 2 * 1024;
    /**
     * 将压缩成zip文件 use-plugin-demo-1.0.zip
     * @param zip
     * @param bootJar
     * @param dependencyJars
     * @throws RuntimeException
     */
    public static void createZip(File zip, File bootJar, List<File> dependencyJars) throws RuntimeException {
        try (ZipOutputStream zos= new ZipOutputStream(new FileOutputStream(zip))){
            // 先将bootJar写入
            byte[] buf = new byte[BUFFER_SIZE];
            int len;
            zos.putNextEntry(new ZipEntry(bootJar.getName()));
            FileInputStream in = new FileInputStream(bootJar);
            while ((len = in.read(buf)) != -1){
                zos.write(buf, 0, len);
            }
            zos.closeEntry();
            // 再依次写入对应的依赖包
            if(Objects.nonNull(dependencyJars) && dependencyJars.size()>0){
                for (File jar : dependencyJars) {
                    buf = new byte[BUFFER_SIZE];
                    zos.putNextEntry(new ZipEntry("lib/"+jar.getName()));
                    in = new FileInputStream(jar);
                    while ((len = in.read(buf)) != -1){
                        zos.write(buf, 0, len);
                    }
                    zos.closeEntry();
                }
            }
            zos.flush();
            in.close();
        }catch (Exception e){
            throw new RuntimeException("error creating zip",e);
        }
    }
}
