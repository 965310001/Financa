package com.ph.financa.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.WebViewClient;
import com.ph.financa.R;
import com.ph.financa.activity.bean.AndroidObject;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.constant.Constant;
import com.ph.financa.utils.AndroidBug5497Workaround;
import com.ph.financa.wxapi.pay.JPayListener;
import com.ph.financa.wxapi.pay.WeiXinBaoStrategy;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.share.WbShareHandler;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.permission.Permission;
import tech.com.commoncore.permission.UsesPermission;
import tech.com.commoncore.utils.DisplayUtil;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.Utils;

public class WebActivity extends BaseTitleActivity {

    private String mUrl;
    private AgentWeb mAgentWeb;

    private int REQUEST_CODE = 1234;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadCallbackBelow;
    Uri imageUri;

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        AndroidBug5497Workaround.assistActivity(this);
        Intent intent = getIntent();
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));
        if (intent.hasExtra(Constant.TITLE)) {
            mTitleBar.setTitleMainText(intent.getStringExtra(Constant.TITLE));
        } else {
            mTitleBar.setVisibility(View.GONE);
            mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
        }

        if (intent.hasExtra(Constant.URL)) {
            mUrl = intent.getStringExtra(Constant.URL);
        }

        Log.i(TAG, "initView: " + mUrl);

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(findViewById(R.id.fl), new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(mWebChromeClient)
                .setWebViewClient(mWebViewClient)
                .createAgentWeb()
                .ready()
                .go(mUrl);

        mAgentWeb.getJsInterfaceHolder().addJavaObject("cosmetics", new AndroidInterface(mAgentWeb, mContext));

        /*我的名片*/
//        if (mUrl.contains(ApiConstant.MY_CARD)) {

//        mAgentWeb.getWebCreator().getWebView().setWebViewClient(mWebViewClient);
////        mAgentWeb.getWebCreator().getWebView().setWebViewClient(new WebViewClient() {
////            @Override
////            public boolean shouldOverrideUrlLoading(WebView view, String url) {
////                /*Log.i(TAG, "shouldOverrideUrlLoading: " + url);*/
////                if (url.contains("tel:")) {
////                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
////                    startActivity(intent);
////                } else {
////                    view.loadUrl(url); // 在本WebView打开新的url请求
////                }
////                return true;  // 标记新请求已经被处理笑话
////                // 上边2行合起来，标识所有新链接都在本页面处理，不跳转别的浏览器
////            }
////
////            @Override
////            public void onPageStarted(WebView view, String url, Bitmap favicon) {
////                super.onPageStarted(view, url, favicon);
////            }
////
//////            @Override
//////            public void onPageFinished(WebView view, String url) {
//////                super.onPageFinished(view, url);
//////            }
////        });
//
//        mAgentWeb.getWebCreator().getWebView().setWebChromeClient(mWebChromeClient);
//        }
    }

    private com.just.agentweb.WebChromeClient mWebChromeClient = new com.just.agentweb.WebChromeClient() {

        /**
         //         * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
         */
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Log.e("WangJ", "运行方法 openFileChooser-1");
            // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null

            requestPermission();
            mUploadCallbackBelow = uploadMsg;
            Log.i(TAG, "openFileChooser:指定拍照存储位置的方式调起相机 ");
//                    takePhoto();

        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);

        }

        /**
         * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
         */
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//            Log.e("WangJ", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
//            // 这里我们就不区分input的参数了，直接用拍照
//            openFileChooser(uploadMsg);
//        }
        @Override
        public void openFileChooser(ValueCallback uploadMsg, String acceptType) {
            openFileChooser(uploadMsg);
        }

        /**
         * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
         */
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            Log.e("WangJ", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
            // 这里我们就不区分input的参数了，直接用拍照
            openFileChooser(uploadMsg);
        }

        /**
         * API >= 21(Android 5.0.1)回调此方法
         */
        @Override
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            Log.e("WangJ", "运行方法 onShowFileChooser");
            // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
//                    takePhoto();
            requestPermission();
            mUploadCallbackAboveL = filePathCallback;

            Log.i(TAG, "onShowFileChooser:指定拍照存储位置的方式调起相机 ");
            return true;
        }
    };

