package com.ph.financa.activity.bean;

import java.io.Serializable;

/**
 * 获取用户未读统计
 */
public class MessageCountBean implements Serializable {
    /**
     * unreadCount : 3
     * noticeUnreadCount : 2
     * systemUnreadCount : 1
     */

    private int unreadCount;
    private int noticeUnreadCount;
    private int systemUnreadCount;

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public int getNoticeUnreadCount() {
        return noticeUnreadCount;
    }

    public void setNoticeUnreadCount(int noticeUnreadCount) {
        this.noticeUnreadCount = noticeUnreadCount;
    }

    public int getSystemUnreadCount() {
        return systemUnreadCount;
    }

    public void setSystemUnreadCount(int systemUnreadCount) {
        this.systemUnreadCount = systemUnreadCount;
    }
}