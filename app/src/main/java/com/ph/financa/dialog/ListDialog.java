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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ph.financa.R;

import java.util.List;

import tech.com.commoncore.utils.DisplayUtil;

/**
 * list Dialog
 */
public class ListDialog extends DialogFragment {

    private List<String> mData;

    private BaseQuickAdapter.OnItemClickListener mClick;

    public static ListDialog show(FragmentManager manager, String tag, List<String> data, BaseQuickAdapter.OnItemClickListener click) {
        ListDialog listDialog = new ListDialog();
        listDialog.show(manager, tag);
        listDialog.setClick(click);
        listDialog.setData(data);
        return listDialog;
    }

    public void setData(List<String> data) {
        this.mData = data;
    }

    public void setClick(BaseQuickAdapter.OnItemClickListener mClick) {
        this.mClick = mClick;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_list, container);
        RecyclerView recyclerView = view.findViewById(R.id.rv_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        ListAdapter adapter = new ListAdapter(R.layout.item_list_text);
        adapter.setNewData(mData);
//        adapter.setOnItemChildClickListener((adapter1, view1, position) -> {
//            Log.i("TAG", "onCreateView: " + position);
//            if (null != mClick) {
//                mClick.onItemChildClick(adapter1, view1, position);
//            }
//        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (null != mClick) {
                    mClick.onItemClick(adapter, view, position);
                }
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
    }

    /*设置透明*/
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        window.setLayout((int) (DisplayUtil.getScreenWidth() * 0.7), ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(null);
        window.setAttributes(windowParams);
    }
}