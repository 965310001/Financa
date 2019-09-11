package com.ph.financa.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.aries.ui.view.title.TitleBarView;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;
import com.ph.financa.dialog.PayDialog;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.widget.CircleImageView;

public class VipActivity extends BaseTitleActivity {

    private CircleImageView mIvHead;
    private TextView mTvName;
    private TextView mTvVip;
    private TextView mTvThreeDays;
    private TextView mTvPrice;
    private LinearLayout mLLPrivilege, mLLPrivilegeOne, mLLPrivilegeTwo;

    private String[] titles1 = {"智能名片", "小程序", "人脉追踪", "获客分析"};
    private String[] titles2 = {"客户喜好", "营销升级", "一键获客", "快速响应"};
    private String[] titles = {"智能名片", "小程序", "人脉追踪", "获客分析", "客户喜好", "营销升级", "一键获客", "快速响应"};
    private PayDialog mPayDialog;

    @Override
    public void initView(Bundle savedInstanceState) {
        mIvHead = findViewById(R.id.iv_head);
        mTvName = findViewById(R.id.tv_name);
        mTvVip = findViewById(R.id.tv_vip);
        mTvThreeDays = findViewById(R.id.tv_three_days);
        mTvPrice = findViewById(R.id.tv_price);
        mLLPrivilege = findViewById(R.id.ll_privilege);

        mLLPrivilegeOne = findViewById(R.id.ll_privilege_one);
        mLLPrivilegeTwo = findViewById(R.id.ll_privilege_two);

        String[] identifications1 = {"个人IP", "商城定制", "成单神器", "获客宝典"};
        int[] icon1 = {R.mipmap.ic_privilege1, R.mipmap.ic_privilege2, R.mipmap.ic_privilege3, R.mipmap.ic_privilege4};
        createPrivilegeView(mLLPrivilegeOne, titles1, identifications1, icon1);

        String[] identifications2 = {"数据统计", "导入潜客", "发产品", "即时通讯"};
        int[] icon2 = {R.mipmap.ic_privilege5, R.mipmap.ic_privilege6, R.mipmap.ic_privilege7, R.mipmap.ic_privilege8};
        createPrivilegeView(mLLPrivilegeTwo, titles2, identifications2, icon2);

        setData(SPHelper.getStringSF(mContext, Constant.USERNAME, "")
                , SPHelper.getStringSF(mContext, Constant.USERHEAD, ""));
    }

    private void setData(String name, String head) {
        if (!TextUtils.isEmpty(name)) {
            mTvName.setText(name);
        } else {
            mTvName.setText("游客");
        }

        if (!TextUtils.isEmpty(head)) {
            GlideManager.loadCircleImg(head, mIvHead);
        }
    }

    private void createPrivilegeView(LinearLayout linearLayout, String[] titles, String[] identifications, int[] icons) {
        if (null != titles && null != icons) {
            for (int i = 0; i < titles.length; i++) {
                LinearLayout itemView = (LinearLayout) View.inflate(mContext, R.layout.item_privilege_text_icon, null);
                AppCompatImageView ivIcon = itemView.findViewById(R.id.iv_icon);
                TextView tvIdentification = itemView.findViewById(R.id.tv_identification);
                TextView tvText = itemView.findViewById(R.id.tv_text);
                ivIcon.setImageResource(icons[i]);
                tvIdentification.setText(identifications[i]);
                tvText.setText(titles[i]);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                itemView.setLayoutParams(params);

                itemView.setOnClickListener(this::onClick);
                linearLayout.addView(itemView);
            }
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("VIP会员");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_vip;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_three_days:
                break;

            case R.id.tv_open:
                showPayDialog();
                break;

            case R.id.ll:
                TextView tv = view.findViewById(R.id.tv_text);
                String text = tv.getText().toString().trim();
                for (int i = 0; i < titles.length; i++) {
                    if (text.equals(titles[i])) {
                        switch (i) {
                            case 0:
                                break;
                            case 1:
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            case 6:
                                break;
                            case 7:
                                break;
                            case 8:
                                break;
                        }
                    }
                }
                break;
        }
    }

    private void showPayDialog() {
        mPayDialog = PayDialog.show(getSupportFragmentManager(), "", index -> {
            switch (index) {
                case 0:
                    mPayDialog.dismiss();
                    break;
                case 1:/*微信*/
                    weiXinPay();
                    break;
                case 2:/*支付宝*/
                    zhiFuBaoPay();
                    break;
                case 3:/*未选择*/
                    ToastUtil.show("请选择支付方式");
                    break;
            }
        });
    }

    private void zhiFuBaoPay() {

    }

    private void weiXinPay() {

    }
}
