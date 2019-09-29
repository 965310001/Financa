package com.ph.financa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.just.agentweb.AgentWeb;
import com.ph.financa.R;

import tech.com.commoncore.base.BaseTitleFragment;

/**
 * web 封装
 */
public class WebFragment extends BaseTitleFragment {

    private static final String URL = "URL";
    private static final String TITLE = "TITLE";

    protected AgentWeb mAgentWeb;

    private String mUrl, mTitle;

    public static WebFragment newInstance(String url) {
        return newInstance(url, "");
    }

    public static WebFragment newInstance(String url, String title) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUrl = getArguments().getString(URL);
            mTitle = getArguments().getString(TITLE);
        }
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    protected String getUrl() {
        return mUrl;
    }

    protected String getJavaObjectKey() {
        return "cosmetics";
    }

    protected Object getJavaObjectValue(AgentWeb agentWeb, Context context) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(getUrl())) {
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(mContentView.findViewById(R.id.fl_content), new FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(getUrl());
            Log.i(TAG, "initView: "+getUrl());
            if (null != getJavaObjectValue(mAgentWeb, getContext())) {
                mAgentWeb.getJsInterfaceHolder().addJavaObject(getJavaObjectKey(), getJavaObjectValue(mAgentWeb, getContext()));
            }
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (!TextUtils.isEmpty(mTitle)) {
            titleBar.setTitleMainText(mTitle);
        } else {
            titleBar.setVisibility(View.GONE);
            /*mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);*/
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_web;
    }
}
