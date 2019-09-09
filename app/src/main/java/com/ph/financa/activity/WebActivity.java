package com.ph.financa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.just.agentweb.AgentWeb;
import com.ph.financa.R;

import tech.com.commoncore.base.BaseTitleActivity;

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
        }

        if (intent.hasExtra("url")) {
            mUrl = intent.getStringExtra("url");
        }
    }

    @Override
    public void loadData() {
        super.loadData();
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((ViewGroup) findViewById(R.id.fl), new FrameLayout.LayoutParams(-1, -1))
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
    public int getContentLayout() {
        return R.layout.activity_web;
    }
}