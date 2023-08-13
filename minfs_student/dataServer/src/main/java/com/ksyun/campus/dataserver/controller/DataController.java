package com.ksyun.campus.dataserver.controller;

import com.ksyun.campus.dataserver.services.DataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;

@RestController("/")
@Slf4j
public class DataController {

    @Autowired
    private DataService dataService;

    @Value("${server.port}")
    private int port;

    ;

    /**
     * 1、读取request content内容并保存在本地磁盘下的文件内
     * 2、同步调用其他ds服务的write，完成另外2副本的写入
     * 3、返回写成功的结果及三副本的位置
     *
     * @param path
     * @param offset
     * @param length
     * @return
     */
    @RequestMapping(value = "write", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity writeFile(@RequestHeader(required = false) String fileSystem,@RequestBody byte[] bytes, @RequestParam("path") String path, @RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "length", defaultValue = "0") int length) throws FileNotFoundException {
        try {
            dataService.write(path, bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return new ResponseEntity(HttpStatus.OK);
    }


    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    @RequestMapping(value = "create", method = RequestMethod.POST, produces = "application/json")
    public String createFile(@RequestHeader(required = false) String fileSystem,@RequestParam String path) throws Exception {
        File directory = new File("");
        boolean b = path.startsWith("/");//true
        if (!b) {
            path = "/" + path;
        }
        String filePath = directory.getAbsolutePath() + "/" + "data_" + port + path;

        File file = new File(filePath);
        // 创建目录
        if (!file.exists()) {
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();// 目录不存在的情况下，创建目录。
            }
            file.createNewFile(); // 在上级目录里面创建文件
        } else {
            // 覆盖创建
            file.delete();
            file.createNewFile();
            //System.out.println("文件已存在");
        }
        // 返回文件保存的位置
        return filePath;
    }


    /**
     * 创建文件夹
     *
     * @param path
     * @return
     */
    @RequestMapping(value = "mkdir", method = RequestMethod.POST, produces = "application/json")
    public String mkdir(@RequestHeader(required = false) String fileSystem,@RequestParam String path) throws Exception {
        File directory = new File("");
        boolean b = path.startsWith("/");//true
        if (!b) {
            path = "/" + path;
        }
        String filePath = directory.getAbsolutePath() + "/" + "data_" + port + path;
        File file = new File(filePath);
        // 创建目录
        if (!file.exists()) {
            // 创建文件夹
            file.mkdirs();
        } else {
            // 覆盖创建
            file.delete();
            file.mkdirs();
            //System.out.println("文件夹已存在");
        }
        return filePath;
    }


    /**
     * 删除文件或文件夹
     *
     * @param path
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.POST, produces = "application/json")
    public String delete(@RequestHeader(required = false) String fileSystem,@RequestParam String path) throws Exception {
        File directory = new File("");
        boolean b = path.startsWith("/");//true
        if (!b) {
            path = "/" + path;
        }
        String filePath = directory.getAbsolutePath() + "/" + "data_" + port + path;

        File file = new File(filePath);
        // 删除文件或目录
        if (!file.exists()) {
            log.info("文件或文件夹不存在");
            return "文件或文件夹不存在";
        } else {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files.length > 0) {
                    log.info("该文件夹下存在数据，无法删除");
                    return "该文件夹下存在数据，无法删除";
                } else {
                    file.delete();
                    return "删除成功";
                }
            } else {
                file.delete();
                return "删除成功";
            }
        }
    }


    /**
     * 在指定本地磁盘路径下，读取指定大小的内容后返回
     *
     * @param path
     * @param offset
     * @param length
     * @return
     */
    @RequestMapping(value = "read", method = RequestMethod.POST)
    public ResponseEntity<InputStreamResource> readFile(@RequestHeader(required = false) String fileSystem,@RequestParam String path, @RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "length", defaultValue = "0") int length) {
        FileInputStream read = null;
        InputStreamResource resource = null;
        try {
            read = dataService.read(path);
            resource = new InputStreamResource(read);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + path)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 关闭退出进程
     */
    @RequestMapping("shutdown")
    public void shutdownServer() {
        System.exit(-1);
    }
}
