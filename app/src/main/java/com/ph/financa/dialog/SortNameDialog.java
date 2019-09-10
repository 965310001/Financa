package com.ph.financa.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.ph.financa.R;

public class SortNameDialog extends DialogFragment implements View.OnClickListener {

    public static SortNameDialog show(FragmentManager manager, String tag, OnClickIndex click) {
        SortNameDialog sortNameDialog = new SortNameDialog();
        sortNameDialog.show(manager, tag);
        sortNameDialog.setClick(click);
        return sortNameDialog;
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
        View view = inflater.inflate(R.layout.dialog_sort_name, container);
        view.findViewById(R.id.tv_default).setOnClickListener(this);
        view.findViewById(R.id.tv_order).setOnClickListener(this);
        view.findViewById(R.id.tv_inverted_order).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {
        if (null != click) {
            int index = 0;
            switch (view.getId()) {
                case R.id.tv_default:
                    index = 0;
                    break;
                case R.id.tv_order:
                    index = 1;
                    break;
                case R.id.tv_inverted_order:
                    index = 2;
                    break;
            }
            click.onIndex(index);
        }
    }
}