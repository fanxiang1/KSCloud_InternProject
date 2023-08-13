package com.ksyun.train.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author: sunjinfu
 * @date: 2023/6/8 15:09
 */
public class CopyBytes {

    public static void main(String[] args) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        try {
            // 文件数据输入流，如果文件不存在或者是一个目录，则发生FileNotFoundException
            fis = new FileInputStream("D://test/source.txt");
            // 数据输出流，目标文件不存在，大部分系统都会自动创建，如果文件已存在，则覆盖文件中已有内容
            fos = new FileOutputStream("D://test/target.txt");
            int c;
            // 如果返回-1，表示已经读到流的末端
            while ((c = fis.read()) != -1) {
                // 如果是ascii字符，每个字符对应的整型值一定是0~127，
                // 如果是汉字，而一个汉字由多个字节组成，因此会输出多个整型数据，128~255
                System.out.print(c + " ");
                fos.write(c);
            }
            fos.write(256);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 从输入流读取数据完成后，一定要关闭流，释放系统资源
            if (fis != null) {
                fis.close();
            }
            // 将数据写入输出流完成后，一定要关闭流，释放系统资源
            if (fos != null) {
                fos.close();
            }
        }
    }
}
