package com.ph.financa.activity.bean;

import com.nanchen.wavesidebar.FirstLetterUtil;

import java.util.Collections;
import java.util.List;

/**
 * 联系人model实体类
 */

public class ContactModel {
    private String id;
    private String index;
    private String name;
    private String phone;
    private boolean check;

    private int color;

    public ContactModel(String id, String name, String phone) {
        this.id = id;
        this.index = FirstLetterUtil.getFirstLetter(name);
        this.name = name;
        this.phone = phone;
    }

    public String getIndex() {
        return index;
    }

    public String getName() {
        return name;
    }

    public String getPhone() {
        return phone;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    /**
     * 这里只是为了做演示，实际上数据应该从服务器获取
     */
    public static List<ContactModel> getContacts(List<ContactModel> contacts) {
        Collections.sort(contacts, new LetterComparator());
        return contacts;
    }
}
