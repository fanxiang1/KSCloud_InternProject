package com.ksyun.train.io;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: sunjinfu
 * @date: 2023/6/9 11:06
 */
public class CopyFileCharacters {

    public static void main(String[] args) {
        try (FileReader fr = new FileReader("D://test/source.txt");
             FileWriter fw = new FileWriter("D://test/target.txt")) {
            // 查看FileReader读取字符编码
            System.out.println(fr.getEncoding());
            // 查看FileWriter写入字符编码
            System.out.println(fw.getEncoding());
            int c;
            while ((c = fr.read()) != -1) {
                System.out.println(c);
                fw.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
