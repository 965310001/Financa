package com.ph.financa.activity;

import android.content.Intent;
import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.utils.SPHelper;

/**
 * 我的客服
 */
public class CustomerActivity extends BaseTitleActivity {

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_customer;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
            mTitleBar.setTitleMainText(intent.getStringExtra("title"));
        } else {
            mTitleBar.setTitleMainText("我的客服");
        }

        String id;
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id");
        } else {
            id = SPHelper.getStringSF(mContext, Constant.USERID, "");
        }


    }
}
