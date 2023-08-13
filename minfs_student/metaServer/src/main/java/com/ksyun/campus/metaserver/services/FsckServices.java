package com.ksyun.campus.metaserver.services;

import com.ksyun.campus.metaserver.domain.FileType;
import com.ksyun.campus.metaserver.domain.ReplicaData;
import com.ksyun.campus.metaserver.domain.StatInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.ExistsBuilder;
import org.apache.zookeeper.data.Stat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

@Service
@Slf4j
public class FsckServices {

    @Autowired
    private CuratorFramework client;

    @Autowired
    private SaveService saveService;

    @Autowired
    private MetaService metaService;

    public static List<String> res = new ArrayList<>();

    private String path = "/metadata";

    //@Scheduled(cron = "0 0 0 * * ?") // 每天 0 点执行
    @Scheduled(fixedRate = 60*1000) // 每隔 1 分钟执行一次
    public void fsckTask() {
        try {
            res = new ArrayList<>();
            // 在zk中查看是否存在该文件的元数据信息
            StatInfo statInfo = null;
            // todo 全量扫描文件列表,获得metadata下的所有节点的数据
            if(client.checkExists().forPath("/metadata")==null){
                return;
            }
            getAllNode("/metadata");
            if(res.size()==0){
                return;
            }
            metaService.pickDataServer();
            if(MetaService.dataMap.size()<3){
                log.info("集群节点不足三个");
                return;
            }
            // todo 检查文件副本数量是否正常
            Iterator<String> iterator = res.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                String[] split = next.split("/");
                String path="";
                for(int i=2;i<split.length;i++){
                    path+="/"+split[i];
                }
                statInfo = saveService.getNode(path, client); // 获取每个节点的数据，根据对应的元数据判断其副本数是否正常
                List<ReplicaData> replicaData = statInfo.getReplicaData();
                if (replicaData.size() <3) { // 不正常，需要进行副本的重新添加,重选三个，如果有重复则不添加，从不重复里面添加
                    FileType type = statInfo.getType();
                    if (type == FileType.File) { // 是文件的话调用文件的创建接口,并写入内容
                        recoveryFile(statInfo);
                    } else if (type == FileType.Directory) {
                        recoveryDir(statInfo); // 重写三副本
                    }
                    log.info("文件或文件夹：" + statInfo.getPath() + "的副本数据恢复成功");
                }
            }
        } catch (Exception e) {
            log.info("文件副本恢复失败");
        }
    }

    /**
     * 恢复文件夹
     */
    public void recoveryDir(StatInfo statInfo) throws Exception {
        String path1 = statInfo.getPath();
        List<ReplicaData> replicaData = statInfo.getReplicaData();
        int count=3-replicaData.size(); // 需要操作存的个数
        Iterator<Map.Entry<String, List>> iterator = MetaService.threeDataMap.entrySet().iterator();
        boolean flag=false;
        while (count>0||iterator.hasNext()){
            flag=false;
            Map.Entry<String, List> next = iterator.next();
            String key=next.getKey();
            List value=next.getValue();
            String dsNode=value.get(0)+":"+value.get(1);
            Iterator<ReplicaData> iterator1 = replicaData.iterator();
            // 如果当前节点存在，则进入下一轮
            while (iterator1.hasNext()){
                ReplicaData next1 = iterator1.next();
                String dsNode1 = next1.getDsNode();
                if(dsNode.equals(dsNode1)){
                    flag=true;
                    break;
                }
            }
            // 不存在，开始操作，创建文件夹
            if(!flag){
                // 调用ds的create接口，进行文件创建
                String url = "http://" + dsNode + "/mkdir?path=" + path1;
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
                RestTemplate restTemplate = new RestTemplate();
                // 需要对请求做异常处理，当请求失败时，需要去掉这个数据节点的存储，表明节点可能出现问题
                try{
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                    ReplicaData replicaData1 = new ReplicaData();
                    replicaData1.setId(key);
                    replicaData1.setPath(response.getBody());
                    replicaData1.setDsNode(dsNode);
                    replicaData.add(replicaData1);
                    count--;
                }catch (Exception e){
                    log.info("该数据节点出错了");
                }
            }
        }
        // 更新元数据信息
        // 将信息存入到zk中
        try {
            saveService.metatoZk(statInfo);
        } catch (Exception e) {
            log.info("元数据写入失败");
        }

    }

    public void recoveryFile(StatInfo statInfo) throws IOException {
        String path1 = statInfo.getPath();
        List<ReplicaData> replicaData = statInfo.getReplicaData();
        int count=3-replicaData.size(); // 需要操作存的个数
        Iterator<Map.Entry<String, List>> iterator = MetaService.threeDataMap.entrySet().iterator();
        boolean flag=false;
        while (count>0||iterator.hasNext()){
            flag=false;
            Map.Entry<String, List> next = iterator.next();
            String key=next.getKey();
            List value=next.getValue();
            String dsNode=value.get(0)+":"+value.get(1);
            Iterator<ReplicaData> iterator1 = replicaData.iterator();
            // 如果当前节点存在，则进入下一轮
            while (iterator1.hasNext()){
                ReplicaData next1 = iterator1.next();
                String dsNode1 = next1.getDsNode();
                if(dsNode.equals(dsNode1)){
                    flag=true;
                    break;
                }
            }
            // 不存在，开始操作，创建文件
            if(!flag){
                // 调用ds的create接口，进行文件创建
                String url = "http://" + dsNode + "/create?path=" + path1;
                HttpHeaders httpHeaders = new HttpHeaders();
                httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
                RestTemplate restTemplate = new RestTemplate();
                // 需要对请求做异常处理，当请求失败时，需要去掉这个数据节点的存储，表明节点可能出现问题
                try{
                    // 新建文件
                    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                    // 读取数据，随机访问一个节点
                    Random random = new Random();
                    ReplicaData metaData = replicaData.get(random.nextInt(replicaData.size()));
                    String read_dsNode = metaData.getDsNode();
                    String read_url = "http://" + read_dsNode + "/read?path=" + path1;
                    HttpHeaders read_httpHeaders = new HttpHeaders();
                    read_httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    HttpEntity<MultiValueMap<String, Object>> read_httpEntity = new HttpEntity<>(httpHeaders);
                    RestTemplate read_restTemplate = new RestTemplate();
                    ResponseEntity<Resource> read_response = read_restTemplate.exchange(read_url, HttpMethod.POST, read_httpEntity, Resource.class);
                    Resource body = read_response.getBody();
                    InputStream inputStream = body.getInputStream();
                    byte[] bytes = new byte[inputStream.available()];
                    inputStream.read(bytes);
                    // 写入数据
                    String write_url = "http://" + dsNode + "/write?path=" + path1;
                    HttpHeaders write_httpHeaders = new HttpHeaders();
                    write_httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    RestTemplate write_restTemplate = new RestTemplate();
                    HttpEntity<ByteArrayResource> write_httpEntity = new HttpEntity<>(new ByteArrayResource(bytes), write_httpHeaders);
                    ResponseEntity<String> write_response = restTemplate.exchange(write_url, HttpMethod.POST, write_httpEntity, String.class);
                    // 写入元数据
                    ReplicaData replicaData1 = new ReplicaData();
                    replicaData1.setId(key);
                    replicaData1.setPath(response.getBody());
                    replicaData1.setDsNode(dsNode);
                    replicaData.add(replicaData1);
                    count--;
                }catch (Exception e){
                    log.info("该数据节点出错了");
                }


            }
        }
        // 更新元数据信息
        // 将信息存入到zk中
        try {
            saveService.metatoZk(statInfo);
        } catch (Exception e) {
            log.info("元数据写入失败");
        }
    }


    public List<String> getAllNode(String parentNode) {
        try {
            List<String> list = client.getChildren().forPath(parentNode);
            for (String tmp : list) {
                String childNode = parentNode.equals("/") ? parentNode + tmp : parentNode + "/" + tmp;
                res.add(childNode);
                getAllNode(childNode);
            }
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
