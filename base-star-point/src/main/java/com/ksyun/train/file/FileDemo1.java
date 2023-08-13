package com.ksyun.train.file;

import java.io.File;

/**
 * @author: sunjinfu
 * @date: 2023/6/14 14:32
 */
public class FileDemo1 {

    public static void main(String[] args) {
        File dir = new File("D://test");

        File sourceFile1 = new File("D://test", "source.txt");

        File sourceFile2 = new File(dir, "source.txt");

        System.out.println(dir.exists());
        System.out.println(sourceFile1.exists());
        System.out.println(sourceFile2.exists());
    }
}
