package com.ksyun.train.io;

import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * @author: sunjinfu
 * @date: 2023/6/13 17:12
 */
public class WriteLineString {

    public static void main(String[] args) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("D://test/target.txt"))) {
            bw.write("2023");
            bw.newLine();
            bw.write("\r\n");
            bw.newLine();
            bw.write("Ksyun 星云训练营");
            bw.flush();
        }
    }
}
