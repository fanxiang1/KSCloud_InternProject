package com.ksyun.train.io;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: sunjinfu
 * @date: 2023/6/13 20:03
 */
public class BufferedOutputStreamDemo {

    public static void main(String[] args) {
        try (BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream("D://test/target.txt"))) {
            // 写入字符 '2' 到内存buffer
            bos.write(50);
            // 写入字符 '0' 到内存buffer
            bos.write(48);
            // 写入字符 '2' 到内存buffer
            bos.write(50);
            // 写入字符 '3' 到内存buffer
            bos.write(51);
// 可在此 sleep 10s，去检测文件内容， 10s后调用了flush方法文件内容才写入
//        try {
//            Thread.sleep(10000);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
            // 刷新输出流，将内存buffer数据写到文件中
            // 缓冲流的close方法调用时，会先自动调用一次flush方法，强制将数据写出
            // 但是明确后续没有数据可写时，最好养成良好习惯，手动调用一次flush方法
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
