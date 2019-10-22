package com.ph.financa.activity.bean;

import java.io.Serializable;

public class DocBean implements Serializable {

    /**
     * name : 文件名
     * type : PDF
     * size : 1024
     * pathUrl : 文件地址
     * createTime : 2019-09-21 18:02:30
     */

    private String name;
    private String type;
    private int size;
    private String pathUrl;
    private String createTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getPathUrl() {
        return pathUrl;
    }

    public void setPathUrl(String pathUrl) {
        this.pathUrl = pathUrl;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
