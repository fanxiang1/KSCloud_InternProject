package com.ksyun.campus.client;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;

import static com.ksyun.campus.client.util.ZkUtil.metaPath;

public class FSInputStream extends InputStream {

    private String path;

    private volatile InputStream inputStream;


    public FSInputStream(InputStream inputStream,String path) {
        this.inputStream=inputStream;
        this.path = path;
    }

    @Override
    public int read() throws IOException {
        return inputStream.read();
    }

    @Override
    public int read(byte[] bytes) throws IOException {
            return inputStream.read(bytes);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return inputStream.read(b,off,len);
    }

    public int available() throws IOException {
        int available = inputStream.available();
        return available;
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
//        super.close();
    }
}
