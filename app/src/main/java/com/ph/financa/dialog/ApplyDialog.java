package com.ph.financa.dialog;

import android.os.Bundle;
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

import tech.com.commoncore.utils.DisplayUtil;

/**
 * 申请成功
 */
public class ApplyDialog extends DialogFragment implements View.OnClickListener {

    public static ApplyDialog show(FragmentManager manager) {
        ApplyDialog shareDialog = new ApplyDialog();
        shareDialog.show(manager, "");
        return shareDialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_apply, container);
        view.findViewById(R.id.tv_ok).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

    /*设置透明*/
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        window.setLayout((int) (DisplayUtil.getScreenWidth() * 0.8), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(null);
        window.setAttributes(windowParams);
    }
}
