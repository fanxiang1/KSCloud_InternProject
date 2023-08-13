package com.ksyun.train.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author: sunjinfu
 * @date: 2023/6/22 21:49
 */
public class FileOutputStreamDemo {

    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("D://test/target.txt", true)) {
            String s = "2023,Ksyun 星云训练营";
            // 调用FileOutputStream的write(byte b[])方法，写入多个字节
            fos.write(s.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
