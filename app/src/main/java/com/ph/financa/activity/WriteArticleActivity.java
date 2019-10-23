package com.ph.financa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.just.agentweb.AgentWeb;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.Utils;

/**
 * 写文章
 */
public class WriteArticleActivity extends BaseTitleActivity {

    private String URL = String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP, ApiConstant.ADDARTICLE,
            SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""),
            SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));

    private AgentWeb mAgentWeb;

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));
        Intent intent = getIntent();
        if (null != intent) {
            if (intent.hasExtra("id")) {
                URL = String.format("%s%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP, ApiConstant.H5, intent.getStringExtra("id"),
                        SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""), SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));
            }
        }

        Log.i(TAG, "initView: " + URL);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mContentView.findViewById(R.id.fl), new FrameLayout.LayoutParams(-1, -1))
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
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("写文章");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_write_article;
    }
}