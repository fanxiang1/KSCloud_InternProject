# minfs_student



## Getting started
直接启动workpublish的脚本即可，会在workpublish的对应目录生成需要的文件
测试项目引入easyClient-1.0.jar即可开始测试


例子：
```public class Main {
    public static void main(String[] args) throws Exception {
        ZkUtil zkUtil = new ZkUtil();
        zkUtil.postCons(); // 得到元数据节点的信息
        EFileSystem eFileSystem = new EFileSystem();
//        //创建文件， 按字节写入文件
        FSOutputStream fsOutputStream1 = eFileSystem.create("/wdag/www/test1.txt");
        String str = "文件上传很简单就实现了";
        byte[] b1 = str.getBytes();
        fsOutputStream1.write(b1);
//
//        //创建文件， 按int写入
//        FSOutputStream fsOutputStream2 = eFileSystem.create("/test2.txt");
//        int b2=100;
//        fsOutputStream2.write(b2);
//
//        //创建文件， 按off+length写入
//        FSOutputStream fsOutputStream3 = eFileSystem.create("/test3.txt");
//        String str3="fanxiang12345";
//        byte[] b3=str3.getBytes();
//        fsOutputStream3.write(b3,1,5);

        // 创建文件夹
//        eFileSystem.mkdir("/tttttttt/");
//         // 查看文件属性信息
        StatInfo fileStats = eFileSystem.getFileStats("/test2.txt");
        System.out.println(fileStats.toString());
         //查看文件的子文件属性
        List<StatInfo> statInfos = eFileSystem.listFileStats("/");
        System.out.println(statInfos.toString());

        // 读取文件信息
//        FSInputStream fsInputStream = eFileSystem.open("/test1.txt");
//        byte[] bytes = new byte[fsInputStream.available()];
//        fsInputStream.read(bytes);
//        System.out.println(new String(bytes));
//
        // 删除文件,删除对应的元数据
//        eFileSystem.delete("/test1.txt");

         //获取metadata集群信息
//        ClusterInfo clusterInfo = eFileSystem.getClusterInfo();
//        System.out.println(clusterInfo.toString());
    }
}
```
