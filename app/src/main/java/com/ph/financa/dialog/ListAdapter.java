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

//        ImageView ivImg = helper.getView(R.id.iv_img);
//        if (!TextUtils.isEmpty(item.getProfilePhoto())) {
//            GlideManager.loadCircleImg(item.getProfilePhoto(), ivImg);
//        } else {
//            GlideManager.loadCircleImg(R.mipmap.man, ivImg);
//        }
//        helper.setText(R.id.tv_log, String.format("%s%s%s", item.getUName(), item.getOperatingType(), item.getBeName(), item.getContent()))
//                .setText(R.id.tv_time, item.getCreateTime());
    }

}
