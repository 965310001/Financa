package com.ph.financa;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.next.easynavigation.constant.Anim;
import com.next.easynavigation.utils.NavigationUtil;
import com.next.easynavigation.view.EasyNavigationBar;
import com.ph.financa.activity.PasteArticleActivity;
import com.ph.financa.activity.WriteArticleActivity;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.UserBean;
import com.ph.financa.constant.Constant;
import com.ph.financa.dialog.AddDialog;
import com.ph.financa.fragments.CustomerFragment;
import com.ph.financa.fragments.HomeFragment;
import com.ph.financa.fragments.MeFragment;
import com.ph.financa.fragments.SeeFragment;
import com.ph.financa.jpush.JPushManager;
import com.ph.financa.jpush.MessageReceiver;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.permission.OnPermissionCallback;
import com.vise.xsnow.permission.PermissionManager;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tech.com.commoncore.base.BaseActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 首页
 */
public class MainActivity extends BaseActivity {


    /*极光*/
    public static boolean isForeground = false;
    private MessageReceiver mMessageReceiver;

    private EasyNavigationBar mNavigationBar;

    private String[] tabText = {"首页", "谁看了我", "", "客户", "我的"};

    //未选中icon
    private int[] normalIcon = {R.mipmap.ic_home_normal, R.mipmap.ic_see_normal, R.mipmap.ic_add_image, R.mipmap.ic_customer_normal, R.mipmap.ic_me_normal};
    //选中时icon
    private int[] selectIcon = {R.mipmap.ic_home_selected, R.mipmap.ic_see_selected, R.mipmap.ic_add_image, R.mipmap.ic_customer_selected, R.mipmap.ic_me_selected};

    private List<Fragment> fragments;