//    WebChromeClient mWebChromeClient = new WebChromeClient() {
//        /**
//         * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
//         */
//        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//            Log.e("WangJ", "运行方法 openFileChooser-1");
//            // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
//
//            requestPermission();
//            mUploadCallbackBelow = uploadMsg;
//            Log.i(TAG, "openFileChooser:指定拍照存储位置的方式调起相机 ");
////                    takePhoto();
//
//        }
//
//        /**
//         * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
//         */
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//            Log.e("WangJ", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
//            // 这里我们就不区分input的参数了，直接用拍照
//            openFileChooser(uploadMsg);
//        }
//
//        /**
//         * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
//         */
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//            Log.e("WangJ", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
//            // 这里我们就不区分input的参数了，直接用拍照
//            openFileChooser(uploadMsg);
//        }
//
//        /**
//         * API >= 21(Android 5.0.1)回调此方法
//         */
//        @Override
//        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
//            Log.e("WangJ", "运行方法 onShowFileChooser");
//            // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
////                    takePhoto();
//            requestPermission();
//            mUploadCallbackAboveL = filePathCallback;
//
//            Log.i(TAG, "onShowFileChooser:指定拍照存储位置的方式调起相机 ");
//            return true;
//        }
//
//          /*  @Override
//            public void onProgressChanged(WebView view, int newProgress) {
//                Log.i(TAG, "onProgressChanged: " + newProgress);
//                super.onProgressChanged(view, newProgress);
//            }*/
//    };

    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
