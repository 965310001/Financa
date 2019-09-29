package com.ph.financa.activity.adpater;

import android.util.Log;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ph.financa.R;
import com.ph.financa.activity.bean.ContactModel;

import java.util.List;

public class ContactsAdapter extends BaseQuickAdapter<ContactModel, BaseViewHolder> {

    public ContactsAdapter(int layoutResId, @Nullable List<ContactModel> data) {
        super(layoutResId, data);
    }

    String index = "";

    @Override
    protected void convert(BaseViewHolder helper, ContactModel item) {
        Log.e(TAG, "onBindViewHolder: index:" + item.getIndex() + " " + item.getName());
        /*int position = getParentPosition(item);*/

        if (!item.getIndex().equals(index)) {
            helper.setText(R.id.tv_index, item.getIndex());
            helper.setVisible(R.id.tv_index, true);

            index = item.getIndex();
        } else {
            helper.setGone(R.id.tv_index, false);
        }

       /* if (position == 0 || (position >= 1 && !getData().get(position - 1).getIndex().equals(item.getIndex()))) {
            helper.setText(R.id.tv_index, item.getIndex());
            helper.setVisible(R.id.tv_index, true);
        } else {
            helper.setGone(R.id.tv_index, false);
        }*/

        helper.setText(R.id.tv_name, item.getName());
    }
}
