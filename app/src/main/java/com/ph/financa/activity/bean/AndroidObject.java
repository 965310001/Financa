package com.ph.financa.activity.bean;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.ph.financa.activity.LoginActivity;

import tech.com.commoncore.utils.FastUtil;

/**
 * 主要是为了封装js交互
 */
public class AndroidObject extends Object {

    private Context mContext;

    public AndroidObject(Context mContext) {
        this.mContext = mContext;
    }

    /*重新登录*/
    @JavascriptInterface
    public void reLogin(String content) {
        Log.i("TAG", "reLogin: " + content);
        FastUtil.startActivity(mContext, LoginActivity.class);
    }
}
