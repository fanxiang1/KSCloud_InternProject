package com.ksyun.train.file;

import java.io.File;

/**
 * @author: sunjinfu
 * @date: 2023/6/14 14:32
 */
public class RenameFileDemo {

    public static void main(String[] args) {
        File source = new File("D://test/source.txt");
        File dir = new File("D://test/sub");
        dir.mkdir();
        File target = new File("D://test/sub/b.txt");
        System.out.println(source.renameTo(target));
    }
}
