package com.ksyun.campus.metaserver.domain;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * DataNode元数据信息
 *
 * @author Ressmix
 */
public class DataNodeInfo {

    // 存储的文件名
    private String filename;

    // 存储的文件大小
    private Long storedDataSize = 0L;

    // 创建时间
    private Date createTime;

    // 修改时间
    private Date updateTime;

    // 内容数据的三副本索引
    private List<ReplicaData> listData;


}
