package com.ksyun.campus.metaserver.controller;

import com.ksyun.campus.metaserver.domain.FileType;
import com.ksyun.campus.metaserver.domain.ReplicaData;
import com.ksyun.campus.metaserver.domain.StatInfo;
import com.ksyun.campus.metaserver.services.MetaService;
import com.ksyun.campus.metaserver.services.SaveService;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static com.ksyun.campus.metaserver.services.MetaService.threeDataMap;
import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;

@RestController("/")
@Slf4j
public class MetaController {
    @Autowired
    private MetaService metaService;

    @Autowired
    private SaveService saveService;

    @Autowired
    private CuratorFramework client;


    @RequestMapping("pickDataServer")
    public Object pickDataServer() {
        return threeDataMap;
        //return new ResponseEntity(HttpStatus.OK);
    }


    @RequestMapping("stats")
    public ResponseEntity stats(@RequestHeader(required = false) String fileSystem, @RequestParam String path) throws IOException {

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("create")
    public ResponseEntity createFile(@RequestHeader(required = false) String fileSystem,@RequestParam String path) throws Exception {
        // 调用service进行文件存储,更新ds
        metaService.pickDataServer();
        // 需要先创建各个文件夹及其元数据信息
        String[] split = path.split("/");
        String dirpath="";
        for(int i=1;i<split.length-1;i++){
            dirpath+="/"+split[i];
            mkdir(null,dirpath);
        }
        // 最后一层创建文件
        metaService.createFile(path);

        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("mkdir")
    public ResponseEntity mkdir(@RequestHeader(required = false) String fileSystem,@RequestParam String path)  {
        // 调用service进行文件存储,更新ds
        try {
            metaService.pickDataServer();
        } catch (Exception e) {
            log.info("更新ds信息失败");
        }
        // 需要先创建各个文件夹及其元数据信息
        String[] split = path.split("/");
        String dirpath="";
        for(int i=1;i<split.length;i++){
            dirpath+="/"+split[i];
            metaService.mkdir(dirpath);
        }
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("listdir")
    public ResponseEntity listdir(@RequestHeader(required = false) String fileSystem, @RequestParam String path) {
        return new ResponseEntity(HttpStatus.OK);
    }

    @RequestMapping("delete")
    public ResponseEntity delete(@RequestHeader(required = false) String fileSystem,@RequestParam String path) {
        String response = metaService.delete(path,client);
        return new ResponseEntity(response,HttpStatus.OK);
    }

    /**
     * 保存文件写入成功后的元数据信息，包括文件path、size、三副本信息等
     *
     * @param path
     * @param offset
     * @param length
     * @return
     */
    @RequestMapping(value = "write", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity commitWrite(@RequestHeader(required = false) String fileSystem,@RequestBody byte[] bytes, @RequestParam("path") String path, @RequestParam(value = "offset", defaultValue = "0") int offset, @RequestParam(value = "length", defaultValue = "0") int length) throws Exception {

        metaService.commitWrite(path,bytes,client);

        return new ResponseEntity(HttpStatus.OK);

    }

    /**
     * 根据文件path读取文件信息
     *
     * @param path
     * @return
     */
    @RequestMapping("read")
    public Resource read(@RequestHeader(required = false) String fileSystem,@RequestParam String path) throws Exception {
        // 在zk中查看是否存在该文件的元数据信息
        StatInfo statInfo = null;
        // 如果zk中包含了该文件的元数据，则取出对应的数据
        boolean b = path.startsWith("/");//true
        if(!b){
            path="/"+path;
        }
        // 得到元数据
        statInfo = saveService.getNode(path, client);
        // 随机访问一个ds
        List<ReplicaData> replicaData = statInfo.getReplicaData();
        Random rand = new Random();
        ReplicaData metaData = replicaData.get(rand.nextInt(replicaData.size()));
        String dsNode = metaData.getDsNode();
        // 调用ds的write接口，进行数据读取
        String url = "http://" + dsNode + "/read?path=" + path;
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Resource> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Resource.class);
        return response.getBody();
    }


    /**
     * 根据文件path查询三副本的位置，返回客户端具体ds、文件分块信息
     * @param path
     * @return
     */
    @RequestMapping("getFileStats")
    public StatInfo getFileStats(@RequestHeader(required = false) String fileSystem,@RequestParam String path) throws Exception {
        // 在zk中查看是否存在该文件的元数据信息
        StatInfo statInfo = null;
        // 如果zk中包含了该文件的元数据，则取出对应的数据
        boolean b = path.startsWith("/");//true
        if(!b){
            path="/"+path;
        }
        statInfo = saveService.getNode(path, client);
        return statInfo;
    }
    /**
     * 根据文件夹path查询其子节点的三副本的位置，返回客户端具体ds、文件分块信息
     * @param path
     * @return
     */
    @RequestMapping("listFileStats")
    public List<StatInfo> listFileStats(@RequestHeader(required = false) String fileSystem,@RequestParam String path) throws Exception {
        List<StatInfo> statInfos = new ArrayList<>();
        // 在zk中查看是否存在该文件的元数据信息
        StatInfo statInfo = null;
        // 如果zk中包含了该文件的元数据，则取出对应的数据
        boolean b = path.startsWith("/");//true
        if(path.equals("/")){
            path="";
        }
        if(!b){
            path="/"+path;
        }
        List<String> list = client.getChildren().forPath("/metadata"+path);
        Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()){
            String next = iterator.next();
            try{
                statInfo = saveService.getNode(path+"/"+next, client);
                statInfos.add(statInfo);
            }catch (Exception e){
                //System.out.println("该节点不存在");
            }
        }
        return statInfos;
    }


    /**
     * 关闭退出进程
     */
    @RequestMapping("shutdown")
    public void shutdownServer() {
        System.exit(-1);
    }

}
