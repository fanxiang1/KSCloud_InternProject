package com.ksyun.train.file;

import java.io.File;
import java.io.IOException;

/**
 * @author: sunjinfu
 * @date: 2023/6/29 9:00
 */
public class CreateFileDemo {

    public static void main(String[] args) throws IOException {
        File file = new File("D://test/txt");
        boolean suc = file.createNewFile();
        System.out.println(suc); // true，创建成功
        suc = file.createNewFile();
        System.out.println(suc); // false, 文件已经存在
        suc = file.mkdir();
        System.out.println(suc); // fasle, 相同路径名的文件已经存在，无法创建目录

        File dir = new File("D://test/a/b/c");
        suc = dir.mkdir();
        System.out.println(suc); // false，无法创建目录c, 父目录D://test/a/b不存在
        suc = dir.mkdirs();
        System.out.println(suc); // true，整个目录D://test/a/b/c全部创建
    }
}
