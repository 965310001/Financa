package com.ph.financa.activity.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataBaseBean implements Serializable {
    @SerializedName("pathUrl")
    String path;
    @SerializedName("name")
    String title;
    @SerializedName("createTime")
    String time;

    public DataBaseBean() {
    }

    public DataBaseBean(String path, String title, String time) {
        this.path = path;
        this.title = title;
        this.time = time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}