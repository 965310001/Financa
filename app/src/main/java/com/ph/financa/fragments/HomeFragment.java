package com.ph.financa.fragments;


import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.ph.financa.R;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.constant.ApiConstant;

/**
 * 首页
 */

public class HomeFragment extends BaseFragment {

    private String URL = String.format("%s%s", ApiConstant.BASE_URL_ZP, ApiConstant.H5);
    private AgentWeb mAgentWeb;

    @Override
    public void loadData() {
        super.loadData();
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((ViewGroup) mContentView.findViewById(R.id.fl), new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(URL);
    }

    @Override
    public void onPause() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
    }
}
