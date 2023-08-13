package com.ksyun.train.io;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author: sunjinfu
 * @date: 2023/6/13 17:32
 */
public class PrintWriterDemo {

    public static void main(String[] args) {
        try (PrintWriter pw = new PrintWriter(new FileWriter("D://test/target.txt"))) {
            // 自动添加换行符
            pw.println("2023,星云训练营");
            // 空行
            pw.println();
            // 自动添加换行符
            pw.println("2023,星云训练营");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