//            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            } else {
                view.loadUrl(url); // 在本WebView打开新的url请求
            }
            return true;  // 标记新请求已经被处理笑话
            // 上边2行合起来，标识所有新链接都在本页面处理，不跳转别的浏览器
        }
    };


    /**
     * 请求权限
     */
    private void requestPermission() {
        new UsesPermission(this, Permission.CAMERA, Permission.WRITE_EXTERNAL_STORAGE) {
            @Override
            protected void onTrue(@NonNull ArrayList<String> lowerPermissions) {
                //获取了全部权限执后行此函数，
                Log.i(TAG, "onTrue: ");
                takePhoto();
            }

            @Override
            protected void onFalse(@NonNull ArrayList<String> rejectFinalPermissions, @NonNull ArrayList<String> rejectPermissions, @NonNull ArrayList<String> invalidPermissions) {
                //未全部授权时执行此函数
                Log.i(TAG, "onFalse: ");
                cancelFilePathCallback();
            }

            //要么实现上面两个方法即可，onTrue或onFalse只会有一个会被调用一次
            //要么仅仅实现下面这个方法，不管授权了几个权限都会调用一次

            @Override
            protected void onComplete(@NonNull ArrayList<String> resolvePermissions, @NonNull ArrayList<String> lowerPermissions, @NonNull ArrayList<String> rejectFinalPermissions, @NonNull ArrayList<String> rejectPermissions, @NonNull ArrayList<String> invalidPermissions) {
                //完成回调，可能全部已授权、全部未授权、或者部分已授权
                //通过resolvePermissions.contains(Permission.XXX)来判断权限是否已授权
                Log.i(TAG, "onComplete: ");
//                cancelFilePathCallback();
            }
        };

//        PermissionManager.instance().request(this, new OnPermissionCallback() {
//            @Override
//            public void onRequestAllow(String permissionName) {
//                takePhoto();
//            }
//
//            @Override
//            public void onRequestRefuse(String permissionName) {
//                Log.i(TAG, "onRequestRefuse: " + permissionName);
//                cancelFilePathCallback();
//            }
//
//            @Override
//            public void onRequestNoAsk(String permissionName) {
//                Log.i(TAG, "onRequestNoAsk: " + permissionName);
//                cancelFilePathCallback();
//            }
//        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    /**
     * 取消mFilePathCallback回调
     */
    private void cancelFilePathCallback() {
        if (mUploadCallbackBelow != null) {
            mUploadCallbackBelow.onReceiveValue(null);
            mUploadCallbackBelow = null;
        } else if (mUploadCallbackAboveL != null) {
            mUploadCallbackAboveL.onReceiveValue(null);
            mUploadCallbackAboveL = null;
        }
    }

    /**
     * 调用相机
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
        imageUri = Uri.fromFile(new File(filePath + fileName));

//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//        startActivityForResult(intent, REQUEST_CODE);
        // 选择图片（不包括相机拍照）,则不用成功后发刷新图库的广播
//        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
//        i.addCategory(Intent.CATEGORY_OPENABLE);
//        i.setType("image/*");
//        startActivityForResult(Intent.createChooser(i, "Image Chooser"), REQUEST_CODE);

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        Intent Photo = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});

        startActivityForResult(chooserIntent, REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult: " + requestCode);
        if (requestCode == REQUEST_CODE) {
            // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
            if (mUploadCallbackBelow != null) {
                chooseBelow(resultCode, data);
            } else if (mUploadCallbackAboveL != null) {
                chooseAbove(resultCode, data);
            } else {
//                Toast.makeText(this, "发生错误", Toast.LENGTH_SHORT).show();
                ToastUtil.show("发生错误");
            }
        }
    }

    /**
     * Android API < 21(Android 5.0)版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseBelow(int resultCode, Intent data) {
        Log.e("WangJ", "返回调用方法--chooseBelow");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对文件路径处理
                Uri uri = data.getData();
                if (uri != null) {
                    Log.e("WangJ", "系统返回URI：" + uri.toString());
                    mUploadCallbackBelow.onReceiveValue(uri);
                } else {
                    mUploadCallbackBelow.onReceiveValue(null);
                }
            } else {
                // 以指定图像存储路径的方式调起相机，成功后返回data为空
                Log.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackBelow.onReceiveValue(imageUri);
            }
        } else {
            mUploadCallbackBelow.onReceiveValue(null);
        }
        mUploadCallbackBelow = null;
    }

    /**
     * Android API >= 21(Android 5.0) 版本的回调处理
     *
     * @param resultCode 选取文件或拍照的返回码
     * @param data       选取文件或拍照的返回结果
     */
    private void chooseAbove(int resultCode, Intent data) {
        Log.e("WangJ", "返回调用方法--chooseAbove");

        if (RESULT_OK == resultCode) {
            updatePhotos();

            if (data != null) {
                // 这里是针对从文件中选图片的处理
                Uri[] results;
                Uri uriData = data.getData();
                if (uriData != null) {
                    results = new Uri[]{uriData};
                    for (Uri uri : results) {
                        Log.e("WangJ", "系统返回URI：" + uri.toString());
                    }
                    mUploadCallbackAboveL.onReceiveValue(results);
                } else {
                    mUploadCallbackAboveL.onReceiveValue(null);
                }
            } else {
                Log.e("WangJ", "自定义结果：" + imageUri.toString());
                mUploadCallbackAboveL.onReceiveValue(new Uri[]{imageUri});
            }
        } else {
            mUploadCallbackAboveL.onReceiveValue(null);
        }
        mUploadCallbackAboveL = null;
    }

    private void updatePhotos() {
        // 该广播即使多发（即选取照片成功时也发送）也没有关系，只是唤醒系统刷新媒体文件
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        intent.setData(imageUri);
        sendBroadcast(intent);
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
        public void fallBack(String content) {
            Log.i(TAG, "fallBack: ");
            runOnUiThread(() -> {
                if (!agent.back()) {
                    finish();
                }
            });
        }

        @JavascriptInterface
        public void toFileLib(String content) {
            Log.i(TAG, "toFileLib: 前往资料库页面方法");
            Bundle bundle = new Bundle();
            bundle.putString(Constant.URL, String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP,
                    ApiConstant.DATA_LIB,
                    SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""),
                    SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, "")));

