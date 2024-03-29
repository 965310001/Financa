package com.vendor.social.auth;

import android.app.Activity;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.vendor.social.AuthApi;
import com.vendor.social.Social;
import com.vendor.social.model.AuthType;

/**
 * 微博登陆<p />
 * onActivityResult需要调用：getSsoHandler().authorizeCallBack(requestCode, resultCode, data);
 * Created by ljfan on 16/04/19.
 */
public class WeiboAuth extends AuthApi {

    private SsoHandler mSsoHandler;

    public WeiboAuth(Activity act) {
        super(act);
        setAuthType(AuthType.WEIBO);
    }

    /**
     * 分享到新浪微博
     */
    @Override
    public void doOauthVerify() {
        // 创建授权认证信息
        AuthInfo authInfo = new AuthInfo(mActivity,
                Social.getWeiboAppKey(),
                Social.getWeiboRedirectUrl(),
                Social.getWeiboScope());

        mSsoHandler = new SsoHandler(mActivity);

//        mSsoHandler.authorize(new WbAuthListener() {
//            @Override
//            public void onSuccess(Oauth2AccessToken oauth2AccessToken) {
//
//                UsersAPI api = new UsersAPI(mActivity, Social.getWeiboAppKey(), accessToken);
//                long uid = Long.parseLong(accessToken.getUid());
//                api.show(uid, new RequestListener() {
//                    @Override
//                    public void onComplete(String s) {
//                        User user = User.parse(s);
//                        if (user != null) {  //获取成功
//                            setCompleteCallBack(new com.vendor.social.model.User(user.id, user.name, user.avatar_hd));
//                        } else {
//                            setErrorCallBack(s);
//                        }
//
//                    }
//
//                    @Override
//                    public void onWeiboException(WeiboException e) {
//                        ErrorInfo info = ErrorInfo.parse(e.getMessage());
//                        if (info != null) {
//                            setErrorCallBack(info.toString());
//                        } else {
//                            setErrorCallBack("weibo login error");
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void cancel() {
//                setCancelCallBack();
//            }
//
//            @Override
//            public void onFailure(WbConnectErrorMessage e) {
//                ErrorInfo info = new ErrorInfo();
//                info.error = e.getErrorMessage();
//                info.error_code = e.getErrorCode();
//                if (info != null) {
//                    setErrorCallBack(info.toString());
//                } else {
//                    setErrorCallBack("weibo login error");
//                }
//            }
//        });

//        mSsoHandler.authorize(new WeiboAuthListener() {
//
//            @Override
//            public void onWeiboException(WeiboException arg0) {
//
//            }
//
//            @Override
//            public void onComplete(Bundle values) {
//                final Oauth2AccessToken accessToken = Oauth2AccessToken.parseAccessToken(values);
//
//                UsersAPI api = new UsersAPI(mActivity, Social.getWeiboAppKey(), accessToken);
//                long uid = Long.parseLong(accessToken.getUid());
//                api.show(uid, new RequestListener() {
//                    @Override
//                    public void onComplete(String s) {
//                        User user = User.parse(s);
//                        if (user != null) {  //获取成功
//                            setCompleteCallBack(new com.vendor.social.model.User(user.id, user.name, user.avatar_hd));
//                        } else {
//                            setErrorCallBack(s);
//                        }
//
//                    }
//
//                    @Override
//                    public void onWeiboException(WeiboException e) {
//                        ErrorInfo info = ErrorInfo.parse(e.getMessage());
//                        if (info != null) {
//                            setErrorCallBack(info.toString());
//                        } else {
//                            setErrorCallBack("weibo login error");
//                        }
//                    }
//                });
//            }
//
//            @Override
//            public void onCancel() {
//                setCancelCallBack();
//            }
//        });
    }

    public SsoHandler getSsoHandler() {
        return mSsoHandler;
    }
}
