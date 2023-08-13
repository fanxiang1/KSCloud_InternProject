package com.ksyun.train.io;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author: sunjinfu
 * @date: 2023/6/23 11:08
 */
public class BufferedReaderDemo {
    public static void main(String[] args) {
        try (BufferedReader br = new BufferedReader(new FileReader("D://test/source.txt"))) {
            String line;
            // 按行读取，自动处理回车换行符
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
