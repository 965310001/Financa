package com.ph.financa.fragments;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IWebLayout;
import com.ph.financa.R;
import com.ph.financa.view.SmartRefreshWebLayout;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import tech.com.commoncore.base.BaseTitleFragment;

/**
 * web 封装
 */
public class WebFragment extends BaseTitleFragment {

    private static final String URL = "URL";
    private static final String TITLE = "TITLE";

    protected AgentWeb mAgentWeb;

    private String mUrl, mTitle;

    private SmartRefreshWebLayout mSmartRefreshWebLayout;
    private SmartRefreshLayout mSmartRefreshLayout;

    public static WebFragment newInstance(String url) {
        return newInstance(url, "");
    }

    public static WebFragment newInstance(String url, String title) {
        WebFragment fragment = new WebFragment();
        Bundle args = new Bundle();
        args.putString(URL, url);
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            mUrl = getArguments().getString(URL);
            mTitle = getArguments().getString(TITLE);
        }
    }

    public void setUrl(String url) {
        this.mUrl = url;
    }

    protected String getUrl() {
        return mUrl;
    }

    protected String getJavaObjectKey() {
        return "cosmetics";
    }

    protected Object getJavaObjectValue(AgentWeb agentWeb, Context context) {
        return null;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(getUrl())) {

            /*com.tencent.smtt.sdk.WebView mWebView=new com.tencent.smtt.sdk.WebView(mContext);*/

            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(mContentView.findViewById(R.id.fl_content), new FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .setWebChromeClient(mWebChromeClient)
                    .setWebViewClient(mWebViewClient)
                    .setWebLayout(getWebLayout())
//                    .setAgentWebWebSettings(new AbsAgentWebSettings() {
//                        @Override
//                        protected void bindAgentWebSupport(AgentWeb agentWeb) {
//                        }
//
//                        @Override
//                        public IAgentWebSettings toSetting(WebView webView) {
//                            IAgentWebSettings iAgentWebSettings = super.toSetting(webView);
//                            iAgentWebSettings.getWebSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
//                            iAgentWebSettings.getWebSettings().setDomStorageEnabled(true);
//
//                            iAgentWebSettings.getWebSettings().setJavaScriptEnabled(true);
//                            iAgentWebSettings.getWebSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//                            iAgentWebSettings.getWebSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
//                            iAgentWebSettings.getWebSettings().setDomStorageEnabled(true);
//                            iAgentWebSettings.getWebSettings().setDatabaseEnabled(true);
//                            iAgentWebSettings.getWebSettings().setAppCacheEnabled(true);
//                            iAgentWebSettings.getWebSettings().setAllowFileAccess(true);
//                            iAgentWebSettings.getWebSettings().setSavePassword(true);
//                            iAgentWebSettings.getWebSettings().setSupportZoom(true);
//                            iAgentWebSettings.getWebSettings().setBuiltInZoomControls(true);
//                            iAgentWebSettings.getWebSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
//                            iAgentWebSettings.getWebSettings().setUseWideViewPort(true);
//
//                            return iAgentWebSettings;
//                        }
//                    })
                    .createAgentWeb()
                    .ready()
                    .go(getUrl());
            Log.i(TAG, "initView: " + getUrl());
            Object obj = getJavaObjectValue(mAgentWeb, getContext());
            if (null != obj) {
                mAgentWeb.getJsInterfaceHolder().addJavaObject(getJavaObjectKey(), obj);
            }

            mSmartRefreshLayout = (SmartRefreshLayout) mSmartRefreshWebLayout.getLayout();
            mSmartRefreshLayout.setOnRefreshListener(refreshlayout -> {
                mAgentWeb.getUrlLoader().reload();
                /*mSmartRefreshLayout.postDelayed(() -> mSmartRefreshLayout.finishRefresh(), 100);*/
            });
//            mSmartRefreshLayout.autoRefresh();
        }
    }

    private com.just.agentweb.WebChromeClient mWebChromeClient = new com.just.agentweb.WebChromeClient() {


        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            Log.i(TAG, "onProgressChanged: " + newProgress);
            if (newProgress == 100) {
                mSmartRefreshLayout.postDelayed(() -> mSmartRefreshLayout.finishRefresh(), 100);
            }
        }
    };

    private com.just.agentweb.WebViewClient mWebViewClient = new com.just.agentweb.WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
//            return super.shouldOverrideUrlLoading(view, request);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.loadUrl(request.getUrl().toString());
            } else {
                view.loadUrl(request.toString());
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            view.setVisibility(View.VISIBLE);
        }


        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();//接受证书
            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            //do you  work
            Log.i("Info", "BaseWebActivity onPageStarted");
        }
    };


    protected IWebLayout getWebLayout() {
        return this.mSmartRefreshWebLayout = new SmartRefreshWebLayout(this.getActivity());
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        if (!TextUtils.isEmpty(mTitle)) {
            titleBar.setTitleMainText(mTitle);
        } else {
            titleBar.setVisibility(View.GONE);
            /*mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);*/
        }
    }

//    @Override
//    public void onResume() {
//        if (null != mAgentWeb) {
//            mAgentWeb.getWebLifeCycle().onResume();
//        }
//        super.onResume();
//    }

    @Override
    public void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_web;
    }
}