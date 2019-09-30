package com.ph.financa.activity.adpater;

import android.graphics.Color;
import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.ph.financa.R;
import com.ph.financa.activity.bean.ContactModel;

import java.util.List;
import java.util.Random;

public class ContactsAdapter extends BaseQuickAdapter<ContactModel, BaseViewHolder> {

    public ContactsAdapter(int layoutResId, @Nullable List<ContactModel> data) {
        super(layoutResId, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ContactModel item) {
        if (helper.getPosition() == 0 || !getData().get(helper.getPosition() - 1).getIndex().equals(item.getIndex())) {
            helper.setText(R.id.tv_index, item.getIndex());
            helper.setVisible(R.id.tv_index, true);
        } else {
            helper.setGone(R.id.tv_index, false);
            /*helper.setVisible(R.id.view_line, true);*/

        }
        String name = item.getName();
        if (!TextUtils.isEmpty(name)) {
            helper.setText(R.id.tv_name, item.getName());

            if (item.getColor() == 0) {
                item.setColor(getRandomColor());
            }

            helper.setBackgroundColor(R.id.iv_avatar, item.getColor());
            if (name.length() > 3) {
                name = name.substring(0, 2);
            }
            helper.setText(R.id.tv_des, name);
        }
        helper.setText(R.id.tv_phone, item.getPhone());

        helper.setChecked(R.id.cb_check, item.isCheck());

        helper.addOnClickListener(R.id.cl_select);

    }

    int getRandomColor() {
        Random rnd = new Random();
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
    }
}
