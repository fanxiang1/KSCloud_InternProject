package com.ksyun.train.io;

/**
 * @author: sunjinfu
 * @date: 2023/6/23 0:12
 */
public class StringEncodeDemo {
    public static void main(String[] args) throws Exception {
        String fileEncoding = System.getProperties().getProperty("file.encoding");
        System.out.println("JVM默认编码: " + fileEncoding);
        String str = "2023,星云训练营";
        System.out.println(new String(str.getBytes(), "GBK"));
    }
}
