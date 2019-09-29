package com.vendor.social.support.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.vendor.social.ShareApi;
import com.vendor.social.model.ShareContent;
import com.vendor.social.share.WeiboShare;
import com.vendor.social.utils.BitmapLoader;

/**
 * 微博分享回调页面
 * Created by ljfan on 16/4/28.
 */
public class WbBaseActivity extends Activity implements WbShareCallback {

    private static String EXTRA_KEY = "extra_key";

    /**
     * 由此页面独立处理 这样app就不要额外处理这个页面的细节
     *
     * @param weiboContent 分享的内容
     */
    public static void allocActivity(Context context, ShareContent weiboContent) {
        Intent intent = new Intent(context, WbBaseActivity.class);
        intent.putExtra(EXTRA_KEY, weiboContent);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ShareContent shareContent = getIntent().getParcelableExtra(EXTRA_KEY);
        final String content = shareContent.getTitle() + "\n" + shareContent.getText() + "\n" + shareContent.getTargetUrl();

        //获取bitmap
        new BitmapLoader().loadIconBitmap(this, shareContent, new BitmapLoader.OnLoadImageListener() {
            @Override
            public void onResult(Bitmap bitmap) {
                if (bitmap != null) {
                    // 1. 初始化微博的分享消息
                    WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
                    TextObject textObject = new TextObject();
                    textObject.text = content;
                    weiboMessage.textObject = textObject;

                    ImageObject imageObject = new ImageObject();
                    imageObject.setImageObject(bitmap);
                    weiboMessage.imageObject = imageObject;

//                    // 2. 初始化从第三方到微博的消息请求
//                    SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
//                    // 用transaction唯一标识一个请求
//                    request.transaction = String.valueOf(System.currentTimeMillis());
//                    request.multiMessage = weiboMessage;
//
//                    // 3. 发送请求消息到微博，唤起微博分享界面
//                    WeiboShare.getWeiboShareAPI().sendRequest(WbBaseActivity.this, request);

                    WeiboShare.getWeiboShareAPI().shareMessage(weiboMessage,false);
                } else {
                    WeiboShare.callbackShareFail("ImageLoader load image fail");
                }
            }
        });
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (WeiboShare.getWeiboShareAPI() != null) {
//            WeiboShare.getWeiboShareAPI().handleWeiboResponse(intent, this);

            WeiboShare.getWeiboShareAPI().doResultIntent(intent,this);

        }
    }

//    @Override
//    public void onResponse(BaseResponse baseResponse) {
//        switch (baseResponse.errCode) {
//            case WBConstants.ErrorCode.ERR_OK:
//                ShareApi.callbackShareOk();
//                break;
//            case WBConstants.ErrorCode.ERR_FAIL:
//            case WBConstants.ErrorCode.ERR_CANCEL:
//                ShareApi.callbackShareFail(baseResponse.errMsg);
//                break;
//        }
//
//        finish();
//    }

    @Override
    public void onWbShareSuccess() {
        ShareApi.callbackShareOk();
        finish();
    }

    @Override
    public void onWbShareCancel() {
        ShareApi.callbackShareFail("分享取消");
    }

    @Override
    public void onWbShareFail() {
        ShareApi.callbackShareFail("分享失败");
    }
}
