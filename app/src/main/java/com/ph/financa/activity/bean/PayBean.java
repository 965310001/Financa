package com.ph.financa.activity.bean;

import java.io.Serializable;

/*微信、支付宝返回的格式*/
public class PayBean implements Serializable {

    /**
     * payUrl : {
     * "timeStamp": "1568198824",
     * "packageValue": "Sign=WXPay",
     * "appId": "wx7faa69a8d98bfb10",
     * "sign": "017B811C86A8BA27C86999A1786A45BA",
     * "prepayId": "wx111847044569815c56a72fb71730507100",
     * "partnerId": "7B396D665DB53599671D872FAB53CD86",
     * "nonceStr": "SUXjsdwRlsdF9JQV"
     * }
     * payTypeEnum : WECHAT
     */

    private String payUrl;
    private String payTypeEnum;

    public String getPayUrl() {
        return payUrl;
    }

    public void setPayUrl(String payUrl) {
        this.payUrl = payUrl;
    }

    public String getPayTypeEnum() {
        return payTypeEnum;
    }

    public void setPayTypeEnum(String payTypeEnum) {
        this.payTypeEnum = payTypeEnum;
    }
}
