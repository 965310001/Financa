package com.ph.financa.activity;

import android.os.Bundle;
import android.os.Handler;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.ph.financa.MainActivity;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;

/**
 * 启动页
 */
public class SplashActivity extends BaseTitleActivity {

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getColor(R.color.white));
        new Handler().postDelayed(() -> {
            if (SPHelper.getStringSF(mContext, Constant.ISLOGIN, "false").equals("true")) {
                FastUtil.startActivity(mContext, MainActivity.class);
            } else {
                FastUtil.startActivity(mContext, LoginActivity.class);
            }
            finish();
        }, 1);
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }


}
