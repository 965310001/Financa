package com.ph.financa.activity.bean;

import java.io.Serializable;

/*查询谁看了我列表*/
public class CustomerBean implements Serializable {

    /**
     * id : 1164360692518617000
     * customerName : 荣钦
     * count : null
     */

    private long id;
    private String customerName;
    private Integer count;
    private String headImgUrl;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}