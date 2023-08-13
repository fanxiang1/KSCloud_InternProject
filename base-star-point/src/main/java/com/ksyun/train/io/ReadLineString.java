package com.ksyun.train.io;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * @author: sunjinfu
 * @date: 2023/6/13 16:58
 */
public class ReadLineString {

    public static void main(String[] args) throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader("D://test/source.txt"))) {
            String line;
            while((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
