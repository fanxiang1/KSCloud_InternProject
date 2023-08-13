package com.ksyun.train.file;

import java.io.File;

/**
 * @author: sunjinfu
 * @date: 2023/6/29 8:57
 */
public class FileDemo2 {

    public static void main(String[] args) {
        File hiddenSshDir = new File("D://test/hidden");
        System.out.println(hiddenSshDir.isHidden());
        System.out.println(hiddenSshDir.isFile());
        System.out.println(hiddenSshDir.isDirectory());
        System.out.println(hiddenSshDir.exists());
    }

}
