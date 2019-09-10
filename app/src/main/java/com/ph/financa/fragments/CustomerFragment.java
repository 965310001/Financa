package com.ph.financa.fragments;

import android.os.Bundle;
import android.view.View;

import com.ph.financa.R;
import com.ph.financa.dialog.MoreDialog;
import com.ph.financa.dialog.SortNameDialog;

import tech.com.commoncore.base.BaseFragment;

/**
 * 客户
 */
public class CustomerFragment extends BaseFragment implements View.OnClickListener {

    private SortNameDialog mSortNameDialog;
    private MoreDialog moreDialog;

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
                    break;
            }
        });
    }

    private void showSortDialog() {
        mSortNameDialog = SortNameDialog.show(getFragmentManager(), "", index -> {
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
