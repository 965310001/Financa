package com.ph.financa.activity.bean;

import java.io.Serializable;

/**
 * Time:2018/11/8
 * Desc: 数据加载的基类. 包含数据请求的最外层  字段: code、msg、data
 */
public class BaseTResp2<T> implements Serializable {
    private int code;
    private String msg;
    private String detailMsg;
    public T data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public void setDetailMsg(String detailMsg) {
        this.detailMsg = detailMsg;
    }

    public String getDetailMsg() {
        return detailMsg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return code == 200;
    }

}
