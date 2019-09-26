package com.ph.financa.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.constant.Constant;
import com.ph.financa.http.NetworkUtil;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.RegUtils;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 更换手机号
 */
public class ChangePhoneActivity extends BaseTitleActivity {

    private TextView mTvSendCode, mTvErrCode, mTvTime;
    private AppCompatEditText mEtPhone, mEtCode;
    private Disposable mdDisposable;

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));
        mTvSendCode = findViewById(R.id.tv_send_code);
        mEtPhone = findViewById(R.id.et_phone);
        mEtCode = findViewById(R.id.et_code);
        mTvErrCode = findViewById(R.id.tv_code_err);
        mTvTime = findViewById(R.id.tv_time);

        TextView tvCurrentPhone = findViewById(R.id.tv_current_phone);
        tvCurrentPhone.setText(String.format("当前手机号：%s", RegUtils.isAsterisk(SPHelper.getStringSF(mContext, Constant.USERPHONE, ""))));
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:/*发送验证码*/
//                if (!checkPhone()) {
//                    return;
//                }
//                sendCode();
                Observable.just(checkPhone()).takeWhile(aBoolean -> aBoolean).subscribe(aBoolean -> sendCode());
                break;

            case R.id.btn_next:/*下一步*/
                Observable.just(checkPhone() && checkCode()).takeWhile(aBoolean -> aBoolean).subscribe(aBoolean -> doGet());
//                if (!checkPhone()) {
//                    return;
//                }
//                if (!checkCode()) {
//                    return;
//                }
//                doGet();
                break;
        }
    }

    /*发送验证码*/
    private void sendCode() {
        showLoading();

        NetworkUtil.sendCode(getPhone(), new ACallback<BaseTResp2>() {
            @Override
            public void onSuccess(BaseTResp2 data) {
                hideLoading();
                if (data.isSuccess()) {
                    countdownTime();
                } else {
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

    private void countdownTime() {
        mdDisposable = Flowable.intervalRange(1, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .map(aLong -> 61 - aLong)
                .doOnNext(aLong -> mTvTime.setText(String.format("%ds", aLong)))
                .doOnSubscribe(subscription -> {
                    mTvSendCode.setVisibility(View.GONE);
                    mTvTime.setVisibility(View.VISIBLE);
                })
                .doOnComplete(() -> {
                    mTvSendCode.setVisibility(View.VISIBLE);
                    mTvTime.setVisibility(View.GONE);
                }).subscribe();
    }

    /*执行下一步*/
    private void doGet() {
        showLoading();

        String phone = getPhone();
        String code = getCode();
        ViseHttp.POST(ApiConstant.UPDATE_PHONE)
                .addParam("phone", phone)
                .addParam("code", code)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        hideLoading();
                        if (data.isSuccess()) {
                            SPHelper.setStringSF(mContext, Constant.USERPHONE, phone);
                            /*FastUtil.startActivity(mContext, EditCompanyActivity.class);*/
                            finish();
                        } else if (data.getCode() == 500) {/*验证码失败*/
                            hideErrCode(false);
                            ToastUtil.show(data.getMsg());
                        } else {
                            Log.i(TAG, "onSuccess: 执行下一步失败");
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

    private boolean checkPhone() {
        if (!RegUtils.isMobile(getPhone())) {
            ToastUtil.show("请填写正确的手机号码");
            return false;
        }
        return true;
    }

    private String getPhone() {
        return mEtPhone.getText().toString().trim();
    }

    private boolean checkCode() {
        if (!RegUtils.isValidateVCode(getCode())) {
            ToastUtil.show("请填写正确的验证码");
            return false;
        }
        return true;
    }

    private String getCode() {
        return mEtCode.getText().toString().trim();
    }

    private void hideErrCode(boolean isHide) {/*true:隐藏*/
        mTvErrCode.setVisibility(isHide ? View.GONE : View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mdDisposable != null) {
            mdDisposable.dispose();
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("更换手机号");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_change_phone;
    }
}