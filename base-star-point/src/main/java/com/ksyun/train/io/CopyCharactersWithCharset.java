package com.ksyun.train.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

/**
 * @author: sunjinfu
 * @date: 2023/6/9 11:06
 */
public class CopyCharactersWithCharset {

    public static void main(String[] args) {
        try (InputStreamReader isr = new InputStreamReader(
                // 指定以UTF-8编码读取字符，而字符存储是ANSI，因此无法解码导致乱码
                new FileInputStream("D://test/source.txt"), StandardCharsets.UTF_8);
             // 内容乱码与OutputStreamWriter无任何直接关系
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
