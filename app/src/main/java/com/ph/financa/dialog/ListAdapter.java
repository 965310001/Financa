package com.ph.financa.dialog;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ph.financa.R;

public class ListAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public ListAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        helper.setText(R.id.tv_text, item);
    }

}
