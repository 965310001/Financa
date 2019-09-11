package com.ph.financa.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.aries.ui.view.title.TitleBarView;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.constant.Constant;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.RegUtils;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 发送验证码和验证
 */
public class SendCodeActivity extends BaseTitleActivity {

    private TextView mTvSendCode, mTvErrCode, mTvTime;
    private AppCompatEditText mEtPhone, mEtCode;
    private Disposable mdDisposable;

    @Override
    public int getContentLayout() {
        return R.layout.activity_send_code;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTvSendCode = findViewById(R.id.tv_send_code);
        mEtPhone = findViewById(R.id.et_phone);
        mEtCode = findViewById(R.id.et_code);
        mTvErrCode = findViewById(R.id.tv_code_err);
        mTvTime = findViewById(R.id.tv_time);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send_code:// TODO: 2019/9/6 发送验证码
                if (!checkPhone()) {
                    return;
                }
                sendCode();
                break;

            case R.id.btn_next:// TODO: 2019/9/6 下一步
                if (!checkPhone()) {
                    return;
                }
                if (!checkCode()) {
                    return;
                }
                doGet();
                break;

        }
    }

    /*发送验证码*/
    private void sendCode() {
        showLoading();
        String phone = getPhone();
        ViseHttp.POST(ApiConstant.SEND_CODE)
                .addParam("phone", phone)
                .request(new ACallback<BaseTResp2>() {
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
        mTvSendCode.setVisibility(View.GONE);
        mTvTime.setVisibility(View.VISIBLE);

        mdDisposable = Flowable.intervalRange(1, 61, 0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(aLong -> {
                    mTvTime.setText(String.format("%ds", (61 - aLong)));
                })
                .doOnComplete(() -> {
                    mTvSendCode.setVisibility(View.VISIBLE);
                    mTvTime.setVisibility(View.GONE);
                })
                .subscribe();
    }

    /*执行下一步*/
    private void doGet() {
        showLoading();

        String phone = getPhone();
        String code = getCode();
        ViseHttp.POST(ApiConstant.VERIFICATION_CODE)
                .addParam("phone", phone)
                .addParam("code", code)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        hideLoading();
                        if (data.isSuccess()) {
                            SPHelper.setStringSF(mContext, Constant.USERPHONE, phone);
                            FastUtil.startActivity(mContext, EditCompanyActivity.class);
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
        if (!RegUtils.isMobile(mEtPhone.getText().toString().trim())) {
            ToastUtil.show("请填写正确的手机号码");
            return false;
        }
        return true;
    }

    private String getPhone() {
        return mEtPhone.getText().toString().trim();
    }

    private boolean checkCode() {
        if (!RegUtils.isValidateVCode(mEtCode.getText().toString().trim())) {
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
    public void setTitleBar(TitleBarView titleBar) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mdDisposable != null) {
            mdDisposable.dispose();
        }
    }
}