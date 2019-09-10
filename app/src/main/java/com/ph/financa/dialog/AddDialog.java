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

public class AddDialog extends DialogFragment implements View.OnClickListener {

    public static AddDialog show(FragmentManager manager, String tag, OnClickIndex click) {
        AddDialog addDialog = new AddDialog();
        addDialog.show(manager, tag);
        addDialog.setClick(click);
        return addDialog;
    }

    private OnClickIndex click;

    public void setClick(OnClickIndex click) {
        this.click = click;
    }

    public interface OnClickIndex {
        void onIndex(int index);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add, container);
        view.findViewById(R.id.ll_edit).setOnClickListener(this);
        view.findViewById(R.id.ll_share_link).setOnClickListener(this);
        view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (null != click) {
            int index = 0;
            switch (view.getId()) {
                case R.id.ll_edit:
                    index = 0;
                    break;
                case R.id.ll_share_link:
                    index = 1;
                    break;
                case R.id.tv_cancel:
                    index = 2;
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
        /*windowParams.dimAmount = 0.0f;*/
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /*window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);*/
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(null);
        window.setAttributes(windowParams);
    }
}