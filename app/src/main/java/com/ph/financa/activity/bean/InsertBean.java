package com.ph.financa.activity.bean;

import java.io.Serializable;

/*创建订单*/
public class InsertBean implements Serializable {

    /**
     * id : 1169084824795742208
     * createTime : 1567566517407
     * userId : 123456
     * orderNo : 20190904110837432651
     * serviceId : 1167320632862441472
     * amount : 388
     * payAmount : 0.00
     * payTime : null
     * payType : 1
     * status : 1
     * serviceStartTime : null
     * serviceEndTime : null
     * createUserId : 123456
     * updateUserId : 123456
     * updateTime : 2019-09-04 11:08:37
     */

    private long id;
    private long createTime;
    private long userId;
    private String orderNo;
    private long serviceId;
    private String amount;
    private String payAmount;
//    private Object payTime;
    private int payType;
    private int status;
//    private String serviceStartTime;
//    private Object serviceEndTime;
//    private int createUserId;
//    private int updateUserId;
//    private String updateTime;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public long getServiceId() {
        return serviceId;
    }

    public void setServiceId(long serviceId) {
        this.serviceId = serviceId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

//    public Object getPayTime() {
//        return payTime;
//    }
//
//    public void setPayTime(Object payTime) {
//        this.payTime = payTime;
//    }

    public int getPayType() {
        return payType;
    }

    public void setPayType(int payType) {
        this.payType = payType;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

//    public Object getServiceStartTime() {
//        return serviceStartTime;
//    }
//
//    public void setServiceStartTime(Object serviceStartTime) {
//        this.serviceStartTime = serviceStartTime;
//    }
//
//    public Object getServiceEndTime() {
//        return serviceEndTime;
//    }
//
//    public void setServiceEndTime(Object serviceEndTime) {
//        this.serviceEndTime = serviceEndTime;
//    }
//
//    public int getCreateUserId() {
//        return createUserId;
//    }
//
//    public void setCreateUserId(int createUserId) {
//        this.createUserId = createUserId;
//    }
//
//    public int getUpdateUserId() {
//        return updateUserId;
//    }
//
//    public void setUpdateUserId(int updateUserId) {
//        this.updateUserId = updateUserId;
//    }
//
//    public String getUpdateTime() {
//        return updateTime;
//    }
//
//    public void setUpdateTime(String updateTime) {
//        this.updateTime = updateTime;
//    }
}