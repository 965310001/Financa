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

    private EasyNavigationBar navigationBar;

    private String[] tabText = {"首页", "谁看了我", "", "客户", "我的"};

    //未选中icon
    private int[] normalIcon = {R.mipmap.ic_home_normal, R.mipmap.ic_see_normal, R.mipmap.ic_add_image, R.mipmap.ic_customer_normal, R.mipmap.ic_me_normal};
    //选中时icon
    private int[] selectIcon = {R.mipmap.ic_home_selected, R.mipmap.ic_see_selected, R.mipmap.ic_add_image, R.mipmap.ic_customer_selected, R.mipmap.ic_me_selected};

    private List<Fragment> fragments;

    private AddDialog mAddDialog;

    public EasyNavigationBar getNavigationBar() {
        return navigationBar;
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        /*测试*/
        /*FastUtil.startActivity(mContext, LoginActivity.class);*/

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


        navigationBar = findViewById(R.id.navigationBar);

        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new SeeFragment());
        fragments.add(new CustomerFragment());
        fragments.add(new MeFragment());

        navigationBar.titleItems(tabText)
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

        navigationBar.setAddViewLayout(createView());

        //注册一个监听连接状态的listener
        EMClient.getInstance().addConnectionListener(new ConnectionListener());
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
                Animator animator = ViewAnimationUtils.createCircularReveal(navigationBar.getAddViewLayout(), x,
                        y, navigationBar.getAddViewLayout().getHeight(), 0);
                animator.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        //							layout.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        navigationBar.getAddViewLayout().setVisibility(View.GONE);
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


}