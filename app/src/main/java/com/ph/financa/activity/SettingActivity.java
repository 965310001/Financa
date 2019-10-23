package com.ph.financa.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.constant.Constant;
import com.ph.financa.dialog.MessageAlertDialog;
import com.ph.financa.ease.FriendTable;
import com.ph.financa.utils.UserUtils;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import butterknife.BindView;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.RegUtils;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 设置
 */
public class SettingActivity extends BaseTitleActivity {

    @BindView(R.id.tv_version)
    TextView tvVersion;/*版本号*/
    @BindView(R.id.tv_phone)
    TextView tvPhone;/*手机号码*/

    private MessageAlertDialog messageAlertDialog;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("设置");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));

        tvVersion.setText(String.format("v%s", FastUtil.getVersionName(mContext)));

        tvPhone.setText(RegUtils.isAsterisk(SPHelper.getStringSF(mContext, Constant.USERPHONE, "")));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_feedback:
                // TODO: 2019/9/9 反馈
                Bundle bundle = new Bundle();
                bundle.putString(FriendTable.FRIEND_NAME, "我的客服");
                bundle.putString(FriendTable.FRIEND_HEAD, "客服");
                FastUtil.startActivity(mContext, CustomerActivity.class, bundle);
                break;
            case R.id.rl_version:
                // TODO: 2019/9/9 版本
                break;
            case R.id.rl_change_phone:
                // TODO: 2019/9/9 更改手机号
                FastUtil.startActivity(mContext, ChangePhoneActivity.class);
                break;
            case R.id.rl_policy:
                // TODO: 2019/9/9 用户协议
                goActivity("用户协议", String.format("%s%s", ApiConstant.BASE_URL_ZP, ApiConstant.PROTOCOL));
                break;
            case R.id.rl_agreement:
                // TODO: 2019/9/9 隐私协议
                goActivity("隐私政策", String.format("%s%s", ApiConstant.BASE_URL_ZP, ApiConstant.AGREEMENT));
                break;
            case R.id.tv_quit:
                // TODO: 2019/9/9 退出登录
                messageAlertDialog = MessageAlertDialog.show(getSupportFragmentManager(), "", "是否退出本账号", "",
                        index -> {
                            messageAlertDialog.dismiss();
                            switch (index) {
                                case 1:
                                    quitUser();
                                    break;
                            }
                        });
                break;
        }
    }

    private void goActivity(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        FastUtil.startActivity(mContext, WebActivity.class, bundle);
    }

    private void quitUser() {
        showLoading();

        ViseHttp.POST(ApiConstant.LOGINOUT)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.isSuccess()) {
                            logoutEaseMob();
                        } else {
                            hideLoading();
                            ToastUtil.show(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        hideLoading();
                        ToastUtil.show(errMsg);
                    }
                });

    }

    /*退出环信*/
    private void logoutEaseMob() {
        Observable.create(subscriber -> EMClient.getInstance().logout(true, new EMCallBack() {
            @Override
            public void onSuccess() {
                subscriber.onNext(0);
                subscriber.onComplete();
            }

            @Override
            public void onProgress(int progress, String status) {
            }

            @Override
            public void onError(int code, String message) {
                subscriber.onNext(message);
                subscriber.onComplete();
            }
        })).subscribeOn(Schedulers.newThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Object>() {
            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(Object obj) {
                if (obj instanceof Integer) {
                    UserUtils.logout();
                    FastUtil.startActivity(mContext, LoginActivity.class);
                    finish();
                } else if (obj instanceof String) {
                    ToastUtil.show(obj.toString());
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
}