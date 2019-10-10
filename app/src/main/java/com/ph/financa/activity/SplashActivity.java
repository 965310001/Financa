package com.ph.financa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatDelegate;

import com.ph.financa.MainActivity;
import com.ph.financa.R;
import com.ph.financa.activity.bean.AppStatus;
import com.ph.financa.activity.bean.AppStatusManager;
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
    public void beforeSetContentView() {
        super.beforeSetContentView();
        if (getIntent() != null && (getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        new Handler().postDelayed(() -> {
            if (!getBooleanSF(Constant.ISGUIDE)) {/*过渡页*/
                FastUtil.startActivity(mContext, WelcomeGuideActivity.class);
            } else if (getBooleanSF(Constant.ISLOGIN)) {/*登录*/
                if (!getBooleanSF(Constant.ISVERIFPHONE)) {/*填写手机号*/
                    FastUtil.startActivity(mContext, SendCodeActivity.class);
                } else {
                    FastUtil.startActivity(mContext, MainActivity.class);
                }
            } else {
                FastUtil.startActivity(mContext, LoginActivity.class);
            }
            AppStatusManager.getInstance().setAppStatus(AppStatus.STATUS_NORMAL);

//            List<State> states = Arrays.asList(new WelcomeGuideState(), new SendCodeState(), new MainState(), new LoginState());
//            for (State state : states) {
//                if (state.getStatus()) {
//                    FastUtil.startActivity(mContext, state.getClazz());
//                    break;
//                }
//            }
            finish();
        }, 1000);
    }

    interface State {
        boolean getStatus();

        Class getClazz();
    }

    /*过渡页*/
    class WelcomeGuideState implements State {

        @Override
        public boolean getStatus() {
            return !getBooleanSF(Constant.ISGUIDE);
        }

        @Override
        public Class getClazz() {
            return WelcomeGuideActivity.class;
        }
    }

    /*发送验证码*/
    class SendCodeState implements State {

        @Override
        public boolean getStatus() {
            return getBooleanSF(Constant.ISLOGIN) && !getBooleanSF(Constant.ISVERIFPHONE);
        }

        @Override
        public Class getClazz() {
            return SendCodeActivity.class;
        }
    }

    /*主界面*/
    class MainState implements State {

        @Override
        public boolean getStatus() {
            return getBooleanSF(Constant.ISLOGIN) && getBooleanSF(Constant.ISVERIFPHONE);
        }

        @Override
        public Class getClazz() {
            return MainActivity.class;
        }
    }

    /*登录*/
    class LoginState implements State {

        @Override
        public boolean getStatus() {
            return true;
        }

        @Override
        public Class getClazz() {
            return LoginActivity.class;
        }
    }

    private boolean getBooleanSF(String key) {
        return SPHelper.getBooleanSF(mContext, key, false);
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_splash;
    }

    //    public void scaleImage(final Activity activity, final View view, int drawableResId) {
//
//        // 获取屏幕的高宽
//        Point outSize = new Point();
//        activity.getWindow().getWindowManager().getDefaultDisplay().getSize(outSize);
//
//        // 解析将要被处理的图片
//        Bitmap resourceBitmap = BitmapFactory.decodeResource(activity.getResources(), drawableResId);
//
//        if (resourceBitmap == null) {
//            return;
//        }
//
//        // 开始对图片进行拉伸或者缩放
//
//        // 使用图片的缩放比例计算将要放大的图片的高度
//        int bitmapScaledHeight = Math.round(resourceBitmap.getHeight() * outSize.x * 1.0f / resourceBitmap.getWidth());
//
//        // 以屏幕的宽度为基准，如果图片的宽度比屏幕宽，则等比缩小，如果窄，则放大
//        final Bitmap scaledBitmap = Bitmap.createScaledBitmap(resourceBitmap, outSize.x, bitmapScaledHeight, false);
//
//        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                //这里防止图像的重复创建，避免申请不必要的内存空间
//                if (scaledBitmap.isRecycled())
//                    //必须返回true
//                    return true;
//
//
//                // 当UI绘制完毕，我们对图片进行处理
//                int viewHeight = view.getMeasuredHeight();
//
//
//                // 计算将要裁剪的图片的顶部以及底部的偏移量
//                int offset = (scaledBitmap.getHeight() - viewHeight) / 2;
//
//
//                // 对图片以中心进行裁剪，裁剪出的图片就是非常适合做引导页的图片了
//                Bitmap finallyBitmap = Bitmap.createBitmap(scaledBitmap, 0, offset, scaledBitmap.getWidth(),
//                        scaledBitmap.getHeight() - offset * 2);
//
//
//                if (!finallyBitmap.equals(scaledBitmap)) {//如果返回的不是原图，则对原图进行回收
//                    scaledBitmap.recycle();
//                    System.gc();
//                }
//
//
//                // 设置图片显示
//                view.setBackgroundDrawable(new BitmapDrawable(getResources(), finallyBitmap));
//                return true;
//            }
//        });
//    }
}
