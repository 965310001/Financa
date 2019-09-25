package com.ph.financa.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.ph.financa.R;
import com.ph.financa.activity.WebActivity;
import com.ph.financa.activity.bean.AndroidObject;
import com.ph.financa.constant.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.Utils;

/**
 * 转发
 */

public class ForwardingFragment extends BaseFragment {

    private String URL = String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP, ApiConstant.FORWARD, SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""), SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));
    private AgentWeb mAgentWeb;

   /* @Override
    public void onPause() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }
*/
  /*  @Override
    public void onResume() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }*/

    @Override
    public int getContentLayout() {
        return R.layout.fragment_forwarding;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Log.i(TAG, "initView: " + URL);
        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent((FrameLayout) mContentView.findViewById(R.id.fl), new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(URL);
        mAgentWeb.getJsInterfaceHolder().addJavaObject("cosmetics", new AndroidInterface(mAgentWeb, getContext()));
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
        public void toForwardDetail(String content) {
            if (!TextUtils.isEmpty(content)) {
                Log.i(TAG, "前往转发详情页面: " + content);
                try {
                    JSONObject jsonObject = new JSONObject(content);
                    String userId = jsonObject.getString("userId");
                    String resourceId = jsonObject.getString("resourceId");
                    Bundle bundle = new Bundle();
                    bundle.putString(Constant.TITLE, "分享");
                    String url = String.format("%s%s?userId=%s&resourceId=%s", ApiConstant.BASE_URL_ZP, ApiConstant.FORWARD_DETAIL, userId, resourceId);
                    bundle.putString(Constant.URL, url);
                    FastUtil.startActivity(mContext, WebActivity.class, bundle);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                ToastUtil.show(content);
            }
        }
    }


    @Override
    public void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }
}
