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

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.ph.financa.MainActivity;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.UserBean;
import com.ph.financa.activity.bean.WXAccessTokenBean;
import com.ph.financa.constant.Constant;
import com.ph.financa.utils.UserUtils;
import com.ph.financa.utils.easeui.DemoHelper;
import com.ph.financa.wxapi.pay.JPayListener;
import com.ph.financa.wxapi.pay.WeiXinBaoStrategy;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
 * 登录
 */
public class LoginActivity extends BaseActivity {

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
            goActivity("用户协议", String.format("%s%s", ApiConstant.BASE_URL_ZP, ApiConstant.PROTOCOL));
        }
    };

    private ClickableSpan mPrivacyClick = new ClickableSpan() {
        @Override
        public void onClick(View view) {
            goActivity("隐私政策", String.format("%s%s", ApiConstant.BASE_URL_ZP, ApiConstant.AGREEMENT));
        }
    };

    private void goActivity(String title, String url) {
        Bundle bundle = new Bundle();
        bundle.putString(Constant.TITLE, title);
        bundle.putString(Constant.URL, url);
        FastUtil.startActivity(mContext, WebActivity.class, bundle);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_login:
                loginWeixin();
                break;

            case R.id.tv_visitor_login:/*游客登录*/
                visitorLogin();
                break;
        }
    }


    private void visitorLogin() {
        showLoading();
        ViseHttp.POST(String.format("%s%s", ApiConstant.BASE_URL_ZP, ApiConstant.ANONYMOUS_LOGIN))
                .request(new ACallback<BaseTResp2<UserBean>>() {
                    @Override
                    public void onSuccess(BaseTResp2<UserBean> data) {
                        hideLoading();
                        Log.i(TAG, "onSuccess: " + data.toString());
                        UserBean bean = data.data;
                        if (null != bean) {
                            saveUser(bean);
                        }

                        if (data.isSuccess() || data.getCode() == 40102002) {
                            loginEaseMob(String.valueOf(bean.getId()), "123456", data.getCode());
                        } else {
                            Log.i(TAG, "onSuccess: " + data.getMsg());
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

    @Override
    public void loadData() {
        super.loadData();

        getVisitorLogin();
    }

    private void getVisitorLogin() {
        ViseHttp.GET(String.format("%s%s", ApiConstant.BASE_URL_ZP, ApiConstant.VISITORLOGIN)).request(new ACallback<Object>() {
            @Override
            public void onSuccess(Object data) {
                Log.i(TAG, "onSuccess: " + data);
                if (null != data) {
                    String str = data.toString();
                    if (!TextUtils.isEmpty(str) && str.contains("yes")) {/*ios ye, andorid no*/
                        mContentView.findViewById(R.id.tv_visitor_login).setVisibility(View.VISIBLE);
                    } else {
                        mContentView.findViewById(R.id.tv_visitor_login).setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFail(int errCode, String errMsg) {
                ToastUtil.show(errMsg);
            }
        });
    }

    private void loginWeixin() {
        WeiXinBaoStrategy weiXinBaoStrategy = WeiXinBaoStrategy.getInstance(mContext);
        weiXinBaoStrategy.login(Constant.WECHATAPPKEY, new JPayListener() {
            @Override
            public void onPaySuccess() {
//                /*Log.i(TAG, "onPaySuccess: " + SPHelper.getStringSF(mContext, "WEIXIN_USER"));*/
//                String userInfo = SPHelper.getStringSF(mContext, "WEIXIN_USER", "");
//                Log.i(TAG, "onPaySuccess: " + userInfo);
//                if (!TextUtils.isEmpty(userInfo)) {
//                    try {
//                        JSONObject data = new JSONObject(userInfo);
//                        Map<String, String> params = new HashMap<>();
//                        params.put("nickname", data.getString("nickname"));
//                        params.put("headImgUrl", data.getString("headImgUrl"));
//                        params.put("country", data.getString("country"));
//                        params.put("province", data.getString("province"));
//                        params.put("city", data.getString("city"));
//                        params.put("openId", data.getString("openId"));
//                        loginUser(params);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                /*SPHelper.getStringSF(mContext,"WEIXIN_USER");*/
                String code = SPHelper.getStringSF(mContext, Constant.WEIXINCODE, "");
                if (!TextUtils.isEmpty(code)) {
                    getAccessToken(code);
                    SPHelper.setStringSF(mContext, Constant.WEIXINCODE, "");
                } else {
                    ToastUtil.show("数据为空，请退出在登录！");
                }
            }

            @Override
            public void onPayError(int error_code, String message) {
                ToastUtil.show(message);
            }

            @Override
            public void onPayCancel() {
                ToastUtil.show("取消登录！");
            }

            @Override
            public void onUUPay(String dataOrg, String sign, String mode) {
            }
        });
    }

    private void getAccessToken(String code) {
        showLoading();
        Map<String, String> params = new HashMap();
        params.put("appid", Constant.WECHATAPPKEY);
        params.put("secret", Constant.WECHATAPPSECRET);
        params.put("code", code);
        params.put("grant_type", "authorization_code");
        ViseHttp.GET("sns/oauth2/access_token")
                .baseUrl("https://api.weixin.qq.com/")
                .addParams(params)
                .request(new ACallback<WXAccessTokenBean>() {
                    @Override
                    public void onSuccess(WXAccessTokenBean data) {
                        if (TextUtils.isEmpty(data.getAccess_token()) || TextUtils.isEmpty(data.getOpenid())) {
                            hideLoading();
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
                        params.put("country", "");
                        params.put("province", "");
                        params.put("city", "");
                        params.put("openId", data.getUnionid());

                        JSONObject jsonObject = new JSONObject(params);
                        ViseHttp.POST(ApiConstant.LOGIN)
                                .setJson(jsonObject)
                                .request(new ACallback<BaseTResp2<UserBean>>() {
                                    @Override
                                    public void onSuccess(BaseTResp2<UserBean> data) {
                                        UserBean bean = data.data;
                                        if (null != bean) {
                                            saveUser(bean);
                                        }

                                        if (data.isSuccess() || data.getCode() == 40102002) {
                                            loginEaseMob(String.valueOf(bean.getId()), "123456", data.getCode());
                                        } else {
                                            Log.i(TAG, "onSuccess: " + data.getMsg());
                                            ToastUtil.show(data.getMsg());
                                        }
                                    }

                                    @Override
                                    public void onFail(int errCode, String errMsg) {
                                        Log.i(TAG, "onFail: " + errMsg + errCode);
                                        hideLoading();
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

    /*登录环信*/
    private void loginEaseMob(String id, String password, int code) {
        Observable.create(subscriber -> EMClient.getInstance().login(id, password, new EMCallBack() {
            @Override
            public void onSuccess() {
                subscriber.onNext(200);
                subscriber.onComplete();
            }

            @Override
            public void onError(int i, String s) {
                subscriber.onNext(String.format("%d//%s", i, s));
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
                hideLoading();
                if (obj instanceof Integer) {
                    Log.i(TAG, "onSuccess: 环信登录成功");
                    /*设置环信的信息*/
                    DemoHelper.getInstance().getUserProfileManager().updateCurrentUserNickName(SPHelper.getStringSF(mContext, Constant.USERNAME, ""));
//                    DemoHelper.getInstance().getUserProfileManager().setCurrentUserAvatar(SPHelper.getStringSF(mContext, Constant.USERHEAD, ""));
                    DemoHelper.getInstance().setCurrentUserName(SPHelper.getStringSF(mContext, Constant.USERID)); // 环信Id

                    DemoHelper.getInstance().getUserProfileManager().asyncGetCurrentUserInfo();
                    EMClient.getInstance().chatManager().loadAllConversations();
                    EMClient.getInstance().groupManager().loadAllGroups();

                    EMClient.getInstance().pushManager().updatePushNickname(
                            SPHelper.getStringSF(mContext, Constant.USERNAME, ""));

                    SPHelper.setBooleanSF(mContext, Constant.ISLOGIN, true);
                    if (code == 40102002) {
                        FastUtil.startActivity(mContext, SendCodeActivity.class);
                    } else if (code == 200) {
                        SPHelper.setBooleanSF(mContext, Constant.ISVERIFPHONE, true);
                        FastUtil.startActivity(mContext, MainActivity.class);
                    }
                    finish();
                } else {
                    Log.i(TAG, "onNext: " + id);
                    Log.i(TAG, "环信登录失败:" + obj.toString());
                    String string = obj.toString();
                    if (!TextUtils.isEmpty(string) && string.contains("//")) {
                        try {
                            String[] strings = string.split("//");
                            Log.i(TAG, "onNext: " + " " + strings[0] + " " + strings[1]);
                            int code = Integer.valueOf(strings[0]).intValue();
                            if (Integer.valueOf(code) == Emerror.USER_ALREADY_LOGIN.getCode()) {
                                EMClient.getInstance().logout(true);
                                loginEaseMob(id, password, code);
                            } else if (code == EMError.USER_NOT_FOUND) {
                                /*用户不存在*/
                                ToastUtil.show("不存在此用户");
//                                EMClient.getInstance().createAccount(id, "123456");//同步方法
//                                loginEaseMob(id, "123456", code);
                            } else {
                                ToastUtil.show(strings[1]);
                            }
                        } catch (Exception e) {
                            ToastUtil.show(e.toString());
                        }
                    } else {
                        hideLoading();
                        ToastUtil.show(string);
                    }
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

    enum Emerror {
        USER_ALREADY_LOGIN(200, "用户已登录");

        private final int code;
        private final String describe;

        Emerror(int code, String describe) {
            this.code = code;
            this.describe = describe;
        }

        public int getCode() {
            return code;
        }
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

            UserUtils.saveUser(data);

//            SPHelper.setStringSF(mContext, Constant.USERNAME, data.getName());
//            SPHelper.setStringSF(mContext, Constant.USERCOMPANYNAME, data.getCompanyName());
//            SPHelper.setStringSF(mContext, Constant.USERHEAD, data.getHeadImgUrl());
//            SPHelper.setStringSF(mContext, Constant.USERID, String.valueOf(data.getId()));
//            SPHelper.setStringSF(mContext, Constant.USERPHONE, data.getTelephone());

            DemoHelper.getInstance().setCurrentUserName(data.getName());
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_login;
    }
}