package com.ph.financa.dialog;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.ph.financa.R;

import tech.com.commoncore.utils.DisplayUtil;

/**
 * 弹出消息
 */
public class MessageAlertDialog extends DialogFragment implements View.OnClickListener {

    private MessageAlertDialog.OnClickIndex click;

    private String title, ok;

    public static MessageAlertDialog show(FragmentManager manager, String tag, String title, String ok, MessageAlertDialog.OnClickIndex click) {
        MessageAlertDialog messageAlertDialog = new MessageAlertDialog();
        messageAlertDialog.show(manager, tag);
        messageAlertDialog.setClick(click);
        messageAlertDialog.setTitle(title);
        messageAlertDialog.setOk(ok);
        return messageAlertDialog;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOk(String ok) {
        this.ok = ok;
    }

    public void setClick(MessageAlertDialog.OnClickIndex click) {
        this.click = click;
    }

    public interface OnClickIndex {
        void onIndex(int index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_message_alert, container);

        TextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText(title);

        TextView tvOk = view.findViewById(R.id.tv_ok);

        if (!TextUtils.isEmpty(ok)) {
            tvOk.setText(ok);
        } else {
            tvOk.setText("确定");
        }

        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        view.findViewById(R.id.tv_ok).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (null != click) {
            int index = 0;
            switch (view.getId()) {
                case R.id.tv_cancel:
                    index = 0;
                    break;
                case R.id.tv_ok:
                    index = 1;
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
        window.setLayout((int) (DisplayUtil.getScreenWidth() * 0.7), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.setBackgroundDrawable(null);
        window.setAttributes(windowParams);
    }
}
