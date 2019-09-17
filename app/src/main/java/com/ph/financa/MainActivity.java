package com.ph.financa;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

    private List<Fragment> fragments = new ArrayList<>();

    //仿微博图片和文字集合
//    private int[] menuIconItems = {R.mipmap.pic1, R.mipmap.pic2, R.mipmap.pic3, R.mipmap.pic4};
//    private String[] menuTextItems = {"文字", "拍摄", "相册", "直播"};
//    private LinearLayout menuLayout;
//    private View cancelImageView;
    /*private MoreDialog moreDialog;*/

    private AddDialog mAddDialog;

    private Handler mHandler = new Handler();

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

        navigationBar = findViewById(R.id.navigationBar);

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
                        //跳转页面（全民K歌）   或者   弹出菜单（微博）
                        /*showMunu();*/
                        mAddDialog = AddDialog.show(getSupportFragmentManager(), "", new AddDialog.OnClickIndex() {
                            @Override
                            public void onIndex(int index) {
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
//        moreDialog = MoreDialog.show(getSupportFragmentManager(), "", new MoreDialog.OnClickIndex() {
//            @Override
//            public void onIndex(int index) {
//                switch (index) {
//                    case 0:
//                        FastUtil.startActivity(mContext, WriteArticleActivity.class);
//                        break;
//                    case 1:
//                        // TODO: 2019/9/10 粘贴文章链接
//                        break;
//                    case 2:
//                        moreDialog.dismiss();
//                        break;
//                }
//            }
//        });
        return view;
    }

    //仿微博弹出菜单
//    private View createWeiboView() {
//        ViewGroup view = (ViewGroup) View.inflate(mContext, R.layout.layout_add_view, null);
//        menuLayout = view.findViewById(R.id.icon_group);
//        cancelImageView = view.findViewById(R.id.cancel_iv);
//        cancelImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                closeAnimation();
//            }
//        });
//        for (int i = 0; i < 4; i++) {
//            View itemView = View.inflate(mContext, R.layout.item_icon, null);
//            ImageView menuImage = itemView.findViewById(R.id.menu_icon_iv);
//            TextView menuText = itemView.findViewById(R.id.menu_text_tv);
//
//            menuImage.setImageResource(menuIconItems[i]);
//            menuText.setText(menuTextItems[i]);
//
//            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.weight = 1;
//            itemView.setLayoutParams(params);
//            itemView.setVisibility(View.GONE);
//            menuLayout.addView(itemView);
//        }
//        return view;
//    }

    /**
     * 关闭window动画
     */
    private void closeAnimation() {
//        mHandler.post(() -> cancelImageView.animate().rotation(0).setDuration(400));
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

    private void showMunu() {
        startAnimation();
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                //＋ 旋转动画
//                cancelImageView.animate().rotation(90).setDuration(400);
//            }
//        });
        //菜单项弹出动画
//        for (int i = 0; i < menuLayout.getChildCount(); i++) {
//            final View child = menuLayout.getChildAt(i);
//            child.setVisibility(View.INVISIBLE);
//            mHandler.postDelayed(() -> {
//                child.setVisibility(View.VISIBLE);
//                ValueAnimator fadeAnim = ObjectAnimator.ofFloat(child, "translationY", 600, 0);
//                fadeAnim.setDuration(500);
//                KickBackAnimator kickAnimator = new KickBackAnimator();
//                kickAnimator.setDuration(500);
//                fadeAnim.setEvaluator(kickAnimator);
//                fadeAnim.start();
//            }, i * 50 + 100);
//        }
    }

    private void startAnimation() {
        mHandler.post(() -> {
            try {
                //圆形扩展的动画
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    int x = NavigationUtil.getScreenWidth(mContext) / 2;
                    int y = (NavigationUtil.getScreenHeith(mContext) - NavigationUtil.dip2px(mContext, 25));
                    Animator animator = ViewAnimationUtils.createCircularReveal(navigationBar.getAddViewLayout(), x,
                            y, 0, navigationBar.getAddViewLayout().getHeight());
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            navigationBar.getAddViewLayout().setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            //							layout.setVisibility(View.VISIBLE);
                        }
                    });
                    animator.setDuration(300);
                    animator.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}