package com.ksyun.train.file;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @author: sunjinfu
 * @date: 2023/6/14 19:33
 */
public class FilesDemo {

    public static void main(String[] args) throws IOException {
     //   Files.createDirectory(Paths.get("D://test"));
        Files.createFile(Paths.get("D://test3/source.txt"));
//        Files.delete(Paths.get("D://test/source.txt"));
    }
}
