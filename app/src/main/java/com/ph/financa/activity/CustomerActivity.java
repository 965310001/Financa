package com.ph.financa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

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


        TextView mTvSend = findViewById(R.id.tv_send);
        AppCompatEditText mEtInput = findViewById(R.id.et_input);
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    mTvSend.setBackgroundResource(R.drawable.shape_send_selector);
                    mTvSend.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    mTvSend.setBackgroundResource(R.drawable.shape_send_nomal);
                    mTvSend.setTextColor(Color.parseColor("#BBBBBB"));
                }
            }
        });
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                break;
        }
    }
}
