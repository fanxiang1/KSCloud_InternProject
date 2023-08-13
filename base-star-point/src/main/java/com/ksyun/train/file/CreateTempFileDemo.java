package com.ksyun.train.file;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

/**
 * @author: sunjinfu
 * @date: 2023/6/14 14:32
 */
public class CreateTempFileDemo {

    public static void main(String[] args) throws IOException {
        Properties properties = System.getProperties();
        properties.forEach((k,v) -> System.out.println(k + "=" + v));
        System.out.println(File.createTempFile("test-", ".log"));
    }
}