//            FastUtil.startActivity(mContext, WebActivity.class, bundle);

            runOnUiThread(() -> mAgentWeb.getWebCreator().getWebView().loadUrl(bundle.getString(Constant.URL)));

        }

        /*名片分享的 方法*/
        @JavascriptInterface
        public void shareMyCard(String content) {
            Log.i(TAG, "shareMyCard: " + content);

            try {
                JSONObject jsonObject = new JSONObject(content);
                String target = jsonObject.getString("target");
                String imgUrl = jsonObject.getString("imgUrl");
                String shareLink = jsonObject.getString("shareLink");

                jsonObject = jsonObject.getJSONObject("sourceData");
                jsonObject = jsonObject.getJSONObject("userInfo");

                mResourceId = jsonObject.getLong("id");

                mShareCode = jsonObject.getString("shareCode");
                mResourceType = "CARD";
                mAuthor = jsonObject.getString("name");
                mTitle = String.format("%s的名片", mAuthor);

                mShareContent = mTitle;
                /*mUrl = shareLink;*/
                mShareUrl = shareLink;

                String description = mTitle;
                if (jsonObject.has("telephone")) {
                    description = String.format("从事多年保险顾问，具有丰富的保险理财经验，能为您提供专业的保险服务！电话：%s", jsonObject.getString("telephone"));
                }
                share(target, shareLink, imgUrl, mTitle, description);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*这是分享产品的*/
        @JavascriptInterface
        public void shareMyProduct(String content) {
            Log.i(TAG, "shareMyProduct: " + content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                String target = jsonObject.getString("target");
                String imgUrl = jsonObject.getString("imgUrl");
                String shareLink = jsonObject.getString("shareLink");

                jsonObject = jsonObject.getJSONObject("sourceData");


                // TODO: 2019/9/26 如果错误修改这里
                JSONObject userInfo = jsonObject.getJSONObject("userInfo");
                mAuthor = userInfo.getString("name");

                String title = " ";//= jsonObject.getString("title");
//                String description = jsonObject.getString("summary");
//                description = description.replace("\n", "");

                String description = "";
                mResourceType = "PRODUCT";

                mShareUrl = shareLink;

                JSONObject productDetail = jsonObject.getJSONObject("productDetail");
                mResourceId = productDetail.getLong("id");
                mShareCode = productDetail.getString("shareCode");

                if (productDetail.has("content")) {
                    JSONArray content1 = productDetail.getJSONArray("content");
                    if (content1.length() > 2) {
                        if (content1.length() > 3) {
                            description = mShareContent = (String) content1.getJSONObject(1).get("value");
                            title = (String) content1.getJSONObject(1).get("value");
                        } else {
                            mShareContent = " ";
                        }
                    } else {
                        mShareContent = " ";
                    }

                } else {
                    mShareContent = " ";
                }


                if (jsonObject.has("positionState")) {
                    mAdPosition = jsonObject.getString("positionState");
                }

                if (jsonObject.has("production")) {
                    mAdContent = jsonObject.getString("production");
                }

                if (TextUtils.isEmpty(description)) {
                    description = title;
                }
                mTitle = title;
                share(target, shareLink, imgUrl, title, description);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        /*前往访客画像*/
        @JavascriptInterface
        public void toBrowsePicture(String content) throws JSONException {
            Log.i(TAG, "toBrowsePicture: " + content);
//            Log.i(TAG, "前往访客画像: " + content);
            if (!TextUtils.isEmpty(content)) {
                JSONObject jsonObject = new JSONObject(content);
                String userId = jsonObject.getString("userId");
                String readerOpenId = jsonObject.getString("readerOpenId");
                Bundle bundle = new Bundle();
                bundle.putString(Constant.URL, String.format("%s%s?userId=%s&readerOpenId=%s", ApiConstant.BASE_URL_ZP,
                        ApiConstant.BROWSE_DETAIL, userId, readerOpenId));
                bundle.putString(Constant.TITLE, "访客画像");
                FastUtil.startActivity(mContext, WebActivity.class, bundle, true);

//                runOnUiThread(() -> {
//                    mTitleBar.setTitleMainText("访客画像");
//                    mAgentWeb.getWebCreator().getWebView().loadUrl(bundle.getString(Constant.URL));
//                });
            } else {
                ToastUtil.show("内容为空");
            }
        }

        /*前往新增客户*/
        @JavascriptInterface
        public void toAddCustomer(String content) throws JSONException {
            Log.i(TAG, "前往新增客户: " + content);
            if (!TextUtils.isEmpty(content)) {
                JSONObject jsonObject = new JSONObject(content);
                String userId = jsonObject.getString("userId");
                String visitOpenId = jsonObject.getString("visitOpenId");
                Bundle bundle = new Bundle();
//                bundle.putString(Constant.URL, String.format("%s%s?userId=%s&visitOpenId=%S", ApiConstant.BASE_URL_ZP,
//                        ApiConstant.BROWSE_DETAIL, userId, visitOpenId));

                bundle.putString(Constant.URL, String.format("%s%s?userId=%s&visitOpenId=%s&openId=%s", ApiConstant.BASE_URL_ZP,
                        ApiConstant.VISIT_ADD, userId, visitOpenId,
                        SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, "")));
                FastUtil.startActivity(mContext, WebActivity.class, bundle);

//                runOnUiThread(() -> {
//                    mContentView.setPadding(0, 0, 0, 0);
//                    mTitleBar.setVisibility(View.GONE);
//                    mAgentWeb.getWebCreator().getWebView().loadUrl(bundle.getString(Constant.URL));
//                });

            } else {
                ToastUtil.show("内容为空");
            }
        }

        @JavascriptInterface
        public void shareArticleData(String content) {
            Log.i(TAG, "shareArticleData: " + content);
            try {
                JSONObject jsonObject = new JSONObject(content);
                String target = jsonObject.getString("target");
                String imgUrl = jsonObject.getString("imgUrl");
                String shareLink = jsonObject.getString("shareLink");
                Log.i(TAG, "shareArticleData: " + shareLink);
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
                mShareUrl = shareLink;

                if (jsonObject.has("positionState")) {
                    mAdPosition = jsonObject.getString("positionState");
                }

                if (jsonObject.has("production")) {
                    mAdContent = jsonObject.getString("production");
                }

                if (TextUtils.isEmpty(description)) {
                    description = title;
                }
                if (description.length() > 700) {
                    description = description.substring(0, 700);
                }
                share(target, shareLink, imgUrl, title, description);
            } catch (JSONException e) {
                Log.i(TAG, "shareArticleData: " + e.toString());
            }
        }
    }

    private WbShareHandler mShareHandler;

    private long mResourceId;
    private String mShareCode, mShareContent, mShareChannel = "OTHER", mResourceType, mAuthor, mTitle, mShareUrl, mAdPosition, mAdContent;

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
        WbSdk.install(mContext, new AuthInfo(mContext, Constant.APP_KEY, Constant.REDIRECT_URL, Constant.SCOPE));
        mShareHandler = new WbShareHandler(mContext);
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

    private void shareSuccess() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.putOpt("resourceId", mResourceId);
            jsonObject.putOpt("shareCode", mShareCode);
            jsonObject.putOpt("shareChannel", mShareChannel);
            jsonObject.putOpt("resourceType", mResourceType);

            JSONObject shareContent = new JSONObject();
            shareContent.putOpt("author", mAuthor);
            shareContent.putOpt("content", mShareContent);
            shareContent.putOpt("title", mTitle);
            shareContent.putOpt("url", mShareUrl);
            jsonObject.putOpt("shareContent", shareContent);
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
    public void onBackPressed() {
        if (!mAgentWeb.back()) {
            finish();
        } else {
//            String url = mAgentWeb.getWebCreator().getWebView().getUrl();
//            if (url.contains(ApiConstant.VISIT_ADD)) {
//                mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
//                mTitleBar.setVisibility(View.VISIBLE);
//                mTitleBar.setTitleMainText("访问画像");
//            } else if (url.contains(ApiConstant.BROWSE_DETAIL)) {
//                mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
//                mTitleBar.setVisibility(View.VISIBLE);
//                mTitleBar.setTitleMainText("浏览详情/轨迹");
//            } else if (url.contains(ApiConstant.VISIT_PICTURE)) {
//                mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
//                mTitleBar.setVisibility(View.VISIBLE);
//            }
//            Log.i(TAG, "onBackPressed: " + url);
        }

//        switch (mNavigationBar.getmViewPager().getCurrentItem()) {
//            case 0:
//                if (!mHomeFragment.back()){
//                }
//                break;
//        }
//        Intent intent = new Intent(Intent.ACTION_MAIN);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.addCategory(Intent.CATEGORY_HOME);
//        startActivity(intent);
    }

    @Override
    protected void onPause() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_web;
    }
}