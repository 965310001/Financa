package com.ph.financa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;

import com.ph.financa.MainActivity;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;
import com.ph.financa.utils.easeui.DemoHelper;

import tech.com.commoncore.base.BaseActivity;
import tech.com.commoncore.utils.AppStatusManager;
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
    public void beforeSetContentView() {
        if (getIntent() != null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        super.beforeSetContentView();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        new Handler().postDelayed(() -> {
            AppStatusManager.getInstance().setAppStatus(AppStatusManager.STATUS_NORMAL);
            if (!getBooleanSF(Constant.ISGUIDE)) {/*过渡页*/
                FastUtil.startActivity(mContext, WelcomeGuideActivity.class);
            } else if (getBooleanSF(Constant.ISLOGIN) && DemoHelper.getInstance().isLoggedIn()) {/*登录*/
                if (!getBooleanSF(Constant.ISVERIFPHONE)) {/*填写手机号*/
                    FastUtil.startActivity(mContext, SendCodeActivity.class);
                } else {
                    FastUtil.startActivity(mContext, MainActivity.class);
                }
            } else {
                FastUtil.startActivity(mContext, LoginActivity.class);
            }
            finish();
        }, 300);
    }

    private boolean getBooleanSF(String key) {
        return SPHelper.getBooleanSF(mContext, key, false);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }
}