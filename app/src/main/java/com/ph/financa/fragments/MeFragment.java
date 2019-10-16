package com.ph.financa.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.ph.financa.R;
import com.ph.financa.activity.CustomerActivity;
import com.ph.financa.activity.SettingActivity;
import com.ph.financa.activity.WebActivity;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.MessageCountBean;
import com.ph.financa.activity.bean.UserBean;
import com.ph.financa.constant.Constant;
import com.ph.financa.ease.FriendTable;
import com.ph.financa.utils.StatusBarUtils;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
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


    private TextView mTv1, mTv2, mTv3;

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        if (mContentView != null) {
            mContentView.setPadding(0, 0, 0, 0);
            StatusBarUtils.immersive(getActivity(), false);
        }
        super.onVisibleChanged(isVisibleToUser);
    }

    @Override
    public void initView(Bundle savedInstanceState) {

        mIcHead = mContentView.findViewById(R.id.ic_head);
        mTvName = mContentView.findViewById(R.id.tv_name);
        mTvCompanyName = mContentView.findViewById(R.id.tv_company_name);

        mContentView.findViewById(R.id.tv_rights).setOnClickListener(this);
        mContentView.findViewById(R.id.tv_receive).setOnClickListener(this);
        mContentView.findViewById(R.id.ic_open).setOnClickListener(this);
        mContentView.findViewById(R.id.ic_crown_open).setOnClickListener(this);

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

        mTv1 = mContentView.findViewById(R.id.tv_msg_count1);
        mTv2 = mContentView.findViewById(R.id.tv_msg_count2);
        mTv3 = mContentView.findViewById(R.id.tv_msg_count3);

        getUnreadMsgCount();

        getMessagePull();
        /*getMessageCount();*/
    }

    /*用户拉取信息*/
    private void getMessagePull() {
        ViseHttp.POST(ApiConstant.MESSAGE_PULL)
                .request(new ACallback<BaseTResp2<String>>() {
                    @Override
                    public void onSuccess(BaseTResp2<String> data) {
                        if (data.isSuccess()) {
                            Log.i(TAG, "onSuccess: " + data.getData());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });
    }

    /*消息模块-获取用户未读统计*/
    private void getMessageCount() {
        ViseHttp.GET(ApiConstant.MESSAGE_COUNT)
                .request(new ACallback<BaseTResp2<MessageCountBean>>() {
                    @Override
                    public void onSuccess(BaseTResp2<MessageCountBean> data) {
                        if (data.isSuccess()) {
                            int count = data.getData().getUnreadCount();
                            if (count > 0) {
                                mTv1.setVisibility(View.VISIBLE);
//                                mTvMessage.setVisibility(View.VISIBLE);

                                mTvMessage.setText(String.format("%s", count));
                            } else {
                                mTv1.setVisibility(View.GONE);
//                                mTvMessage.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });

    }


    private void getUnreadMsgCount() {
        /*用户*/
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(SPHelper.getStringSF(mContext, Constant.CUSTOMSERVICE));
        if (null != conversation) {
           /* int count = conversation.getUnreadMsgCount();
            if (count > 0) {
                mTv1.setVisibility(View.VISIBLE);
            }*/
            /*客服*/
            int count = conversation.getUnreadMsgCount();
            if (count > 0) {
                mTv2.setVisibility(View.VISIBLE);
            }
        }
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
//            mTvCompanyName.setText("公司名字");
        }

        if (!TextUtils.isEmpty(head)) {
            GlideManager.loadCircleImg(head, mIcHead);
        } else {
            GlideManager.loadCircleImg(R.mipmap.ic_user_head, mIcHead);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfo();

        getUnreadMsgCount();

        getMessageCount();
    }

    /*获取用户信息*/
    private void getUserInfo() {
        ViseHttp.GET(ApiConstant.GET_USER)
                .request(new ACallback<BaseTResp2<UserBean>>() {
                    @Override
                    public void onSuccess(BaseTResp2<UserBean> data) {
                        UserBean bean = data.data;
                        if (null != bean) {
                            saveUser(bean);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });
    }

    /*保存用户信息*/
    private void saveUser(UserBean data) {
        if (null != data) {
            SPHelper.setStringSF(mContext, Constant.USERNAME, data.getName());
            SPHelper.setStringSF(mContext, Constant.USERCOMPANYNAME, data.getCompanyName());
            SPHelper.setStringSF(mContext, Constant.USERHEAD, data.getHeadImgUrl());
            SPHelper.setStringSF(mContext, Constant.USERID, String.valueOf(data.getId()));
            SPHelper.setStringSF(mContext, Constant.USERPHONE, data.getTelephone());

            SPHelper.setIntergerSF(mContext, Constant.ISVIP, data.getUserType());


            setData(SPHelper.getStringSF(mContext, Constant.USERNAME, "")
                    , SPHelper.getStringSF(mContext, Constant.USERCOMPANYNAME, ""),
                    SPHelper.getStringSF(mContext, Constant.USERHEAD, ""));

        }
    }


    @Override
    public void onClick(View view) {
        Bundle bundle = new Bundle();
        switch (view.getId()) {
            case R.id.tv_rights:
            case R.id.tv_receive:
            case R.id.ic_crown_open:
            case R.id.ic_open:
                // TODO: 2019/9/9 开通688会员
                bundle.putString("url", getUrl(ApiConstant.PAYMENT));
                goActivity(WebActivity.class, bundle);
                break;

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
                bundle.putString("url", getUrl(ApiConstant.MESSAGE));
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
