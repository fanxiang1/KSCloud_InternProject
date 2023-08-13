package com.ksyun.train.file;

import java.io.File;

/**
 * @author: sunjinfu
 * @date: 2023/6/14 14:32
 */
public class FileDemo3 {

    public static void main(String[] args) {
        File sourceFile = new File("test/dir");
        System.out.println(sourceFile.getName());
        System.out.println(sourceFile.getAbsolutePath());
        System.out.println(sourceFile.getParent());
        System.out.println(sourceFile.getPath());
        System.out.println(sourceFile.length());
    }
}
