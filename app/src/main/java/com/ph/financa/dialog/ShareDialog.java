package com.ph.financa.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.ph.financa.R;

/**
 * 分享
 */

//private void copyText(String text) {
//        ClipboardManager copy = (ClipboardManager) MainActivity.this
//        .getSystemService(Context.CLIPBOARD_SERVICE);
//        copy.setText(text);
//        }

//private ShareDialog mShareDialog;
//  mShareDialog = ShareDialog.show(getSupportFragmentManager(), "", index -> {
//          mShareDialog.dismiss();
//          switch (index) {
//          case 0:/*微信*/
//          Map<String, String> map = new HashMap<>();
//        map.put("title", "");
//        map.put("description", "");
//        map.put("imageurl", "");
//        WeiXinBaoStrategy weiXinBaoStrategy = WeiXinBaoStrategy.getInstance(mContext);
//        weiXinBaoStrategy.wechatShare(Constant.WEIXIN_APP_ID, index, map, new JPayListener() {
//@Override
//public void onPaySuccess() {
//        ToastUtil.show("分享成功");
//        }
//
//@Override
//public void onPayError(int error_code, String message) {
//        ToastUtil.show(message);
//        }
//
//@Override
//public void onPayCancel() {
//
//        }
//
//@Override
//public void onUUPay(String dataOrg, String sign, String mode) {
//
//        }
//        });
//        break;
//        case 1:/*朋友圈*/
//        break;
//        case 2:/*复制链接*/
//        copyText("");
//        break;
//        case 3:/*新浪微博*/
//        break;
//        }
//        });


public class ShareDialog extends DialogFragment implements View.OnClickListener {

    public static ShareDialog show(FragmentManager manager, String tag, AddDialog.OnClickIndex click) {
        ShareDialog shareDialog = new ShareDialog();
        shareDialog.show(manager, tag);
        shareDialog.setClick(click);
        return shareDialog;
    }

    private AddDialog.OnClickIndex click;

    public void setClick(AddDialog.OnClickIndex click) {
        this.click = click;
    }

    public interface OnClickIndex {
        void onIndex(int index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_share, container);
        view.findViewById(R.id.ll_share1).setOnClickListener(this);
        view.findViewById(R.id.ll_share2).setOnClickListener(this);
        view.findViewById(R.id.ll_share3).setOnClickListener(this);
        view.findViewById(R.id.ll_share4).setOnClickListener(this);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (null != click) {
            int index = 0;
            switch (view.getId()) {
                case R.id.ll_share1:
                    index = 0;
                    break;
                case R.id.ll_share2:
                    index = 1;
                    break;
                case R.id.ll_share3:
                    index = 2;
                    break;
                case R.id.ll_share4:
                    index = 3;
                    break;
                case R.id.iv_close:
                    index = -1;
                    dismiss();
                    break;
            }
            click.onIndex(index);
        }
    }

    /*设置透明*/
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(null);
        window.setAttributes(windowParams);
    }
}