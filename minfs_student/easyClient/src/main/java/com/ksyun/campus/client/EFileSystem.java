package com.ksyun.campus.client;

import com.ksyun.campus.client.domain.ClusterInfo;
import com.ksyun.campus.client.domain.StatInfo;
import com.ksyun.campus.client.util.FileUtils;
import com.ksyun.campus.client.util.HttpClientConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static com.ksyun.campus.client.util.ZkUtil.metaPath;

public class EFileSystem extends FileSystem {

    private String fileName = "default";

    public EFileSystem() {
    }

    public EFileSystem(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 打开文件
     *
     * @param path
     * @return
     */
    public FSInputStream open(String path) throws IOException {
        FSInputStream fsInputStream = null;
        String url ="http://"+metaPath+"/read?path="+path;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Resource> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Resource.class);
        InputStream inputStream = response.getBody().getInputStream();
        fsInputStream=new FSInputStream(inputStream,path);

        return fsInputStream;
    }

    /**
     * 创建文件
     *
     * @param path
     * @return
     */
    public FSOutputStream create(String path) {
        FSOutputStream fsOutputStream = new FSOutputStream(path);
        try {
//            fsOutputStream.write(content);
            String url = "http://" + metaPath + "/create?path=" + path;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        } catch (Exception e) {
            System.out.println("文件创建失败");
        }
        // 调用metaServer
        return fsOutputStream;
    }

    /**
     * 新建文件夹
     *
     * @param path
     * @return
     */
    public boolean mkdir(String path) {
        try{
//            fsOutputStream.write(content);
            String url ="http://"+metaPath+"/mkdir?path="+path;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            return true;
        }catch (Exception e){
            System.out.println("文件创建失败");
        }

        return false;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public boolean delete(String path) {
        try{
            String url ="http://"+metaPath+"/delete?path="+path;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
            System.out.println(response.getBody());
            return true;
        }catch (Exception e){
            System.out.println("文件删除失败");
        }

        return false;
    }

    /**
     * 获取文件状态
     *
     * @param path
     * @return
     */
    public StatInfo getFileStats(String path) {
        try{
//            fsOutputStream.write(content);
            String url ="http://"+metaPath+"/getFileStats?path="+path;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<StatInfo> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, StatInfo.class);
            return response.getBody();
        }catch (Exception e){
            System.out.println("文件创建失败");
        }

        return null;
    }

    /**
     * 获取文件夹下的所有文件的信息
     * @param path
     * @return
     */
    public List<StatInfo> listFileStats(String path) {
        try{
//            fsOutputStream.write(content);
            String url ="http://"+metaPath+"/listFileStats?path="+path;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<List> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, List.class);
            List<StatInfo> body = (List<StatInfo>) response.getBody();
            return body;
        }catch (Exception e){
            System.out.println("文件属性获取失败，请检查文件是否存在");
        }
        return null;
    }

    public ClusterInfo getClusterInfo() throws Exception {
        ClusterInfo clusterInfo = new ClusterInfo();
        ClusterInfo.MetaServerMsg metaServerMsg = null;
        ClusterInfo.DataServerMsg dataServerMsg = null;
        ArrayList arrayList = new ArrayList();
        CuratorFramework client = CuratorFrameworkFactory.builder()
                //连接地址  集群用,隔开
                .connectString("10.0.0.201:2181")
                .connectionTimeoutMs(50000)
                //会话超时时间
                .sessionTimeoutMs(50000)
                //设置重试机制
                .retryPolicy(new ExponentialBackoffRetry(100, 3))
                //设置命名空间 在操作节点的时候，会以这个为父节点
                .build();
        client.start();
        // 获取services下的所有节点
        List<String> metaList = client.getChildren().forPath("/meta/services");
        Iterator<String> iterator = metaList.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            byte[] bytes = client.getData().forPath("/meta/services/" + next);
            String s = new String(bytes);
            String[] split = s.split(",");
            metaServerMsg=clusterInfo.new MetaServerMsg();;
            metaServerMsg.setHost(split[0]);
            metaServerMsg.setPort(Integer.parseInt(split[1]));
            if(next.equals("master")){
                clusterInfo.setMasterMetaServer(metaServerMsg);
            }else {
                clusterInfo.setSlaveMetaServer(metaServerMsg);
            }
        }
        // 获取data下的所有节点
        List<String> dataList = client.getChildren().forPath("/data");
        Iterator<String> iterator1 = dataList.iterator();
        while (iterator1.hasNext()){
            String next = iterator1.next();
            byte[] bytes = client.getData().forPath("/data/" + next);
            String s = new String(bytes);
            String[] split = s.split(",");
            dataServerMsg=clusterInfo.new DataServerMsg();
            dataServerMsg.setHost(split[0]);// ip
            dataServerMsg.setPort(Integer.parseInt(split[1]));// port
            dataServerMsg.setFileTotal(Integer.parseInt(split[2])); // fileTotal
            dataServerMsg.setCapacity(Integer.parseInt(split[3]));
            dataServerMsg.setUseCapacity(Integer.parseInt(split[4]));
            arrayList.add(dataServerMsg);
        }
        clusterInfo.setDataServer(arrayList);


        return clusterInfo;
    }
}
