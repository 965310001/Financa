package com.ph.financa.activity.bean;

import java.io.Serializable;

/*套餐模板模块-根据服务类型查询对应所有订单模板*/
public class SelectBean implements Serializable {

    /**
     * id : 1167320632862441472
     * createTime : 1567163901000
     * name : 1年
     * originalPrice : 888.0000
     * price : 388.0000
     * serviceTime : 12
     * type : 1
     * details : null
     * status : 1
     * createUserId : 123
     * updateUserId : 123
     * updateTime : 2019-08-30 19:18:21
     */

    private long id;
    private long createTime;
    private String name;
    private String originalPrice;
    private String price;
    private int serviceTime;
    private int type;
    private String details;
    private int status;
    private long createUserId;
    private long updateUserId;
    private String updateTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(String originalPrice) {
        this.originalPrice = originalPrice;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(int serviceTime) {
        this.serviceTime = serviceTime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(long createUserId) {
        this.createUserId = createUserId;
    }

    public long getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(long updateUserId) {
        this.updateUserId = updateUserId;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}