package com.ksyun.campus.metaserver.domain;

import lombok.Data;

import java.util.List;

@Data
public class StatInfo
{
    public String path; // 文件名
    public long size; // 大小
    public long mtime; // 创建时间，修改时间
    public FileType type; // 类型

//    private List<ReplicaData> replicaData; // 内容数据的三副本索引
    public List<ReplicaData> replicaData;
    public StatInfo() {}

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public long getMtime() {
        return mtime;
    }

    public void setMtime(long mtime) {
        this.mtime = mtime;
    }

    public FileType getType() {
        return type;
    }

    public void setType(FileType type) {
        this.type = type;
    }

}
