package com.ph.financa.activity.bean;

import java.io.Serializable;

/*获取通讯录*/
public class ContactColumnBean implements Serializable {

    private String id;
    private String name;
    private String number;

    public ContactColumnBean() {
    }

    public ContactColumnBean(String id, String name, String number) {
        this.id = id;
        this.name = name;
        this.number = number;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}