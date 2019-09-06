package com.ph.financa.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.aries.ui.view.title.TitleBarView;
import com.ph.financa.R;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.RegUtils;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 发送验证码和验证
 */
public class SendCodeActivity extends BaseTitleActivity {

    private TextView mTvSendCode, mTvErrCode;
    private AppCompatEditText mEtPhone, mEtCode;

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
                .request(new ACallback<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        hideLoading();
                        if (true) {
                            Log.i(TAG, "onSuccess: 发送成功");
                            countdownTime();
                        } else {
                            Log.i(TAG, "onSuccess: 发送失败");
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

    }

    /*执行下一步*/
    private void doGet() {
        showLoading();

        String phone = getPhone();
        String code = getCode();
        ViseHttp.POST(ApiConstant.VERIFICATION_CODE).addParam("phone", phone)
                .addParam("code", code)
                .request(new ACallback<Object>() {
                    @Override
                    public void onSuccess(Object data) {
                        hideLoading();
                        if (true) {
                            Log.i(TAG, "onSuccess: 发送成功，");
                            FastUtil.startActivity(mContext, EditCompanyActivity.class);
                        } else if (true) {/*验证码失败*/
                            hideErrCode(false);
                        } else {
                            Log.i(TAG, "onSuccess: 执行下一步失败");
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
}