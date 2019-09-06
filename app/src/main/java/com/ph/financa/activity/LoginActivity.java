package com.ph.financa.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.ph.financa.R;

import tech.com.commoncore.base.BaseActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    @Override
    public void initView(Bundle savedInstanceState) {
        final SpannableStringBuilder style = new SpannableStringBuilder("登录表示同意《用户协议》《隐私政策》");

        style.setSpan(mProtocolClick, style.toString().indexOf("《用"), style.toString().indexOf("议》") + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(mPrivacyClick, style.toString().indexOf("《隐"), style.toString().indexOf("策》") + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tvProtocol = findViewById(R.id.tv_protocol);

        style.setSpan(new ClickableSpan1(), style.toString().indexOf("《用"), style.toString().indexOf("策》") + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvProtocol.setText(style);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    class ClickableSpan1 extends CharacterStyle {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#407DFC"));
            ds.setUnderlineText(false);
        }
    }

    private ClickableSpan mProtocolClick = new ClickableSpan() {
        @Override
        public void onClick(View view) {
            goActivity("用户协议", ApiConstant.PROTOCOL);
        }
    };

    private ClickableSpan mPrivacyClick = new ClickableSpan() {
        @Override
        public void onClick(View view) {
            goActivity("隐私政策", ApiConstant.AGREEMENT);
        }
    };

    private void goActivity(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        FastUtil.startActivity(mContext, WebActivity.class, bundle);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_login:
                FastUtil.startActivity(mContext, SendCodeActivity.class);
                break;
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_login;
    }

}
