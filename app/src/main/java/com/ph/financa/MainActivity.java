package com.ph.financa;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.util.NetUtils;
import com.next.easynavigation.constant.Anim;
import com.next.easynavigation.utils.NavigationUtil;
import com.next.easynavigation.view.EasyNavigationBar;
import com.ph.financa.activity.WriteArticleActivity;
import com.ph.financa.dialog.AddDialog;
import com.ph.financa.fragments.CustomerFragment;
import com.ph.financa.fragments.HomeFragment;
import com.ph.financa.fragments.MeFragment;
import com.ph.financa.fragments.SeeFragment;
import com.vise.xsnow.permission.OnPermissionCallback;
import com.vise.xsnow.permission.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.base.BaseActivity;
import tech.com.commoncore.utils.FastUtil;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {

    private EasyNavigationBar mNavigationBar;

    private String[] tabText = {"首页", "谁看了我", "", "客户", "我的"};

    //未选中icon
    private int[] normalIcon = {R.mipmap.ic_home_normal, R.mipmap.ic_see_normal, R.mipmap.ic_add_image, R.mipmap.ic_customer_normal, R.mipmap.ic_me_normal};
    //选中时icon
    private int[] selectIcon = {R.mipmap.ic_home_selected, R.mipmap.ic_see_selected, R.mipmap.ic_add_image, R.mipmap.ic_customer_selected, R.mipmap.ic_me_selected};

    private List<Fragment> fragments;

    private AddDialog mAddDialog;
    private HomeFragment mHomeFragment;

    public EasyNavigationBar getNavigationBar() {
        return mNavigationBar;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
//
        /*测试*/
//        View decorView = getWindow().getDecorView();
//        int option = View.SYSTEM_UI_FLAG_FULLSCREEN;
//        decorView.setSystemUiVisibility(option);
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.hide();


//        StatusBarUtil.setRootViewFitsSystemWindows(this, false);

//        getWindow()
//                .getDecorView()
//                .setSystemUiVisibility(
//                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        /*FastUtil.startActivity(mContext, LoginActivity.class);*/
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
//        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));

        PermissionManager.instance().request(mContext, new OnPermissionCallback() {
            @Override
            public void onRequestAllow(String permissionName) {
                Log.i(TAG, "onRequestAllow: " + permissionName);
            }

            @Override
            public void onRequestRefuse(String permissionName) {
                Log.i(TAG, "onRequestRefuse: " + permissionName);
            }

            @Override
            public void onRequestNoAsk(String permissionName) {
                Log.i(TAG, "onRequestNoAsk: " + permissionName);
            }
        }, Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION);


        mNavigationBar = findViewById(R.id.navigationBar);

        fragments = new ArrayList<>();
        mHomeFragment = new HomeFragment();
        fragments.add(mHomeFragment);
        fragments.add(new SeeFragment());
        fragments.add(new CustomerFragment());
        fragments.add(new MeFragment());

        mNavigationBar.titleItems(tabText)
                .normalIconItems(normalIcon)
                .selectIconItems(selectIcon)
                .fragmentList(fragments)
                .fragmentManager(getSupportFragmentManager())
                .addLayoutRule(EasyNavigationBar.RULE_BOTTOM)
                .addLayoutBottom(100)
                .onTabClickListener((view, position) -> {
                    if (position == 4) {
                        /*Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();*/
                        //return true则拦截事件、不进行页面切换
//                            return true;
                    } else if (position == 2) {
                        mAddDialog = AddDialog.show(getSupportFragmentManager(), "", index -> {
                            switch (index) {
                                case 0:
                                    mAddDialog.dismiss();
                                    FastUtil.startActivity(mContext, WriteArticleActivity.class);
                                    break;
                                case 1:
                                    // TODO: 2019/9/10 粘贴文章链接
                                    break;
                                case 2:
                                    mAddDialog.dismiss();
                                    break;
                            }
                        });
                    }
                    return false;
                })
                .mode(EasyNavigationBar.MODE_ADD)
                .anim(Anim.ZoomIn)
                .build();

        mNavigationBar.setAddViewLayout(createView());

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new ConnectionListener());


//        StatusBarUtil.setRootViewFitsSystemWindows(this,true);
//        //设置状态栏透明
//        StatusBarUtil.setTranslucentStatus(this);
//        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
//        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
//        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
//            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
//            //这样半透明+白=灰, 状态栏的文字能看得清
////            StatusBarUtil.setStatusBarColor(this,0x55000000);
//        }

//        StatusBarUtils.setPaddingSmart(this, mNavigationBar);
//        mNavigationBar.setPadding(0, DisplayUtil.getStatusBarHeight(),0,0);

    }

    //实现ConnectionListener接口
    private class ConnectionListener implements EMConnectionListener {
        @Override
        public void onConnected() {
            Log.i(TAG, "onConnected: ");
        }

        @Override
        public void onDisconnected(final int error) {
            runOnUiThread(() -> {
                if (error == EMError.USER_REMOVED) {
                    // 显示帐号已经被移除
                    Log.i(TAG, "onDisconnected: 显示帐号已经被移除");
                } else if (error == EMError.USER_LOGIN_ANOTHER_DEVICE) {
                    // 显示帐号在其他设备登录
                    Log.i(TAG, "onDisconnected: 显示帐号在其他设备登录");
                } else {
                    if (NetUtils.hasNetwork(mContext)) {
                        //连接不到聊天服务器
                        Log.i(TAG, "onDisconnected: 连接不到聊天服务器");
                    } else {
                        //当前网络不可用，请检查网络设置
                        Log.i(TAG, "onDisconnected: 当前网络不可用，请检查网络设置");
                    }
                }
            });
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        Log.i(TAG, "onNewIntent: 分享回调来这里了———————-");
    }


    private View createView() {
        ViewGroup view = (ViewGroup) View.inflate(mContext, R.layout.dialog_add, null);
        view.findViewById(R.id.tv_cancel).setOnClickListener(view13 -> closeAnimation());

        view.findViewById(R.id.ll_edit).setOnClickListener(view1 -> {
            FastUtil.startActivity(mContext, WriteArticleActivity.class);
        });
        view.findViewById(R.id.ll_share_link).setOnClickListener(view12 -> {
            // TODO: 2019/9/10 粘贴文章链接
        });
        return view;
    }

    /**
     * 关闭window动画
     */
    private void closeAnimation() {
        try {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                int x = NavigationUtil.getScreenWidth(this) / 2;
                int y = (NavigationUtil.getScreenHeith(this) - NavigationUtil.dip2px(this, 25));
                Animator animator = ViewAnimationUtils.createCircularReveal(mNavigationBar.getAddViewLayout(), x,
                        y, mNavigationBar.getAddViewLayout().getHeight(), 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //							layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mNavigationBar.getAddViewLayout().setVisibility(View.GONE);
                        //dismiss();
                    }
                });
                animator.setDuration(300);
                animator.start();
            }
        } catch (Exception e) {
            Log.i(TAG, "closeAnimation: " + e.toString());
        }
    }


//    @Override
//    public void onBackPressed() {
////        quitApp();
//        switch (mNavigationBar.getmViewPager().getCurrentItem()) {
//            case 0:
//                Log.i(TAG, "onBackPressed: " + mHomeFragment.back());
//                break;
//        }
//    }

//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        switch (mNavigationBar.getmViewPager().getCurrentItem()) {
//            case 0:
//                if (!mHomeFragment.back()){
//                    if (keyCode == KeyEvent.KEYCODE_BACK) {
//                        Intent home = new Intent(Intent.ACTION_MAIN);
//                        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        home.addCategory(Intent.CATEGORY_HOME);
//                        startActivity(home);
//                        return true;
//                    }
//                }
//                break;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

}