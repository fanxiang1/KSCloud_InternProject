package com.ksyun.train.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

/**
 * @author: sunjinfu
 * @date: 2023/6/9 11:06
 */
public class CopyCharacters {

    public static void main(String[] args) {
        try (InputStreamReader isr = new InputStreamReader(
                new FileInputStream("D://test/source.txt"));
             OutputStreamWriter osw = new OutputStreamWriter(
                     new FileOutputStream("D://test/target.txt"))) {
            int c;
            while((c=isr.read()) != -1) {
                System.out.println(c);
                osw.write(c);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
