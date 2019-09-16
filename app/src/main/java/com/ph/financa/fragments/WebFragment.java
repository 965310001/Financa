package com.ph.financa.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.just.agentweb.AgentWeb;
import com.ph.financa.R;

import tech.com.commoncore.base.BaseTitleFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.DisplayUtil;


/**
 * webfragment
 */
public class WebFragment extends BaseTitleFragment {

    private static final String URL = "URL";
    private static final String TITLE = "TITLE";

    private AgentWeb mAgentWeb;

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
        if (getArguments() != null) {
            mUrl = getArguments().getString(URL);
            mTitle = getArguments().getString(TITLE);
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        Log.i(TAG, "loadData: " + mUrl);
        if (!TextUtils.isEmpty(mUrl)) {
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(mContentView.findViewById(R.id.fl_content), new FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(mUrl);

        }
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
        if (!TextUtils.isEmpty(mTitle)) {
            mTitleBar.setTitleMainText(mTitle);
        } else {
            mTitleBar.setVisibility(View.GONE);
            mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
            Log.i(TAG, "initView: " + mUrl);
            if (mUrl.endsWith(ApiConstant.CUSTOMER)) {
                StatusBarCompat.setStatusBarColor(mContext, getContext().getColor(R.color.white));
            }

        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_web;
    }
}
