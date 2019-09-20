package com.ph.financa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.ph.financa.R;
import com.ph.financa.activity.LoginActivity;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.constant.Constant;
import com.ph.financa.wxapi.pay.JPayListener;
import com.ph.financa.wxapi.pay.WeiXinBaoStrategy;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.DisplayUtil;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.Utils;

/**
 * 首页
 */
public class HomeFragment extends BaseFragment implements WbShareCallback {

    private String URL = String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP, ApiConstant.H5,
            SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""),
            SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));

    private AgentWeb mAgentWeb;

    private WbShareHandler mShareHandler;

    private long mResourceId;
    private String mShareCode, mShareContent, mShareChannel = "OTHER", mResourceType, mAuthor, mTitle, mUrl, mAdPosition, mAdContent;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_home;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Log.i(TAG, "initView: " + URL);

        FastUtil.startActivity(mContext, LoginActivity.class);

//        StatusBarUtils.setPaddingSmart(mContext, mContentView);
//        mContentView.setPadding(0, 0, 0, 0);
//        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.bg_loading));

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mContentView.findViewById(R.id.fl), new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(URL);

        /*监听分享*/
        mAgentWeb.getJsInterfaceHolder().addJavaObject("cosmetics", new AndroidInterface(mAgentWeb, getContext()));
    }

    @Override
    public void onWbShareSuccess() {
        shareSuccess();
    }

    @Override
    public void onWbShareCancel() {
        ToastUtil.show("分享取消");
    }

    @Override
    public void onWbShareFail() {
        ToastUtil.show("分享失败");
    }

    class AndroidInterface extends Object {

        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            this.agent = agent;
            this.context = context;
        }

        @JavascriptInterface
        public void shareArticleData(String content) {
            Log.i(TAG, "shareArticleData: " + content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                String target = jsonObject.getString("target");
                String imgUrl = jsonObject.getString("imgUrl");
                String shareLink = jsonObject.getString("shareLink");

                jsonObject = jsonObject.getJSONObject("sourceData");
                String title = jsonObject.getString("title");
                String description = jsonObject.getString("summary");
                description = description.replace("\n", "");

                mResourceId = jsonObject.getLong("id");

                mShareCode = jsonObject.getString("shareCode");
                mShareContent = jsonObject.getString("content");
                mResourceType = "ARTICLE";
                mAuthor = jsonObject.getString("author");
                mTitle = title;
                mUrl = shareLink;

                if (jsonObject.has("positionState")) {
                    mAdPosition = jsonObject.getString("positionState");
                }

                if (jsonObject.has("production")) {
                    mAdContent = jsonObject.getString("production");
                }

                share(target, shareLink, imgUrl, title, description);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void share(String target, String shareLink, String imgUrl, String title, String description) {
        switch (target) {
            case "shareToFriend":/*微信*/
                mShareChannel = "WECHAT";
                Map<String, String> map = new HashMap<>();
                map.put("url", shareLink);
                map.put("imageurl", imgUrl);
                map.put("title", title);
                map.put("description", description);
                WeiXinBaoStrategy.getInstance(mContext).wechatShare(Constant.WECHATAPPKEY, 0, map, listener);
                break;
            case "shareToCircle":/*朋友圈*/
                mShareChannel = "WECHAT_CIRCLE";
                map = new HashMap<>();
                map.put("url", shareLink);
                map.put("imageurl", imgUrl);
                map.put("title", title);
                map.put("description", description);
                WeiXinBaoStrategy.getInstance(mContext).wechatShare(Constant.WECHATAPPKEY, 1, map, listener);
                break;
            case "shareToSina":/*新浪微博*/
                mShareChannel = "SINA_WEIBO";
                wbShare(shareLink, imgUrl, title, description);
                break;
        }
    }


    private void shareSuccess() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("resourceId", mResourceId);
            jsonObject.putOpt("shareCode", mShareCode);
            jsonObject.putOpt("shareChannel", mShareChannel);
            jsonObject.putOpt("resourceType", mResourceType);

            JSONArray jsonArray = new JSONArray();
            JSONObject articleAd = new JSONObject();
            articleAd.putOpt("adContent", mAdContent);
            articleAd.putOpt("adPosition", mAdPosition);
            jsonArray.put(articleAd);

            JSONObject shareContent = new JSONObject();
            shareContent.putOpt("author", mAuthor);
            shareContent.putOpt("content", mShareContent);
            shareContent.putOpt("title", mTitle);
            shareContent.putOpt("url", mUrl);
            jsonObject.putOpt("shareContent", shareContent);
            shareContent.putOpt("articleAd", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ViseHttp.POST(ApiConstant.SHARE_SUCCESS)
                .setJson(jsonObject)
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        Log.i(TAG, "onSuccess: " + data.getMsg());
                        if (data.isSuccess()) {
                            ToastUtil.show("分享成功");
                        } else {
                            ToastUtil.show(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        Log.i(TAG, "onFail: " + errMsg);
                        ToastUtil.show(errMsg);
                    }
                });
    }

    @Override
    public void onPause() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
//        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));

        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    /*微博分享*/
    private void wbShare(String shareLink, String imgUrl, String title, String description) {
        initWBSDK();

        WeiboMultiMessage weiboMultiMessage = new WeiboMultiMessage();
        TextObject textObject = new TextObject();
        textObject.text = shareLink;
        textObject.title = title;
        textObject.description = description;
        textObject.actionUrl = imgUrl;
        weiboMultiMessage.textObject = textObject;
        mShareHandler.shareMessage(weiboMultiMessage, false);
    }

    private void initWBSDK() {
        WbSdk.install(getActivity(), new AuthInfo(getActivity(), Constant.APP_KEY, Constant.REDIRECT_URL, Constant.SCOPE));
        mShareHandler = new WbShareHandler(getActivity());
        mShareHandler.registerApp();
    }

    private final JPayListener listener = new JPayListener() {
        @Override
        public void onPaySuccess() {
            ToastUtil.show("分享成功！");
            shareSuccess();
            Log.i(TAG, "onPaySuccess: ");
        }

        @Override
        public void onPayError(int error_code, String message) {
            ToastUtil.show(message);
        }

        @Override
        public void onPayCancel() {
            ToastUtil.show("分享取消！");
        }

        @Override
        public void onUUPay(String dataOrg, String sign, String mode) {

        }
    };

    @Override
    public void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    public boolean back() {
        return mAgentWeb.back();
    }
}
