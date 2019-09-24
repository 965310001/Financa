package com.ph.financa.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.just.agentweb.AgentWeb;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.DisplayUtil;
import tech.com.commoncore.utils.ToastUtil;

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
                .createAgentWeb()
                .ready()
                .go(mUrl);

        mAgentWeb.getJsInterfaceHolder().addJavaObject("cosmetics", new AndroidInterface(mAgentWeb, mContext));

        /*我的名片*/
        if (mUrl.contains(ApiConstant.MY_CARD)) {
            mAgentWeb.getWebCreator().getWebView().setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url); // 在本WebView打开新的url请求
                    return true;  // 标记新请求已经被处理笑话
                    // 上边2行合起来，标识所有新链接都在本页面处理，不跳转别的浏览器
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                }
            });
            mAgentWeb.getWebCreator().getWebView().setWebChromeClient(new WebChromeClient() {

                /**
                 * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
                 */
                public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                    Log.e("WangJ", "运行方法 openFileChooser-1");
                    // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
                    mUploadCallbackBelow = uploadMsg;
                    Log.i(TAG, "openFileChooser:指定拍照存储位置的方式调起相机 ");
                    takePhoto();
                }

                /**
                 * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
                 */
                public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                    Log.e("WangJ", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
                    // 这里我们就不区分input的参数了，直接用拍照
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
                    mUploadCallbackAboveL = filePathCallback;
                    takePhoto();
                    Log.i(TAG, "onShowFileChooser:指定拍照存储位置的方式调起相机 ");
                    return true;
                }
            });
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


    class AndroidInterface extends Object {

        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            this.agent = agent;
            this.context = context;
        }

        @JavascriptInterface
        public void fallBack(String content) {
            runOnUiThread(() -> {
                if (!agent.back()) {
                    finish();
                }
            });
        }

        // TODO: 2019/9/21 上传照片

        // TODO: 2019/9/21 上传二维码
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