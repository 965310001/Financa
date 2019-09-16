package com.ph.financa.activity.bean;

import java.io.Serializable;

public class VipBean implements Serializable {
    /**
     * userId : 123456
     * serviceId : 1167320632862441472
     * status : 1
     * isEnable : true
     * startTime : 2019-09-01 21:13:13
     * endTime : 2021-09-01 21:37:13
     */

    private Number userId;
    private Number serviceId;
    private Number status;
    private boolean isEnable;
    private String startTime;
    private String endTime;

    public Number getUserId() {
        return userId;
    }

    public void setUserId(Number userId) {
        this.userId = userId;
    }

    public Number getServiceId() {
        return serviceId;
    }

    public void setServiceId(Number serviceId) {
        this.serviceId = serviceId;
    }

    public Number getStatus() {
        return status;
    }

    public void setStatus(Number status) {
        this.status = status;
    }

    public boolean isIsEnable() {
        return isEnable;
    }

    public void setIsEnable(boolean isEnable) {
        this.isEnable = isEnable;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
