package com.ksyun.train.io;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

/**
 * @author: sunjinfu
 * @date: 2023/6/29 10:12
 */
public class BufferedWriterDemo2 {
    public static void main(String[] args) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream("D://test/target.txt")))) {
            bw.write("2023,星云训练营");
            // windows换行符
            // bw.write("\r\n");
            bw.newLine();
            bw.write("2023,星云训练营");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
