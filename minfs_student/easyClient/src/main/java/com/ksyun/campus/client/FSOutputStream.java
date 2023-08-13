package com.ksyun.campus.client;

import com.ksyun.campus.client.util.HttpClientConfig;
import com.ksyun.campus.client.util.HttpClientUtil;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.entity.ByteArrayEntity;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import static com.ksyun.campus.client.util.ZkUtil.metaPath;

public class FSOutputStream extends OutputStream {

    private String path;

    public FSOutputStream() {
    }

    public FSOutputStream(String path) {
        this.path = path;
    }



    @Override
    public void write(int b) throws IOException {
        // 将int转成字节
        byte[] array=new byte[1];
        array[0]= (byte) b;
        String url ="http://"+metaPath+"/write?path="+path;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ByteArrayResource> httpEntity = new HttpEntity<>(new ByteArrayResource(array), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
    }

    @Override
    public void write(byte[] b) throws IOException {

        String url ="http://"+metaPath+"/write?path="+path;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ByteArrayResource> httpEntity = new HttpEntity<>(new ByteArrayResource(b), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        //super.write(b);
    }

    @Override
    public void write(byte[] b, int off, int len) throws IOException {
        if (b == null) {
            throw new NullPointerException();
        } else if ((off < 0) || (off > b.length) || (len < 0) ||
                ((off + len) > b.length) || ((off + len) < 0)) {
            throw new IndexOutOfBoundsException();
        } else if (len == 0) {
            return;
        }
        // 复制off到len到字节到数组中
        byte[] bytes = new byte[len - off];
        for(int i=0;i<bytes.length;i++){
            bytes[i]=b[off+i];
        }
        String url ="http://"+metaPath+"/write?path="+path;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<ByteArrayResource> httpEntity = new HttpEntity<>(new ByteArrayResource(bytes), httpHeaders);
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
//        super.write(b, off, len);
    }

    @Override
    public void close() throws IOException {
        super.close();
    }
}
