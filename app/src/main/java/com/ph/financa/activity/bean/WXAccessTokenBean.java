package com.ph.financa.activity.bean;

import java.io.Serializable;

public class WXAccessTokenBean implements Serializable {

    /**
     * access_token : 25_oS8FLoJWl9OaWN2DGjBBVxTPqLbP46S4U02iZ2jpNfOWfB-VA66yGPPFGMLdHuCPGrZT_fCLpBbuhZq3IN1g3HcHzUY8Rs2sqVV-B5rCrFs
     * expires_in : 7200
     * refresh_token : 25_oS8FLoJWl9OaWN2DGjBBV-xSgWcfxL0KZ4g6Wzfa6R6OWzS2iYoYI3e6XY9mtz0qgQv6uefXlKrfibHvMVPYzceNNblFj1OiTUSFuvVTDqY
     * openid : oyNfPwTPvjJgoCsZWuN6WXpjh6XM
     * scope : snsapi_userinfo
     * unionid : oxTsiwFfx65Y2bICtmMCNCLru6wY
     */

    private String access_token;
    private int expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private String unionid;
    private int errcode;
    private String errmsg;

    /**
     * nickname : 迷了鹿
     * sex : 1
     * language : zh_CN
     * city : Guangzhou
     * province : Guangdong
     * country : CN
     * headimgurl : http://thirdwx.qlogo.cn/mmopen/vi_32/TjcibDU2rmBVZnrJqPRldjZf1CpJY9sLUdgAKm7uP0mZEdhNPgOhbP3iboV6z1e5UlGoPvWInYsPuFkpicguveSyw/132
     * privilege : []
     */

    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }
}
