package com.ksyun.train.file;

import java.io.File;

/**
 * @author: sunjinfu
 * @date: 2023/6/14 18:12
 */
public class DeleteTxtFileDemo {

    public static void main(String[] args) {
        File file = new File("D://test");
        File[] files = file.listFiles((dir, name) -> name.endsWith(".txt"));
        if (files != null) {
            for (File f : files) {
                f.delete();
            }
        }
    }
}
