package com.ksyun.train.io;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author: sunjinfu
 * @date: 2023/6/14 9:39
 */
public class BufferedWriterDemo {

    public static void main(String[] args) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("D://test/target.txt"))) {
            bw.write("2023,星云训练营");
            // 自动获取操作系统换行符写入到文件中
            bw.newLine();
            // 写入空行
            bw.newLine();
            bw.write("2023,星云训练营");
            // 写入结束，最后一行后未添加换行符
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
