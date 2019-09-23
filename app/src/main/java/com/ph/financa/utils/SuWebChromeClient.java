package com.ph.financa.utils;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.just.agentweb.LogUtils;

import java.io.File;
import java.util.Calendar;
import java.util.Locale;

public class SuWebChromeClient extends WebChromeClient {
private ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ValueCallback<Uri> mUploadCallbackBelow;
    private Uri imageUri;
    private Activity activity;
    public void setActivity(Activity activity) {
        this.activity = activity;
    }
    public ValueCallback<Uri[]> getmUploadCallbackAboveL() {
        return mUploadCallbackAboveL;
    }

    public ValueCallback<Uri> getmUploadCallbackBelow() {
        return mUploadCallbackBelow;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    /**
     * 8(Android 2.2) <= API <= 10(Android 2.3)回调此方法
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg) {
        LogUtils.e("SuningWebChromeClient", "运行方法 openFileChooser-1");
        // (2)该方法回调时说明版本API < 21，此时将结果赋值给 mUploadCallbackBelow，使之 != null
        mUploadCallbackBelow = uploadMsg;
        takePhoto();
    }

    /**
     * 11(Android 3.0) <= API <= 15(Android 4.0.3)回调此方法
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
        LogUtils.e("SuningWebChromeClient", "运行方法 openFileChooser-2 (acceptType: " + acceptType + ")");
        // 这里我们就不区分input的参数了，直接用拍照
        openFileChooser(uploadMsg);
    }

    /**
     * 16(Android 4.1.2) <= API <= 20(Android 4.4W.2)回调此方法
     */
    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        LogUtils.e("SuningWebChromeClient", "运行方法 openFileChooser-3 (acceptType: " + acceptType + "; capture: " + capture + ")");
        // 这里我们就不区分input的参数了，直接用拍照
        openFileChooser(uploadMsg);
    }

    /**
     * API >= 21(Android 5.0.1)回调此方法
     */
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        LogUtils.e("SuningWebChromeClient", "运行方法 onShowFileChooser filePathCallback" + filePathCallback.toString());
        // (1)该方法回调时说明版本API >= 21，此时将结果赋值给 mUploadCallbackAboveL，使之 != null
        mUploadCallbackAboveL = filePathCallback;
        takePhoto();
        return true;
    }

    /**
     * 调用相机 图库
     */
    private void takePhoto() {
        // 指定拍照存储位置的方式调起相机
        String filePath = Environment.getExternalStorageDirectory() + File.separator
                + Environment.DIRECTORY_PICTURES + File.separator;
        String fileName = "IMG_" + DateFormat.format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";

        File file = new File(filePath);
         //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }

        Intent captureIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
//        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);

        if (Build.VERSION.SDK_INT < 24) {
            // 从文件中创建uri
            imageUri = Uri.fromFile(new File(filePath + fileName));
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        } else {
            //兼容android7.0 使用共享文件的形式
            ContentValues contentValues = new ContentValues(1);
            contentValues.put(MediaStore.Images.Media.DATA, new File(filePath + fileName).getAbsolutePath());
            imageUri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
            captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            captureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
//        选择图片
        Intent Photo = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        Intent chooserIntent = Intent.createChooser(Photo, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[]{captureIntent});
        activity.startActivityForResult(chooserIntent, 10);

    }
}