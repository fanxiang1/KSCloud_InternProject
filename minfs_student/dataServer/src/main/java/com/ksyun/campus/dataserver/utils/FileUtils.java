package com.ksyun.campus.dataserver.utils;

import java.io.File;

public class FileUtils {

    /**
     * 获得当前文件及其下面文件的大小
     * @param file
     * @return
     */
    public long getDirSize(File file) {
        //判断文件是否存在
        if (file.exists()) {
            //如果是目录则递归计算其内容的总大小
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                long size = 0;
                for (File f : children) {
                    size += getDirSize(f);
                }
                return size;
            } else {//如果是文件则直接返回其大小,以“兆”为单位
                long size = file.length();
                return size;
            }
        } else {
            //System.out.println("文件或者文件夹不存在，请检查路径是否正确！");
            return 0;
        }
    }

    /**
     * 获得当前文件夹下文件的数量
     * @param file
     * @return
     */
    public int getDirCount(File file) {
        int fileCount=0;
        // 判断文件是否存在
        if(file.exists()){
            if(file.isDirectory()){
                File list[] = file.listFiles();
                for (int i = 0; i < list.length; i++) {
                    if (list[i].isFile()) {
                        fileCount++;
                    }
                }
            }
        }
        return fileCount;
    }
}
