package com.ph.financa.activity;

import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;

import com.githang.statusbar.StatusBarCompat;
import com.ph.financa.MainActivity;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;

import tech.com.commoncore.base.BaseActivity;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;

/**
 * 启动页
 */
public class SplashActivity extends BaseActivity {

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));

        new Handler().postDelayed(() -> {
            if (SPHelper.getBooleanSF(mContext, Constant.ISLOGIN, false)) {
                if (!SPHelper.getBooleanSF(mContext, Constant.ISVERIFPHONE, false)) {/*填写手机号*/
                    FastUtil.startActivity(mContext, SendCodeActivity.class);
                } else {
                    FastUtil.startActivity(mContext, MainActivity.class);
                }
            } else {
                FastUtil.startActivity(mContext, LoginActivity.class);
            }

            /*FastUtil.startActivity(mContext, ChangePhoneActivity.class);*/
            finish();
        }, 1500);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }


}
