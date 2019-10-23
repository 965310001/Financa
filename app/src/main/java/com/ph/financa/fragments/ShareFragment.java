package com.ph.financa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;

import com.just.agentweb.AgentWeb;
import com.ph.financa.activity.WebActivity;
import com.ph.financa.activity.bean.AndroidObject;
import com.ph.financa.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.Utils;

/**
 * 分享
 */
public class ShareFragment extends WebFragment {

    @Override
    protected String getUrl() {
        return String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP, ApiConstant.SHARE,
                SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""), SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));
    }

    @Override
    protected Object getJavaObjectValue(AgentWeb agentWeb, Context context) {
        return new AndroidInterface(mAgentWeb, getContext());
    }

    class AndroidInterface extends AndroidObject {

        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            super(context);
            this.agent = agent;
            this.context = context;
        }

        @JavascriptInterface
        public void toShareDetail(String content) {
            if (!TextUtils.isEmpty(content)) {
                Log.i(TAG, "前往分享详情页面: " + content);
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String userId = jsonObject.getString("userId");
                    String resourceId = jsonObject.getString("resourceId");
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TITLE, "分享详情");
                    String url = String.format("%s%s?userId=%s&resourceId=%s", ApiConstant.BASE_URL_ZP, ApiConstant.SHARE_DETAIL, userId, resourceId);
                    bundle.putString(Constant.URL, url);
                    FastUtil.startActivity(mContext, WebActivity.class, bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                ToastUtil.show(content);
            }
        }

        /*前往访客画像*/
        @JavascriptInterface
        public void toBrowsePicture(String content) throws JSONException {
            Log.i(TAG, "toBrowsePicture: " + content);
            if (!TextUtils.isEmpty(content)) {
                JSONObject jsonObject = new JSONObject(content);
                String userId = jsonObject.getString("userId");
                String readerOpenId = jsonObject.getString("readerOpenId");
                Bundle bundle = new Bundle();
                bundle.putString(Constant.URL, String.format("%s%s?userId=%s&readerOpenId=%s", ApiConstant.BASE_URL_ZP,
                        ApiConstant.BROWSE_DETAIL, userId, readerOpenId));
                bundle.putString(Constant.TITLE, "访客画像");
                FastUtil.startActivity(mContext, WebActivity.class, bundle, true);
            } else {
                ToastUtil.show("内容为空");
            }
        }
    }

}
