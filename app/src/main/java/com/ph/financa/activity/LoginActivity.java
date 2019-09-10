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
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.ph.financa.MainActivity;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.UserBean;
import com.ph.financa.activity.bean.WXAccessTokenBean;
import com.ph.financa.constant.Constant;
import com.ph.financa.wxapi.pay.JPayListener;
import com.ph.financa.wxapi.pay.WeiXinBaoStrategy;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONObject;

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
        Log.i(TAG, "loginWeixin: ");
        WeiXinBaoStrategy weiXinBaoStrategy = WeiXinBaoStrategy.getInstance(mContext);
        weiXinBaoStrategy.login(Constant.WECHATAPPKEY, new JPayListener() {
            @Override
            public void onPaySuccess() {
                String code = SPHelper.getStringSF(mContext, Constant.WEIXINCODE, "");
                if (!TextUtils.isEmpty(code)) {
                    getAccessToken(code);
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

    private void getAccessToken(String code) {
        Map<String, String> params = new HashMap();
        params.put("appid", Constant.WEIXIN_APP_ID);
        params.put("secret", Constant.WECHATAPPSECRET);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        Log.i(TAG, "getWXUserInfo: ");
        ViseHttp.GET("sns/oauth2/access_token")
                .baseUrl("https://api.weixin.qq.com/")
                .addParams(params)
                .request(new ACallback<WXAccessTokenBean>() {
                    @Override
                    public void onSuccess(WXAccessTokenBean data) {
                        if (TextUtils.isEmpty(data.getAccess_token()) || TextUtils.isEmpty(data.getOpenid())) {
                            ToastUtil.show(data.getErrmsg());
                        } else {
                            getWXUserInfo(data.getAccess_token(), data.getOpenid());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });
    }

    private void getWXUserInfo(String token, final String openid) {
        ViseHttp.GET("sns/userinfo").baseUrl("https://api.weixin.qq.com/")
                .addParam("access_token", token)
                .addParam("openid", openid)
                .request(new ACallback<WXAccessTokenBean>() {
                    @Override
                    public void onSuccess(WXAccessTokenBean data) {
                        Map<String, String> params = new HashMap<>();
                        params.put("nickname", data.getNickname());
                        params.put("headImgUrl", data.getHeadimgurl());
                        params.put("country", data.getCountry());
                        params.put("province", data.getProvince());
                        params.put("city", data.getCity());
                        params.put("openId", data.getOpenid());
                        JSONObject jsonObject = new JSONObject(params);
                        ViseHttp.POST(ApiConstant.LOGIN)
                                .setJson(jsonObject)
                                .request(new ACallback<BaseTResp2<UserBean>>() {
                                    @Override
                                    public void onSuccess(BaseTResp2<UserBean> data) {
                                        UserBean bean = data.data;
                                        if (null != bean) {
                                            saveUser(data.data);
                                        }
                                        Log.i(TAG, "onSuccess: " + data);
                                        if (data.getCode() == 40102002) {
                                            FastUtil.startActivity(mContext, SendCodeActivity.class);
                                        } else if (data.isSuccess()) {
                                            FastUtil.startActivity(mContext, MainActivity.class);
                                        } else {
                                            ToastUtil.show(data.getMsg());
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

                    }
                });

    }

    /*保存用户信息*/
    private void saveUser(UserBean data) {
        if (null != data) {
            String openId = data.getOpenId();
            HashMap<String, String> globalHeaders = new HashMap<>();
            globalHeaders.put("openId", openId);
            globalHeaders.put("userId", String.valueOf(data.getId()));
            ViseHttp.CONFIG().globalHeaders(globalHeaders);
            SPHelper.setStringSF(mContext, Constant.WXOPENID, openId);

            SPHelper.setStringSF(mContext, Constant.USERNAME, data.getName());
            SPHelper.setStringSF(mContext, Constant.USERCOMPANYNAME, data.getCompanyName());
            SPHelper.setStringSF(mContext, Constant.USERHEAD, data.getHeadImgUrl());
            SPHelper.setStringSF(mContext, Constant.USERID, String.valueOf(data.getId()));
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_login;
    }

}
