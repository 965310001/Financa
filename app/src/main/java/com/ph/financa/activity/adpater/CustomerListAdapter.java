//package com.ph.financa.activity.adpater;
//
//import androidx.appcompat.widget.AppCompatCheckBox;
//
//import com.chad.library.adapter.base.BaseQuickAdapter;
//import com.chad.library.adapter.base.BaseViewHolder;
//import com.ph.financa.R;
//import com.ph.financa.activity.bean.CustomerBean;
//
//import tech.com.commoncore.manager.GlideManager;
//
//public class CustomerListAdapter extends BaseQuickAdapter<CustomerBean, BaseViewHolder> {
//
//    private boolean select;
//
//    public CustomerListAdapter(int layoutResId) {
//        super(layoutResId);
//    }
//
//    @Override
//    protected void convert(BaseViewHolder helper, CustomerBean item) {
//        GlideManager.loadRoundImg(item.getHeadImgUrl(), helper.getView(R.id.iv_head));
//        helper.setText(R.id.tv_name, item.getCustomerName())
//                .setText(R.id.tv_count, String.format("查看文章：%s", item.getCount().toString()));
//        select = item.isSelect();
//        helper.setGone(R.id.cb_check, select);
//
//        if (select) {
//            AppCompatCheckBox cbCheck = helper.getView(R.id.cb_check);
//            cbCheck.setChecked(item.isCheck());
//            cbCheck.setOnCheckedChangeListener((compoundButton, b) -> item.setCheck(b));
//        }
//
//    }
//
//}
