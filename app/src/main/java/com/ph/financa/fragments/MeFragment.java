package com.ph.financa.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.easeui.EaseConstant;
import com.ph.financa.R;
import com.ph.financa.activity.CustomerActivity;
import com.ph.financa.activity.SettingActivity;
import com.ph.financa.activity.VipActivity;
import com.ph.financa.activity.WebActivity;
import com.ph.financa.constant.Constant;
import com.ph.financa.ease.FriendTable;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.Utils;
import tech.com.commoncore.widget.CircleImageView;

/**
 * 我的
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {

    private TextView mTvCompanyName;//公司名字
    private TextView mTvName;//姓名
    private CircleImageView mIcHead;//头像
    private TextView mTvCollection;//收藏
    private TextView mTvProducts;//产品
    private TextView mTvShare;//分享
    private TextView mTvMessage;//消息

//    @Override
//    public void onResume() {
//        super.onResume();
////        mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
////        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.ce6454a));
////        StatusBarUtils.setPadding(mContext,mContentView);
//    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mIcHead = mContentView.findViewById(R.id.ic_head);
        mTvName = mContentView.findViewById(R.id.tv_name);
        mTvCompanyName = mContentView.findViewById(R.id.tv_company_name);

        mContentView.findViewById(R.id.tv_rights).setOnClickListener(this);
        mContentView.findViewById(R.id.tv_receive).setOnClickListener(this);
        mContentView.findViewById(R.id.ic_open).setOnClickListener(this);

        mContentView.findViewById(R.id.ll_card).setOnClickListener(this);
        mContentView.findViewById(R.id.ic_head).setOnClickListener(this);
        mContentView.findViewById(R.id.ll_collection).setOnClickListener(this);
        mContentView.findViewById(R.id.ll_products).setOnClickListener(this);
        mContentView.findViewById(R.id.ll_share).setOnClickListener(this);
        mContentView.findViewById(R.id.ll_message).setOnClickListener(this);
        mContentView.findViewById(R.id.ll_service).setOnClickListener(this);
        mContentView.findViewById(R.id.ll_setting).setOnClickListener(this);

        mTvCollection = mContentView.findViewById(R.id.tv_collection);
        mTvProducts = mContentView.findViewById(R.id.tv_products);
        mTvShare = mContentView.findViewById(R.id.tv_share);
        mTvMessage = mContentView.findViewById(R.id.tv_message);

        Log.i(TAG, "loadData: " + SPHelper.getStringSF(mContext, Constant.USERNAME, ""));
        setData(SPHelper.getStringSF(mContext, Constant.USERNAME, "")
                , SPHelper.getStringSF(mContext, Constant.USERCOMPANYNAME, ""),
                SPHelper.getStringSF(mContext, Constant.USERHEAD, ""));
    }

    private void setData(String name, String companyName, String head) {
        if (!TextUtils.isEmpty(name)) {
            mTvName.setText(name);
        } else {
            mTvName.setText("游客");
        }

        if (!TextUtils.isEmpty(companyName)) {
            mTvCompanyName.setText(companyName);
        } else {
            mTvCompanyName.setText("公司名字");
        }

        if (!TextUtils.isEmpty(head)) {
            GlideManager.loadCircleImg(head, mIcHead);
        } else {
            GlideManager.loadCircleImg(R.mipmap.ic_user_head, mIcHead);
        }
    }

    /*设置 收藏、产品、分享、消息*/
    private void setNumber(TextView tv, String number) {
        if (!TextUtils.isEmpty(number)) {
            tv.setText(number);
            tv.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_rights:
                // TODO: 2019/9/9 会员权益
                goActivity(null, null);
                break;
//            case R.id.tv_receive:
//                // TODO: 2019/9/9 新会员专享688/两年 立即领取
//                goActivity(null, null);
//                break;
            case R.id.ic_open:
                // TODO: 2019/9/9 开通688会员
                goActivity(VipActivity.class, null);
                break;
            case R.id.tv_receive:
            case R.id.ic_head:
            case R.id.ll_card:
                // TODO: 2019/9/9 我的名片
                bundle.putString("url", getUrl(ApiConstant.MY_CARD));
                goActivity(WebActivity.class, bundle);
                break;
            case R.id.ll_collection:
                // TODO: 2019/9/9 我的收藏
                bundle.putString("url", getUrl(ApiConstant.COLLECTION));
                goActivity(WebActivity.class, bundle);
                break;
            case R.id.ll_products:
                // TODO: 2019/9/9 我的产品
                bundle.putString("url", getUrl(ApiConstant.MY_PRODUCT));
                goActivity(WebActivity.class, bundle);
                break;
            case R.id.ll_share:
                // TODO: 2019/9/9 历史分享
                bundle.putString("url", getUrl(ApiConstant.HISTORYARTICLE));
                goActivity(WebActivity.class, bundle);
                break;
            case R.id.ll_message:
                // TODO: 2019/9/9 消息通知
                bundle.putString("url", getUrl(ApiConstant.MESSAGE_NOTICE));
                goActivity(WebActivity.class, bundle);
                break;
            case R.id.ll_service:
                // TODO: 2019/9/9 我的客服
                bundle.putString(EaseConstant.EXTRA_USER_ID, Constant.CUSTOMSERVICE);
                bundle.putString(FriendTable.FRIEND_NAME, "我的客服");
                bundle.putString(FriendTable.FRIEND_HEAD, "客服");
                goActivity(CustomerActivity.class, bundle);
                break;
            case R.id.ll_setting:
                // TODO: 2019/9/9 设置
                goActivity(SettingActivity.class, null);
                break;
        }
    }

    private String getUrl(String url) {
        return String.format("%s%s?userId=%s&openId=%s", ApiConstant.BASE_URL_ZP, url,
                SPHelper.getStringSF(Utils.getContext(), Constant.USERID, ""),
                SPHelper.getStringSF(Utils.getContext(), Constant.WXOPENID, ""));
    }

    private void goActivity(Class clazz, Bundle bundle) {
        if (null != bundle) {
            FastUtil.startActivity(mContext, clazz, bundle);
        } else {
            FastUtil.startActivity(mContext, clazz);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_me;
    }
}
