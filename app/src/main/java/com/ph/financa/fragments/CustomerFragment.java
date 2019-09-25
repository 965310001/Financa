package com.ph.financa.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.githang.statusbar.StatusBarCompat;
import com.just.agentweb.AgentWeb;
import com.ph.financa.R;
import com.ph.financa.activity.bean.AndroidObject;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.ContactColumnBean;
import com.ph.financa.constant.Constant;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.permission.OnPermissionCallback;
import com.vise.xsnow.permission.PermissionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.DisplayUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.Utils;

import static android.app.Activity.RESULT_OK;

/**
 * 客户
 */
public class CustomerFragment extends BaseFragment {

    private static final String URL = String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP,
            ApiConstant.CUSTOMER,
            SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""),
            SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));

    private AgentWeb mAgentWeb;
    private final int REQUEST_CODE = 1234;
    private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadCallbackBelow;
    private Uri imageUri;

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        if (null != mContentView) {
            mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
            StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));
        }
        super.onVisibleChanged(isVisibleToUser);
    }


    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initView(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(URL)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
                builder.detectFileUriExposure();
            }

            mAgentWeb = AgentWeb.with(mContext)
                    .setAgentWebParent(mContentView.findViewById(R.id.fl_content), new FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .createAgentWeb()
                    .ready()
                    .go(URL);
            mAgentWeb.getJsInterfaceHolder().addJavaObject("cosmetics", new AndroidInterface(mAgentWeb, getContext()));
            WebSettings settings = mAgentWeb.getWebCreator().getWebView().getSettings();
            settings.setUseWideViewPort(true);
            settings.setLoadWithOverviewMode(true);
            settings.setDomStorageEnabled(true);
            settings.setDefaultTextEncodingName("UTF-8");
            settings.setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
            settings.setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
            // 是否允许通过file url加载的Javascript读取本地文件，默认值 false
            settings.setAllowFileAccessFromFileURLs(false);
            // 是否允许通过file url加载的Javascript读取全部资源(包括文件,http,https)，默认值 false
            settings.setAllowUniversalAccessFromFileURLs(false);
            //开启JavaScript支持
            settings.setJavaScriptEnabled(true);
            // 支持缩放
            settings.setSupportZoom(true);


            mAgentWeb.getWebCreator().getWebView().setWebViewClient(client);
            mAgentWeb.getWebCreator().getWebView().setWebChromeClient(chromeClient);

            Log.i(TAG, "initView: " + URL);
        }
    }

    final WebViewClient client = new WebViewClient() {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                            view.loadUrl(url); // 在本WebView打开新的url请求
            return true;  // 标记新请求已经被处理笑话
            // 上边2行合起来，标识所有新链接都在本页面处理，不跳转别的浏览器
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    };

    final WebChromeClient chromeClient = new WebChromeClient() {

        /**
         * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
         */
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            Log.e("WangJ", "运行方法 openFileChooser-1");
            // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
            mUploadCallbackBelow = uploadMsg;
            Log.i(TAG, "openFileChooser:指定拍照存储位置的方式调起相机 ");
//            takePhoto();
            requestPermission();
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
            requestPermission();
            Log.i(TAG, "onShowFileChooser:指定拍照存储位置的方式调起相机 ");
            return true;
        }
    };

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
//                ToastUtil.show("发生错误");
            }
        }
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
        getActivity().sendBroadcast(intent);
    }

    /**
     * 请求权限
     */
    private void requestPermission() {
        PermissionManager.instance().request(getActivity(), new OnPermissionCallback() {
            @Override
            public void onRequestAllow(String permissionName) {
//                takePhoto();
            }

            @Override
            public void onRequestRefuse(String permissionName) {
                Log.i(TAG, "onRequestRefuse: " + permissionName);
                cancelFilePathCallback();
            }

            @Override
            public void onRequestNoAsk(String permissionName) {
                Log.i(TAG, "onRequestNoAsk: " + permissionName);
                cancelFilePathCallback();
            }
        }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        takePhoto();
    }


    /**
     * 调用相机
     */
    private void takePhoto() {
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


    class AndroidInterface extends AndroidObject {

        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
            super(context);
            this.agent = agent;
            this.context = context;
        }

        @JavascriptInterface
        public void importAddressBook(String content) {
            Log.i(TAG, "importAddressBook: " + content);
            PermissionManager.instance().request(getActivity(), new OnPermissionCallback() {
                @Override
                public void onRequestAllow(String permissionName) {
                    insertContact(getPhoneContacts());
                }

                @Override
                public void onRequestRefuse(String permissionName) {
                    Log.i(TAG, "onRequestRefuse: " + permissionName);
                }

                @Override
                public void onRequestNoAsk(String permissionName) {
                    Log.i(TAG, "onRequestNoAsk: " + permissionName);
                }
            }, Manifest.permission.READ_CONTACTS);

        }
    }

    /*从通讯录导入*/
    private List<ContactColumnBean> getPhoneContacts() {
        ContentResolver resolver = getActivity().getContentResolver();
        Cursor cursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            List<ContactColumnBean> beanList = new ArrayList<>();
            while (cursor.moveToNext()) {
                String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID)); // id
                String name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)); // 姓名
                String number = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)); // 电话
                beanList.add(new ContactColumnBean(id, name, number));
                Log.i(TAG, "getPhoneContacts: " + id + " " + name + " " + number);
            }
            cursor.close();
            return beanList;
        }
        return null;
    }

    /*从通讯录导入*/
    private void insertContact(List<ContactColumnBean> beans) {
        if (null != beans && beans.size() > 0) {
            JSONArray jsonArray = new JSONArray();
            for (ContactColumnBean bean : beans) {
                Map<String, Object> map = new HashMap<>();
                map.put("customerName", bean.getName());
                map.put("telephone", Arrays.asList(bean.getNumber()));
                jsonArray.put(new JSONObject(map));
            }
            ViseHttp.POST(ApiConstant.INSERT_CONTACT)
                    .setJson(jsonArray)
                    .request(new ACallback<BaseTResp2>() {
                        @Override
                        public void onSuccess(BaseTResp2 data) {
                            if (data.isSuccess()) {
                                ToastUtil.show("上传成功！");
                                mAgentWeb.getWebCreator().getWebView().reload();
                            } else {
                                ToastUtil.show(data.getMsg());
                            }
                        }

                        @Override
                        public void onFail(int errCode, String errMsg) {
                            ToastUtil.show(errMsg);
                        }
                    });
        } else {
            ToastUtil.show("通讯录为空");
        }
    }

    @Override
    public void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

//    @Override
//    public void setTitleBar(TitleBarView titleBar) {
//    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_customer;
    }
}
