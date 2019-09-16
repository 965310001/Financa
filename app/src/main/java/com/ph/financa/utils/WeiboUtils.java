//package com.ph.financa.utils;
//
//import android.app.Activity;
//
//import com.ph.financa.wxapi.pay.Context;
//import com.sina.weibo.sdk.WbSdk;
//import com.sina.weibo.sdk.auth.AuthInfo;
//import com.sina.weibo.sdk.share.WbShareHandler;
//
//public class WeiboUtils {
//
//    /*初始化*/
//    public static void init(Context context, String appKey, String redirectUrl, String scope) {
//        WbSdk.install(context, new AuthInfo(context, appKey, redirectUrl, scope));
//    }
//
//    public WbShareHandler getShareHandler(Activity activity) {
//        WbShareHandler shareHandler = new WbShareHandler(activity);
//        shareHandler.registerApp();
//        return shareHandler;
//    }
//
//    /*分享*/
//    public static void share() {
//
//    }
//
//}
