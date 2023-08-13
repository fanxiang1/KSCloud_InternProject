package com.ksyun.train.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: sunjinfu
 * @date: 2023/6/8 15:45
 */
public class CopyBytes2 {
    public static void main(String[] args) {
        try (FileInputStream fis = new FileInputStream("D://test/source.txt");
             FileOutputStream fos = new FileOutputStream("D://test/target.txt")) {
            int c;
            // 内容长度(可读取的字节数量)
            System.out.println("内容有效字节数: " + fis.available());
            while ((c = fis.read()) != -1) {
                System.out.print(c + " ");
                fos.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
