package tech.com.commoncore.utils;

public class AppStatusManager {

    public static final int STATUS_RECYVLE =-1; //被回收
    public static final int STATUS_NORMAL=1;    //正常

    public static int appStatus = AppStatusManager.STATUS_RECYVLE;    //APP状态 初始值为不在前台状态

    public static AppStatusManager appStatusManager;

    private AppStatusManager() {
    }

    //单例模式
    public static AppStatusManager getInstance() {
        if (appStatusManager == null) {
            appStatusManager = new AppStatusManager();
        }
        return appStatusManager;
    }

    public int getAppStatus() {
        return appStatus;
    }

    public void setAppStatus(int appStatus) {
        this.appStatus = appStatus;
    }
}