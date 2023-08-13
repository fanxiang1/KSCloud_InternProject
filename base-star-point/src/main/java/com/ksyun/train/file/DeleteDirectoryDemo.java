package com.ksyun.train.file;

import java.io.File;
import java.util.Objects;

/**
 * @author: sunjinfu
 * @date: 2023/6/14 18:57
 */
public class DeleteDirectoryDemo {

    public static void main(String[] args) {
        File file = new File("D://test");
        deleteFile(file);
    }

    public static void deleteFile(File file) {
        if (!file.exists()) {
            return;
        }
        if (file.isFile()) {
            if (!file.delete()) {
                System.out.println("failed to delete file: " +  file.getAbsolutePath());
            }
            return;
        }
        File[] files = file.listFiles();
        if (Objects.nonNull(files) && files.length > 0) {
            for (File f : files) {
                deleteFile(f);
            }
        }
        if (!file.delete()) {
            System.out.println("failed to delete directory: " +  file.getAbsolutePath());
        }
    }
}
