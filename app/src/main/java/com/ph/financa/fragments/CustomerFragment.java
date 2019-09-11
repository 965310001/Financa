package com.ph.financa.fragments;

import android.os.Bundle;
import android.view.View;

import com.ph.financa.R;
import com.ph.financa.dialog.ListDialog;
import com.ph.financa.dialog.MoreDialog;
import com.ph.financa.dialog.SortNameDialog;

import java.util.Arrays;
import java.util.List;

import tech.com.commoncore.base.BaseFragment;

/**
 * 客户
 */
public class CustomerFragment extends BaseFragment implements View.OnClickListener {

    private SortNameDialog mSortNameDialog;
    private MoreDialog moreDialog;
    private ListDialog mListDialog;

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.findViewById(R.id.iv_search).setOnClickListener(this);
        mContentView.findViewById(R.id.iv_more).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_search:
                break;
            case R.id.iv_more:
                showMoreDialog();
                break;
        }
    }

    private void showMoreDialog() {
        moreDialog = MoreDialog.show(getFragmentManager(), "", index -> {
            switch (index) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    moreDialog.dismiss();
                    showSortDialog();
                    break;
                case 3:
                    showListDialog();
                    break;
            }
        });
    }

    private void showListDialog() {
        List<String> list = Arrays.asList("从谁看了我导入", "从通讯录导入", "取消");
        mListDialog = ListDialog.show(getFragmentManager(), "", list, (adapter, view, position) -> {
            mListDialog.dismiss();
            switch (position) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        });
    }

    private void showSortDialog() {
        mSortNameDialog = SortNameDialog.show(getFragmentManager(), "", index -> {
            mSortNameDialog.dismiss();
            switch (index) {
                case 0:
                    break;
                case 1:
                    break;
                case 2:
                    break;
            }
        });
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_customer;
    }
}
