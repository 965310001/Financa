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

public class MoreDialog extends DialogFragment implements View.OnClickListener {

    public static MoreDialog show(FragmentManager manager, String tag, OnClickIndex click) {
        MoreDialog moreDialog = new MoreDialog();
        moreDialog.show(manager, tag);
        moreDialog.setClick(click);
        return moreDialog;
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
        View view = inflater.inflate(R.layout.dialog_more, container);
        view.findViewById(R.id.ll_new).setOnClickListener(this);
        view.findViewById(R.id.ll_group).setOnClickListener(this);
        view.findViewById(R.id.ll_sort).setOnClickListener(this);
        view.findViewById(R.id.ll_import).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (null != click) {
            int index = 0;
            switch (view.getId()) {
                case R.id.ll_new:
                    index = 0;
                    break;
                case R.id.ll_group:
                    index = 1;
                    break;
                case R.id.ll_sort:
                    index = 2;
                    break;
                case R.id.ll_import:
                    index = 3;
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
        window.setGravity(Gravity.TOP | Gravity.RIGHT);
        window.setBackgroundDrawable(null);
        window.setAttributes(windowParams);
    }
}