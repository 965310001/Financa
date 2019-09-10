package com.ph.financa.fragments;

import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.just.agentweb.AgentWeb;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.Utils;

/**
 * 访客
 */
public class VisitorsFragment extends BaseFragment {

    private String URL = String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP, ApiConstant.VISIT,
            SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""), SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));
    private AgentWeb mAgentWeb;

    /*  @Override
      public void onPause() {
          if (null != mAgentWeb) {
              mAgentWeb.getWebLifeCycle().onPause();
          }
          super.onPause();
      }

      @Override
      public void onResume() {
          if (null != mAgentWeb) {
              mAgentWeb.getWebLifeCycle().onResume();
          }
          super.onResume();
      }
  */
    @Override
    public int getContentLayout() {
        return R.layout.fragment_visitors;
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
    }

}