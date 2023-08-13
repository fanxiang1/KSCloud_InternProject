package com.ksyun.campus.metaserver.services;

import com.ksyun.campus.metaserver.config.ZookeeperConfig;
import com.ksyun.campus.metaserver.domain.FileType;
import com.ksyun.campus.metaserver.domain.ReplicaData;
import com.ksyun.campus.metaserver.domain.StatInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class MetaService implements ApplicationRunner {


    @Autowired
    private ZookeeperConfig zookeeperConfig;

    @Autowired
    private SaveService saveService;

//    @Autowired
//    private FsckServices fsckServices;

    public static List<String> res = new ArrayList<>();

    @Autowired
    private CuratorFramework metaclient;

    private CuratorFramework client;

    public static final String DATA_BASE_PATH = "/data";

    private static CountDownLatch watch = new CountDownLatch(1);

    // datasource数据保存
    public static Map<String, List> dataMap = new LinkedHashMap<String, List>();
    public static Map<String, List> threeDataMap = new LinkedHashMap<String, List>();

    public static List<Map.Entry<String, List>> lists = new ArrayList<>(dataMap.entrySet());


    /**
     * 负载均衡选择3个ds
     *
     * @throws Exception
     */
    public void pickDataServer() throws Exception {
        // todo 通过zk内注册的ds列表，选择出来3个ds，用来后续的write
//        List<Map.Entry<String, List>> lists = new ArrayList<>(dataMap.entrySet());
        updateDS();
        // 清一下旧的，重选三个
        threeDataMap.clear();
        if (lists.size() >= 3) {
            for (Map.Entry<String, List> set : lists.subList(0, 3)) {
                threeDataMap.put(set.getKey(), set.getValue());
            }
        } else {
            for (Map.Entry<String, List> set : lists) {
                threeDataMap.put(set.getKey(), set.getValue());
            }
        }
    }


    /**
     * 创建文件夹
     *
     * @param path
     */
    public void mkdir(String path) {
        // 设置文件的元数据信息
        StatInfo statInfo = new StatInfo();
        ArrayList<ReplicaData> list = new ArrayList<>();
        // 获取文件属性信息
        long size = 0; // 文件大小，创建时为0
        long time = System.currentTimeMillis();// 文件创建时间
        statInfo.setType(FileType.Directory);
        statInfo.setMtime(time);
        statInfo.setPath(path);
        statInfo.setSize(size);
        // 获得三副本信息
        Iterator<Map.Entry<String, List>> iterator = threeDataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List> next = iterator.next();
            String key = next.getKey();
            List value = next.getValue();
            String dsNode = value.get(0) + ":" + value.get(1);
            // 调用ds的create接口，进行文件创建
            String url = "http://" + dsNode + "/mkdir?path=" + path;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            // 需要对请求做异常处理，当请求失败时，需要去掉这个数据节点的存储，表明节点可能出现问题
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                ReplicaData replicaData = new ReplicaData();
                replicaData.setId(key);
                replicaData.setPath(response.getBody());
                replicaData.setDsNode(dsNode);
                list.add(replicaData);
            } catch (Exception e) {
                log.info("该数据节点出错了");
            }
        }
        statInfo.setReplicaData(list);
        // 将信息存入到zk中
        try {
            saveService.metatoZk(statInfo);
        } catch (Exception e) {
            log.info("元数据写入失败");
        }
    }

    /**
     * 创建文件
     *
     * @param path
     */
    public void createFile(String path) {
        // 设置文件的元数据信息
        StatInfo statInfo = new StatInfo();
        ArrayList<ReplicaData> list = new ArrayList<>();
        // 获取文件属性信息
        long size = 0; // 文件大小，创建时为0
        long time = System.currentTimeMillis();// 文件创建时间
        statInfo.setType(FileType.File);
        statInfo.setMtime(time);
        statInfo.setPath(path);
        statInfo.setSize(size);
        // todo：调用ds进行文件创建
        // 获得三副本信息
        Iterator<Map.Entry<String, List>> iterator = threeDataMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List> next = iterator.next();
            String key = next.getKey();
            List value = next.getValue();
            String dsNode = value.get(0) + ":" + value.get(1);
            // 调用ds的create接口，进行文件创建
            String url = "http://" + dsNode + "/create?path=" + path;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            try {
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
                // 更新三副本信息
                ReplicaData replicaData = new ReplicaData();
                replicaData.setId(key);
                System.out.println(response.getBody());
                replicaData.setPath(response.getBody());
                replicaData.setDsNode(dsNode);
                list.add(replicaData);
            } catch (Exception e) {
                log.info("该数据节点出错了");
            }

        }
        statInfo.setReplicaData(list);
        // 将信息存入到zk中
        try {
            saveService.metatoZk(statInfo);
        } catch (Exception e) {
            log.info("存入元数据信息失败");
        }
    }

    public void commitWrite(String path, byte[] bytes, CuratorFramework client) {
        // 在zk中查看是否存在该文件的元数据信息
        StatInfo statInfo = null;
        // 如果zk中包含了该文件的元数据，则取出对应的数据
        boolean b = path.startsWith("/");//true
        if (!b) {
            path = "/" + path;
        }
        try {
            statInfo = saveService.getNode(path, client);
        } catch (Exception e) {
            log.info("节点不存在");
        }
        System.out.println(statInfo);
        List<ReplicaData> replicaData = statInfo.getReplicaData();
        String res = new String(bytes);
        System.out.println(res);
        // 分别调用三个副本进行数据存储
        // 获得三副本信息
        Iterator<ReplicaData> iterator = replicaData.iterator();
        while (iterator.hasNext()) {
            ReplicaData next = iterator.next();
            String dsNode = next.getDsNode();
            // 调用ds的write接口，进行文件写入
            String url = "http://" + dsNode + "/write?path=" + path;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<ByteArrayResource> httpEntity = new HttpEntity<>(new ByteArrayResource(bytes), httpHeaders);
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        }
        // 更新节点文件的数据大小
        statInfo.setSize(bytes.length);
        // 将信息存入到zk中
        try {
            long time = System.currentTimeMillis();// 文件更新时间
            statInfo.setMtime(time);
            saveService.metatoZk(statInfo);
        } catch (Exception e) {
            log.info("元数据存入zk失败");
        }
    }

    /**
     * 删除文件夹或文件
     *
     * @param path
     */
    public String delete(String path, CuratorFramework client) {
        // 在zk中查看是否存在该文件的元数据信息
        StatInfo statInfo = null;
        // 如果zk中包含了该文件的元数据，则取出对应的数据
        boolean b = path.startsWith("/");//true
        if (!b) {
            path = "/" + path;
        }
        // 先去删除ds中的数据，再删除元数据节点
        try {
            statInfo = saveService.getNode(path, client);
        } catch (Exception e) {
            log.info("zk中不存在对应节点");
        }
        List<ReplicaData> replicaData = statInfo.getReplicaData();
        ResponseEntity<String> response = null;
        // 获得三副本信息
        Iterator<ReplicaData> iterator = replicaData.iterator();
        while (iterator.hasNext()) {
            ReplicaData next = iterator.next();
            String dsNode = next.getDsNode();
            // 调用ds的delete接口，进行文件删除
            String url = "http://" + dsNode + "/delete?path=" + path;
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(httpHeaders);
            RestTemplate restTemplate = new RestTemplate();
            response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        }
        // 删除对应元数据中的对应节点信息
        try {
            client.delete().deletingChildrenIfNeeded().forPath("/metadata" + path);
        } catch (Exception e) {
            log.info("删除zk中的元数据失败");
        }

        return response.getBody();
    }


    /**
     * 注册监听
     * TreeCache: 可以将指定的路径节点作为根节点（祖先节点），对其所有的子节点操作进行监听，
     * 呈现树形目录的监听，可以设置监听深度，最大监听深度为 int 类型的最大值。
     */
    public void zkWatch(CuratorFramework client, String path) throws Exception {
        TreeCache treeCache = new TreeCache(client, path);
        // 开始监听
        treeCache.start();
        treeCache.getListenable().addListener(new TreeCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
                ChildData eventData = event.getData();
                switch (event.getType()) {
                    case NODE_ADDED:
//                        fsckServices.fsckTask();
                        log.warn(path + "节点添加" + eventData.getPath() + "\t添加数据为：" + new String(eventData.getData()));
                        break;
                    case NODE_UPDATED:
//                        fsckServices.fsckTask();
                        log.warn(eventData.getPath() + "节点数据更新\t更新数据为：" + new String(eventData.getData()) + "\t版本为：" + eventData.getStat().getVersion());
                        break;
                    case NODE_REMOVED:
                        // 在zk中查看是否存在该文件的元数据信息
                        StatInfo statInfo = null;
                        log.warn(eventData.getPath() + "节点被删除");
                        // 更新每个元数据的信息，去掉这个数据节点
                        getAllNode("/metadata");
                        if (res.size() == 0) {
                            return;
                        }
                        Iterator<String> iterator = res.iterator();
                        // 只要节点包含了该ds信息，则需要去除
                        while (iterator.hasNext()) {
                            String next = iterator.next();
                            String[] split = next.split("/");
                            String path = "";
                            for (int i = 2; i < split.length; i++) {
                                path += "/" + split[i];
                            }
                            statInfo = saveService.getNode(path, metaclient); // 获取每个节点的数据，根据对应的元数据判断其副本数是否正常
                            List<ReplicaData> replicaData = statInfo.getReplicaData();
                            Iterator<ReplicaData> iterator1 = replicaData.iterator();
                            while (iterator1.hasNext()) {
                                ReplicaData next1 = iterator1.next();
                                String id = next1.getId();
                                // 如果包含该节点则删除
                                if (eventData.getPath().equals(id)) {
                                    iterator1.remove();
                                }
                            }
                            saveService.metatoZk(statInfo);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
        Thread.sleep(1000);  //如果不执行 watch.countDown()，进程会一致阻塞在 watch.await()
    }

    /**
     * 更新ds列表
     *
     * @throws Exception
     */
    public void updateDS() throws Exception {
        // 获取数据节点下所有子节点的路径
        List<String> path = client.getChildren().forPath(DATA_BASE_PATH);
        // 循环获取节点放入hashmap中，并按容量大小进行排序
        Iterator<String> iterator = path.iterator();
        while (iterator.hasNext()) {
            String next = iterator.next();
            String s = new String(client.getData().forPath(DATA_BASE_PATH + "/" + next));
            String[] split = s.split(",");
            List<String> list = new ArrayList<>();
            for (String temp : split) {
                list.add(temp);
            }

            // 将所有的节点放入hashmap中
            dataMap.put(DATA_BASE_PATH + "/" + next, list);
        }

        lists = new ArrayList<Map.Entry<String, List>>(dataMap.entrySet());
        // 排序
        Collections.sort(lists, new Comparator<Map.Entry<String, List>>() {
            @Override
            public int compare(Map.Entry<String, List> o1, Map.Entry<String, List> o2) {
                int t1 = Integer.parseInt((String) o1.getValue().get(4));
                int t2 = Integer.parseInt((String) o2.getValue().get(4));
                return t1 - t2;
            }
        });
    }

    public List<String> getAllNode(String parentNode) {
        try {
            List<String> list = metaclient.getChildren().forPath(parentNode);
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


    @Override
    public void run(ApplicationArguments args) throws Exception {
        client = CuratorFrameworkFactory.builder()
                //连接地址  集群用,隔开
                .connectString(zookeeperConfig.getAddr())
                .retryPolicy(new ExponentialBackoffRetry(50, 100))
                .build();
        client.start();
        zkWatch(client, DATA_BASE_PATH);
    }


}
