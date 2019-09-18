package com.ph.financa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.just.agentweb.AgentWeb;
import com.ph.financa.R;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.utils.DisplayUtil;

public class WebActivity extends BaseTitleActivity {

    private String mUrl;
    private AgentWeb mAgentWeb;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
            mTitleBar.setTitleMainText(intent.getStringExtra("title"));
        } else {
            mTitleBar.setVisibility(View.GONE);
            StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));
            mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
        }

        if (intent.hasExtra("url")) {
            mUrl = intent.getStringExtra("url");
        }

        Log.i(TAG, "initView: " + mUrl);

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(findViewById(R.id.fl), new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(mUrl);
    }

    @Override
    protected void onPause() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_web;
    }
}