package com.ph.financa;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMClientListener;
import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMMultiDeviceListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.NetUtils;
import com.next.easynavigation.constant.Anim;
import com.next.easynavigation.utils.NavigationUtil;
import com.next.easynavigation.view.EasyNavigationBar;
import com.ph.financa.activity.PasteArticleActivity;
import com.ph.financa.activity.SplashActivity;
import com.ph.financa.activity.WebActivity;
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
import com.ph.financa.service.BadgeIntentService;
import com.ph.financa.utils.easeui.DemoHelper;
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
import tech.com.commoncore.utils.AppStatus;
import tech.com.commoncore.utils.AppStatusManager;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.Utils;

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

        updateUnreadLabel();
        super.onResume();

        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.pushActivity(this);
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);

        refreshUIWithMessage();
    }

    @Override
    protected void onPause() {
        isForeground = false;

        super.onPause();

        EMClient.getInstance().chatManager().removeMessageListener(mMessageListener);
        EMClient.getInstance().removeClientListener(clientListener);
        DemoHelper sdkHelper = DemoHelper.getInstance();
        sdkHelper.popActivity(this);
    }

    EMClientListener clientListener = success -> {
        Toast.makeText(MainActivity.this, "onUpgradeFrom 2.x to 3.x " + (success ? "success" : "fail"), Toast.LENGTH_LONG).show();
        if (success) {
            Log.i(TAG, "onMigrate2x: ");
            refreshUIWithMessage();
        }
    };

/*    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }*/

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
    public void beforeSetContentView() {
        if (AppStatusManager.getInstance().getAppStatus() == AppStatus.STATUS_RECYVLE) {
            //跳到闪屏页
            Log.i(TAG, "beforeSetContentView: ");
            FastUtil.startActivity(mContext, SplashActivity.class);
            finish();
            return;
        }
        super.beforeSetContentView();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
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
                                    /*FastUtil.startActivity(mContext, WriteArticleActivity.class);*/
                                    Bundle bundle = new Bundle();
                                    bundle.putString("url", String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP, ApiConstant.ADDARTICLE,
                                            SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""),
                                            SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, "")));
                                    FastUtil.startActivity(mContext, WebActivity.class, bundle);
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

        registerBroadcastReceiver();

        //注册一个监听连接状态的listener
        EMClient.getInstance().addMultiDeviceListener(new MyMultiDeviceListener());
        EMClient.getInstance().addConnectionListener(new ConnectionListener());

        getUserInfo();

        getUnreadMsgCount();
        /*mNavigationBar.setMsgPointCount(1,10);*/
    }

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
//                updateUnreadAddressLable();
//                if (currentTabIndex == 0) {
//                    // refresh conversation list
//                    if (conversationListFragment != null) {
//                        conversationListFragment.refresh();
//                    }
//                } else if (currentTabIndex == 1) {
//                    if (contactListFragment != null) {
//                        contactListFragment.refresh();
//                    }
//                }
//                String action = intent.getAction();
//                if (action.equals(Constant.ACTION_GROUP_CHANAGED)) {
//                    if (EaseCommonUtils.getTopActivity(MainActivity.this).equals(GroupsActivity.class.getName())) {
//                        GroupsActivity.instance.onResume();
//                    }
//                }
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    private void updateUnreadLabel() {
        int count = getUnreadMsgCountTotal();
        startService(new Intent(mContext, BadgeIntentService.class).putExtra("badgeCount", 11));
    }

//    public int getUnreadMsgCountTotal() {
//        return EMClient.getInstance().chatManager().getUnreadMessageCount();
//    }


    private void getUnreadMsgCount() {
        /*用户*/
        EMClient.getInstance().chatManager().addMessageListener(mMessageListener);
    }

    private EMMessageListener mMessageListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {
                DemoHelper.getInstance().getNotifier().vibrateAndPlayTone(message);
                try {
                    Log.i(TAG, "onMessageReceived: " + message.getStringAttribute("otherUserNickName"));
                    Log.i(TAG, "onMessageReceived: " + message.getStringAttribute("otherUserPortrait"));


                    Log.i(TAG, "onMessageReceived: " + message.getStringAttribute("nickName"));
                    Log.i(TAG, "onMessageReceived: " + message.getStringAttribute("UserPortrait"));

                } catch (HyphenateException e) {
                    Log.i(TAG, "onMessageReceived: " + e.toString());
                }
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            Log.i(TAG, "onCmdMessageReceived: ");
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {

        }

        @Override
        public void onMessageDelivered(List<EMMessage> messages) {

        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            Log.i(TAG, "onMessageRecalled: ");
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    void refreshUIWithMessage() {
        runOnUiThread(() -> {
            int count = getUnreadMsgCountTotal();
            if (count > 0) {
                Log.i(TAG, "onMessageReceived: ");
                mNavigationBar.setMsgPointCount(1, count);
                /*发送广播*/
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setAction(Constant.MESS_BROADCAST);
                sendBroadcast(intent);
            } else {
                mNavigationBar.clearMsgPoint(1);
            }
        });
    }

    int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getUnreadMessageCount();
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

//        ShortcutBadger.applyCount(mContext, 2); //for 1.1.3


        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);


        /*设置聊天条数*/
//        startService(new Intent(mContext, BadgeIntentService.class).putExtra("badgeCount", 11));

        /*删除图标条数*/
//        ShortcutBadger.removeCount(mContext);


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