    private AddDialog mAddDialog;
    private HomeFragment mHomeFragment;


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }

    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(Constant.MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void init() {
        JPushInterface.init(getApplicationContext());
        JPushManager.getInstance().setAlias(mContext, SPHelper.getStringSF(mContext, Constant.USERID, ""));
    }

    @Override
    public void initView(Bundle savedInstanceState) {
//        FastUtil.startActivity(mContext, LoginActivity2.class);
//        Bundle bundle = new Bundle();
//        bundle.putString(EaseConstant.EXTRA_USER_ID, Constant.CUSTOMSERVICE);
//        bundle.putString(FriendTable.FRIEND_NAME, "我的客服");
//        bundle.putString(FriendTable.FRIEND_HEAD, "客服");
//        FastUtil.startActivity(mContext,CustomerActivity.class, bundle);


//        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));

        /*设置全屏并有状态栏 start */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        WindowManager.LayoutParams attrs = getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(attrs);
        /* 设置全屏并有状态栏 end   */

        registerMessageReceiver();  // used for receive msg
        init();
        /*测试*/
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
                }, Manifest.permission.READ_CONTACTS, Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

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
                .selectTextColor(Color.parseColor("#407DFC"))
                .normalTextColor(Color.parseColor("#777777"))
                .fragmentManager(getSupportFragmentManager())
                .addLayoutRule(EasyNavigationBar.RULE_BOTTOM)
                .addLayoutBottom(100)
                .onTabClickListener((view, position) -> {
                    if (position == 0) {
                        Log.i(TAG, "initView: ");
                        mHomeFragment.loadData();
                    } else if (position == 4) {
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
                                    mAddDialog.dismiss();
                                    FastUtil.startActivity(mContext, PasteArticleActivity.class);
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
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
        EMClient.getInstance().addConnectionListener(new ConnectionListener());

        getUserInfo();
    }

    private void loginEaseMob(String id, String password) {
        try {
            List<String> selfIds = EMClient.getInstance().contactManager().getSelfIdsOnOtherPlatform();
            for (String selfId : selfIds) {
                Log.i(TAG, "loginEaseMob: " + selfId);
            }
        } catch (HyphenateException e) {
            e.printStackTrace();
        }

        Observable.create(subscriber -> EMClient.getInstance().login(id, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                subscriber.onNext(200);
                subscriber.onComplete();
            }

            @Override
            public void onError(int i, String s) {
                subscriber.onNext(s);
                subscriber.onComplete();
            }

            @Override
            public void onProgress(int i, String s) {
            }
        })).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Object obj) {
                if (obj instanceof Integer) {
//                    hideLoading();
                    Log.i(TAG, "onSuccess: 环信登录成功");
                    EMClient.getInstance().groupManager().loadAllGroups();
                    EMClient.getInstance().chatManager().loadAllConversations();
//                    EMClient.getInstance().pushManager().updatePushNickname(
//                            SPHelper.getStringSF(mContext, Constant.USERNAME, ""));

//                    SPHelper.setBooleanSF(mContext, Constant.ISLOGIN, true);
//                    if (code == 40102002) {
//                        FastUtil.startActivity(mContext, SendCodeActivity.class);
//                        finish();
//                    } else if (code == 200) {
//                        SPHelper.setBooleanSF(mContext, Constant.ISVERIFPHONE, true);
//
//                        FastUtil.startActivity(mContext, MainActivity.class);
//                        finish();
//                    }
//                } else {
//                    hideLoading();
//                    ToastUtil.show(obj.toString());
//                    Log.i(TAG, "环信登录失败:" + obj.toString());
//                }
                }
            }

            @Override
            public void onError(Throwable e) {
                ToastUtil.show(e.toString());
            }

            @Override
            public void onComplete() {

            }
        });
    }


    class MyMultiDeviceListener implements EMMultiDeviceListener {

        @Override
        public void onContactEvent(int event, String target, String ext) {
            Log.i(TAG, "onContactEvent: " + event + " " + target + " " + ext);
        }

        @Override
        public void onGroupEvent(int event, String target, List<String> usernames) {
            for (String username : usernames) {
                Log.i(TAG, "onGroupEvent: " + username);
            }
            Log.i(TAG, "onGroupEvent: " + event + " " + target);
        }
    }

    /*获取用户信息*/
    private void getUserInfo() {
        ViseHttp.GET(ApiConstant.GET_USER)
                .request(new ACallback<BaseTResp2<UserBean>>() {
                    @Override
                    public void onSuccess(BaseTResp2<UserBean> data) {
                        UserBean bean = data.data;
                        if (null != bean) {
                            saveUser(bean);
                            loginEaseMob(String.valueOf(bean.getId()), "123456");
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });
    }

    /*保存用户信息*/
    private void saveUser(UserBean data) {
        if (null != data) {
            SPHelper.setStringSF(mContext, Constant.USERNAME, data.getName());
            SPHelper.setStringSF(mContext, Constant.USERCOMPANYNAME, data.getCompanyName());
            SPHelper.setStringSF(mContext, Constant.USERHEAD, data.getHeadImgUrl());
            SPHelper.setStringSF(mContext, Constant.USERID, String.valueOf(data.getId()));
            SPHelper.setStringSF(mContext, Constant.USERPHONE, data.getTelephone());

            SPHelper.setIntergerSF(mContext, Constant.ISVIP, data.getUserType());
        }
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
                    loginEaseMob(SPHelper.getStringSF(mContext, Constant.USERID, ""), "123456");
                } else {
                    if (NetUtils.hasNetwork(mContext)) {
                        //连接不到聊天服务器
//                        loginEaseMob(SPHelper.getStringSF(mContext, Constant.USERID, ""), "123456");
                        Log.i(TAG, "onDisconnected: 连接不到聊天服务器" + error);
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
//                        layout.setVisibility(View.GONE);
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

    @Override
    public int getContentLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onBackPressed() {
//        switch (mNavigationBar.getmViewPager().getCurrentItem()) {
//            case 0:
//                if (!mHomeFragment.back()){
//                }
//                break;
//        }
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

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