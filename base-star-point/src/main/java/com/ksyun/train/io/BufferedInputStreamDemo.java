package com.ksyun.train.io;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * @author: sunjinfu
 * @date: 2023/6/13 18:54
 */
public class BufferedInputStreamDemo {

    public static void main(String[] args) {
        try (BufferedInputStream bis = new BufferedInputStream(
                new FileInputStream("D://test/source.txt"))) {
            int c;
            // 从内存buffer中读取字节数据，如果buffer为空，才会进行IO操作
            while((c = bis.read()) != -1) {
                System.out.println(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
