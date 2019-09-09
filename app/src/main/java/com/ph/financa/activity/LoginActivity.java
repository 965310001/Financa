package com.ph.financa.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.ph.financa.R;
import com.ph.financa.constant.Constant;
import com.ph.financa.wxapi.pay.JPayListener;
import com.ph.financa.wxapi.pay.WeiXinBaoStrategy;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import tech.com.commoncore.base.BaseActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 登录
 */
public class LoginActivity extends BaseActivity {

    /*private IWXAPI mApi;*/

    @Override
    public void initView(Bundle savedInstanceState) {
        final SpannableStringBuilder style = new SpannableStringBuilder("登录表示同意《用户协议》《隐私政策》");

        style.setSpan(mProtocolClick, style.toString().indexOf("《用"), style.toString().indexOf("议》") + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(mPrivacyClick, style.toString().indexOf("《隐"), style.toString().indexOf("策》") + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        TextView tvProtocol = findViewById(R.id.tv_protocol);

        style.setSpan(new ClickableSpan1(), style.toString().indexOf("《用"), style.toString().indexOf("策》") + 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        tvProtocol.setText(style);
        tvProtocol.setMovementMethod(LinkMovementMethod.getInstance());
    }

    class ClickableSpan1 extends CharacterStyle {

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(Color.parseColor("#407DFC"));
            ds.setUnderlineText(false);
        }
    }

    private ClickableSpan mProtocolClick = new ClickableSpan() {
        @Override
        public void onClick(View view) {
            goActivity("用户协议", ApiConstant.PROTOCOL);
        }
    };

    private ClickableSpan mPrivacyClick = new ClickableSpan() {
        @Override
        public void onClick(View view) {
            goActivity("隐私政策", ApiConstant.AGREEMENT);
        }
    };

    private void goActivity(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("url", url);
        FastUtil.startActivity(mContext, WebActivity.class, bundle);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_login:
                loginWeixin();
                break;
        }
    }

    private void loginWeixin() {
        WeiXinBaoStrategy weiXinBaoStrategy = WeiXinBaoStrategy.getInstance(mContext);
        weiXinBaoStrategy.login(Constant.WECHATAPPKEY, new JPayListener() {
            @Override
            public void onPaySuccess() {
                String code = SPHelper.getStringSF(mContext, Constant.WEIXINCODE, "");
                if (!TextUtils.isEmpty(code)) {
                    getWXUserInfo(code);
                    SPHelper.setStringSF(mContext, Constant.WEIXINCODE, "");
                }
            }

            @Override
            public void onPayError(int error_code, String message) {
                ToastUtil.show(message);
            }

            @Override
            public void onPayCancel() {
            }

            @Override
            public void onUUPay(String dataOrg, String sign, String mode) {

            }
        });


    }

    private void getWXUserInfo(String code) {
        Map<String, String> params = new HashMap();
        params.put("appid", Constant.WEIXIN_APP_ID);
        params.put("secret", Constant.WECHATAPPSECRET);
        params.put("code", code);
        params.put("grant_type", "authorization_code");

        ViseHttp.GET("https://api.weixin.qq.com/sns/oauth2/access_token")
                .addParams(params)
                .request(new ACallback<WXAccessTokenBean>() {
                    @Override
                    public void onSuccess(WXAccessTokenBean data) {

                        HashMap<String, String> globalHeaders = new HashMap<>();
                        globalHeaders.put("openId", SPHelper.getStringSF(mContext, Constant.WXOPENID));
                        globalHeaders.put("userId", SPHelper.getStringSF(mContext, Constant.USERID));
                        ViseHttp.CONFIG().globalHeaders(globalHeaders);

                        SPHelper.setStringSF(mContext, Constant.WXOPENID, "");

                        Map<String, String> params = new HashMap<>();
                        params.put("nickname", "");
                        params.put("headImgUrl", "");
                        params.put("country", "");
                        params.put("province", "");
                        params.put("city", "");
                        params.put("openId", "");
                        ViseHttp.POST(ApiConstant.LOGIN)
                                .addParams(params).request(new ACallback<Object>() {
                            @Override
                            public void onSuccess(Object data) {
                                saveUser(data);
                                if (true) {
                                    FastUtil.startActivity(mContext, SendCodeActivity.class);
                                }
                            }

                            @Override
                            public void onFail(int errCode, String errMsg) {
                                ToastUtil.show(errMsg);
                            }
                        });
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });
    }

    /*保存用户信息*/
    private void saveUser(Object data) {
        SPHelper.setStringSF(mContext, Constant.USERNAME, data.toString());
        SPHelper.setStringSF(mContext, Constant.USERCOMPANYNAME, data.toString());
        SPHelper.setStringSF(mContext, Constant.USERHEAD, data.toString());
        SPHelper.setStringSF(mContext, Constant.USERID, data.toString());
    }

    class WXAccessTokenBean implements Serializable {

    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_login;
    }

}
