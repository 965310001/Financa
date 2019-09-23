package com.ph.financa.fragments;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.IAgentWebSettings;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.ContactColumnBean;
import com.ph.financa.constant.Constant;
import com.ph.financa.utils.WebCameraHelper;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;
import com.vise.xsnow.permission.OnPermissionCallback;
import com.vise.xsnow.permission.PermissionManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.com.commoncore.base.BaseTitleFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.DisplayUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.Utils;

/**
 * 客户
 */
public class CustomerFragment extends BaseTitleFragment {

    private static final String URL = String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP,
            ApiConstant.CUSTOMER,
            SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""),
            SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));

    private AgentWeb mAgentWeb;

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        if (null != mContentView) {
            mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
            StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));
        }
        super.onVisibleChanged(isVisibleToUser);
        Log.i(TAG, "onVisibleChanged: ");
    }

    @Override
    public void onDestroy() {
        if (null != mAgentWeb) {
            mAgentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(URL)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                StrictMode.setVmPolicy(builder.build());
            }

            Log.i(TAG, "initView: " + URL);
            mAgentWeb = AgentWeb.with(this)
                    .setAgentWebParent(mContentView.findViewById(R.id.fl_content), new FrameLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()
                    .setAgentWebWebSettings(new AbsAgentWebSettings() {
                        @Override
                        protected void bindAgentWebSupport(AgentWeb agentWeb) {
                        }

                        @Override
                        public IAgentWebSettings toSetting(WebView webView) {
                            IAgentWebSettings settings = super.toSetting(webView);
                            webView.getSettings().setAllowContentAccess(true); // 是否可访问Content Provider的资源，默认值 true
                            webView.getSettings().setAllowFileAccess(true);    // 是否可访问本地文件，默认值 true
                            webView.getSettings().setAllowFileAccessFromFileURLs(false);
                            webView.getSettings().setJavaScriptEnabled(true);
                            webView.getSettings().setLoadWithOverviewMode(true);
                            webView.setWebChromeClient(new MyWebChromeClient());
//                            webView.setWebChromeClient(new WebChromeClient() {
////                                @Override
////                                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
////                                    // TODO 自动生成的方法存根
//////                                    valueCallbacks = filePathCallback;
//////                                    int dd = fileChooserParams.getMode();
//////                                    if (dd == 0) {
//////                                        WebViewAcitviy.this.startActivityForResult(createDefaultOpenableIntent(),
//////                                                WebViewAcitviy.this.FILECHOOSER_RESULTCODE);
//////
//////                                        return true;
//////                                    } else {
//////                                        WebViewAcitviy.this.startActivityForResult(createCameraIntent(),
//////                                                WebViewAcitviy.this.FILECHOOSER_RESULTCODE);
//////                                    }
////                                    Log.i(TAG, "onShowFileChooser: ");
////                                    return true;
////                                }
////@Override
////public void onProgressChanged(WebView view, int newProgress) {
////    if (newProgress == 100) {
////        progressBar.setVisibility(View.GONE);//加载完网页进度条消失
////    } else {
////        progressBar.setProgress(newProgress);//设置进度值
////        progressBar.setVisibility(View.VISIBLE);//开始加载网页时显示进度条
////    }
////}
//
//                                /**
//                                 * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
//                                 */
//                                private void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg) {
//                                    Log.e("WangJ", "运行方法 openFileChooser-1");
//                                    // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
////                                    mUploadCallbackBelow = uploadMsg;
////                                    takePhoto();
//
//                                }
//
//                                /**
//                                 * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
//                                 */
//                                public void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg, String acceptType) {
//                                    Log.e("WangJ", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
//                                    // 这里我们就不区分input的参数了，直接用拍照
//                                    openFileChooser(uploadMsg);
//                                }
//
//                                /**
//                                 * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
//                                 */
//                                public void openFileChooser(android.webkit.ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//                                    Log.e("WangJ", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
//                                    // 这里我们就不区分input的参数了，直接用拍照
//                                    openFileChooser(uploadMsg);
//                                }
//
//                                /**
//                                 * API >= 21(Android 5.0.1)回调此方法
//                                 */
//                                @Override
//                                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
//                                    Log.e("WangJ", "运行方法 onShowFileChooser");
//                                    // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
////                                    mUploadCallbackAboveL = valueCallback;
////                                    takePhoto();
//                                    return true;
//                                }
//                            });
                            return settings;
                        }
                    })
                    .createAgentWeb()
                    .ready()
                    .go(URL);

            mAgentWeb.getJsInterfaceHolder().addJavaObject("cosmetics", new AndroidInterface(mAgentWeb, getContext()));
        }
    }

    class MyWebChromeClient extends WebChromeClient {

        // For Android < 3.0
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            WebCameraHelper.getInstance().mUploadMessage = uploadMsg;
            WebCameraHelper.getInstance().showOptions(getActivity());
            Log.i(TAG, "openFileChooser: ");
        }

        // For Android > 4.1.1
        public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                    String acceptType, String capture) {
            WebCameraHelper.getInstance().mUploadMessage = uploadMsg;
            WebCameraHelper.getInstance().showOptions(getActivity());
            Log.i(TAG, "openFileChooser: ");
        }

        // For Android > 5.0支持多张上传
        @Override
        public boolean onShowFileChooser(WebView webView,
                                         ValueCallback<Uri[]> uploadMsg,
                                         FileChooserParams fileChooserParams) {
            WebCameraHelper.getInstance().mUploadCallbackAboveL = uploadMsg;
            WebCameraHelper.getInstance().showOptions(getActivity());
            Log.i(TAG, "onShowFileChooser: ");
            return true;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//  WebView      mShopWebView=mAgentWeb.getWebCreator().getWebView();
//        mUploadCallbackAboveL = mShopWebView.getmWebChromeClient().getmUploadCallbackAboveL();
//        mUploadCallbackBelow = mShopWebView.getmWebChromeClient().getmUploadCallbackBelow();
//        imageUri = mShopWebView.getmWebChromeClient().getImageUri();
//        if (requestCode == Constants.CONSTANT_TEN) {
//            // 经过上边(1)、(2)两个赋值操作，此处即可根据其值是否为空来决定采用哪种处理方法
//            if (mUploadCallbackBelow != null) {
//                chooseBelow(resultCode, data);
//            } else if (mUploadCallbackAboveL != null) {
//                chooseAbove(resultCode, data);
//            } else {
//                Toast.makeText(this, getString(R.string.upload_error), Toast.LENGTH_SHORT).show();
//            }
//        }
//
//
//    }

    //    class SuWebChromeClient extends WebChromeClient {
//        private ValueCallback<Uri> mUploadCallbackBelow;
//        private Uri imageUri;
//        private Activity activity;
//        public void setActivity(Activity activity) {
//            this.activity = activity;
//        }
//        public ValueCallback<Uri[]> getmUploadCallbackAboveL() {
//            return mUploadCallbackAboveL;
//        }
//
//        public ValueCallback<Uri> getmUploadCallbackBelow() {
//            return mUploadCallbackBelow;
//        }
//
//        public Uri getImageUri() {
//            return imageUri;
//        }
//
//        /**
//         * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
//         */
//        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
//            LogUtils.e("SuningWebChromeClient", "运行方法 openFileChooser-1");
//            // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
//            mUploadCallbackBelow = uploadMsg;
//            takePhoto();
//        }
//
//        /**
//         * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
//         */
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
//            LogUtils.e("SuningWebChromeClient", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
//            // 这里我们就不区分input的参数了，直接用拍照
//            openFileChooser(uploadMsg);
//        }
//
//        /**
//         * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
//         */
//        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
//            LogUtils.e("SuningWebChromeClient", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
//            // 这里我们就不区分input的参数了，直接用拍照
//            openFileChooser(uploadMsg);
//        }
//
//        /**
//         * API >= 21(Android 5.0.1)回调此方法
//         */
//        @Override
//        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
//            LogUtils.e("SuningWebChromeClient", "运行方法 onShowFileChooser filePathCallback" + filePathCallback.toString());
//            // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
//            mUploadCallbackAboveL = filePathCallback;
//            takePhoto();
//            return true;
//        }
//
//        /**
//         * 调用相机 图库
//         */
//        private void takePhoto() {
//            // 指定拍照存储位置的方式调起相机
//            String filePath = Environment.getExternalStorageDirectory() + File.separator
//                    + Environment.DIRECTORY_PICTURES + File.separator;
//            String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";
//
//            File file = new File(filePath);
//            //判断文件夹是否存在,如果不存在则创建文件夹
//            if (!file.exists()) {
//                file.mkdir();
//            }
//
//            Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
////        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//
//            if (Build.VERSION.SDK_INT < 24) {
//                // 从文件中创建uri
//                imageUri = Uri.fromFile(new File(filePath + fileName));
//                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            } else {
//                //兼容android7.0 使用共享文件的形式
//                ContentValues contentValues = new ContentValues(1);
//                contentValues.put(MediaStore.Images.Media.DATA, new File(filePath + fileName).getAbsolutePath());
//                imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
//                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
//            }
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                //添加这一句表示对目标应用临时授权该Uri所代表的文件
//                captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            }
////        选择图片
//            Intent Photo = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//
//            Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
//            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
//            activity.startActivityForResult(chooserIntent, Constants.CONSTANT_TEN);
//
//        }
//    }

    class AndroidInterface extends Object {

        private AgentWeb agent;
        private Context context;

        public AndroidInterface(AgentWeb agent, Context context) {
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
                                /*loadData();*/
                                /*mAgentWeb.back();*/
                                mAgentWeb.getWebCreator().getWebView().loadUrl(URL);
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
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_customer;
    }
}
