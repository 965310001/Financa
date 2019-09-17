package com.ph.financa.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.ph.financa.activity.bean.InsertBean;
import com.ph.financa.activity.bean.PayBean;
import com.ph.financa.activity.bean.SelectBean;
import com.ph.financa.activity.bean.VipBean;
import com.ph.financa.constant.Constant;
import com.ph.financa.dialog.PayDialog;
import com.ph.financa.wxapi.pay.Context;
import com.ph.financa.wxapi.pay.JPayListener;
import com.ph.financa.wxapi.pay.WeiXinBaoStrategy;
import com.ph.financa.wxapi.pay.ZhiFuBaoStrategy;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.manager.GlideManager;
import tech.com.commoncore.utils.DisplayUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.ToastUtil;
import tech.com.commoncore.utils.ToastUtils;
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
    private LinearLayout mLLRecommend;

    private View mOldView;
    private long mServiceId;/*服务模板ID*/

    private String[] PAYTYPE = {"wxpay", "alipay"};

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getColor(R.color.white));
        mIvHead = findViewById(R.id.iv_head);
        mTvName = findViewById(R.id.tv_name);
        mTvVip = findViewById(R.id.tv_vip);
        mTvThreeDays = findViewById(R.id.tv_three_days);
        mTvPrice = findViewById(R.id.tv_price);
        mLLPrivilege = findViewById(R.id.ll_privilege);

        mLLPrivilegeOne = findViewById(R.id.ll_privilege_one);
        mLLPrivilegeTwo = findViewById(R.id.ll_privilege_two);

        mLLRecommend = findViewById(R.id.ll_recommend);

        String[] identifications1 = {"个人IP", "商城定制", "成单神器", "获客宝典"};
        int[] icon1 = {R.mipmap.ic_privilege1, R.mipmap.ic_privilege2, R.mipmap.ic_privilege3, R.mipmap.ic_privilege4};
        createPrivilegeView(mLLPrivilegeOne, titles1, identifications1, icon1);

        String[] identifications2 = {"数据统计", "导入潜客", "发产品", "即时通讯"};
        int[] icon2 = {R.mipmap.ic_privilege5, R.mipmap.ic_privilege6, R.mipmap.ic_privilege7, R.mipmap.ic_privilege8};
        createPrivilegeView(mLLPrivilegeTwo, titles2, identifications2, icon2);

        setData(SPHelper.getStringSF(mContext, Constant.USERNAME, "")
                , SPHelper.getStringSF(mContext, Constant.USERHEAD, ""));

        setPrivilegeData(null);
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

    private void setRecommendData(String year, String price, String oldPrice, TextView tvYear, TextView tvPrice, TextView tvOldPrice) {
        tvYear.setText(year);
        tvPrice.setText(price);
        tvOldPrice.setText(String.format("￥%s", oldPrice));
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

    /*特权图片*/
    private void setPrivilegeData(List<String> imgs) {
        AppCompatImageView iv;
        int[] ids = {R.id.iv_privilege1, R.id.iv_privilege2, R.id.iv_privilege3, R.id.iv_privilege4, R.id.iv_privilege5, R.id.iv_privilege6};
        if (null != imgs && imgs.size() > 0) {
            for (int i = 0; i < imgs.size(); i++) {
                iv = findViewById(ids[i]);
                GlideManager.loadRoundImg(imgs.get(i), iv);
            }
        }
    }


    @Override
    public void loadData() {
        super.loadData();
        isVip();

        select();
    }

    private void select() {
        ViseHttp.GET(ApiConstant.ORDER_TEMPLATE_SELECT)
                .addParam("type", "1")
                .request(new ACallback<BaseTResp2<List<SelectBean>>>() {
                    @Override
                    public void onSuccess(BaseTResp2<List<SelectBean>> data) {
                        Log.i(TAG, "onSuccess: ");
                        if (data.isSuccess()) {
                            Log.i(TAG, "onSuccess: " + data.getData());
                            createRecommendView(data.getData());
                        } else {
                            ToastUtil.show(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        Log.i(TAG, "onFail: " + errMsg);
                        ToastUtil.show(errMsg);
                    }
                });
    }

    private void createRecommendView(List<SelectBean> data) {
        if (null != data && data.size() > 0) {
            int index = 0;
            for (SelectBean bean : data) {
                LinearLayout itemView = (LinearLayout) View.inflate(mContext, R.layout.item_recommend, null);

                TextView tvYear = itemView.findViewById(R.id.tv_year);
                TextView tvPrice = itemView.findViewById(R.id.tv_price);
                TextView tvOldPrice = itemView.findViewById(R.id.tv_old_price);

                setRecommendData(bean.getName(), bean.getPrice(), bean.getOriginalPrice(), tvYear, tvPrice, tvOldPrice);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                params.weight = 1;
                itemView.setLayoutParams(params);

                itemView.setOnClickListener(view -> {
                    if (view != mOldView) {
                        mOldView.setBackgroundResource(R.drawable.shape_bg_recommend_nomar);
                        view.setBackgroundResource(R.drawable.shape_bg_recommend_select);
                        mOldView = view;
                        String text = ((TextView) view.findViewById(R.id.tv_year)).getText().toString();

                        for (SelectBean datum : data) {
                            if (text.equals(datum.getName())) {
                                setPrice(datum.getPrice());
                                mServiceId = datum.getId();
                            }
                        }
                    }
                });
                index++;
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) itemView.getLayoutParams();
                if (index == data.size()) {
                    layoutParams.setMargins(DisplayUtil.dip2px(16), 0, DisplayUtil.dip2px(16), 0);
                } else {
                    layoutParams.setMargins(DisplayUtil.dip2px(16), 0, 0, 0);
                }
                mLLRecommend.addView(itemView);
            }
            mOldView = mLLRecommend.getChildAt(0);
            mOldView.setBackgroundResource(R.drawable.shape_bg_recommend_select);
            SelectBean bean = data.get(0);
            setPrice(bean.getPrice());
            mServiceId = bean.getId();
        }
    }

    /*套餐模块-判断是否已开通vip套餐服务*/
    private void isVip() {
        ViseHttp.GET(ApiConstant.IS_VIP)
                .request(new ACallback<BaseTResp2<VipBean>>() {
                    @Override
                    public void onSuccess(BaseTResp2<VipBean> data) {
                        if (data.isSuccess()) {
                            VipBean bean = data.getData();
                            if (bean.isIsEnable()) {
                                mTvVip.setText("您是VIP会员");
                            } else {
                                mTvVip.setText("您还不是VIP会员");
                            }
                        } else {
                            ToastUtil.show(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });
    }

    /*订单模块-创建订单*/
    private void insert(int payType) {
        Map<String, String> map = new HashMap<>();
        map.put("serviceId", String.valueOf(mServiceId));
        map.put("amount", SPHelper.getStringSF(mContext, Constant.PRICE));
        map.put("payType", String.valueOf(payType));
        ViseHttp.POST(ApiConstant.INSERT)
                .setJson(new JSONObject(map))
                .request(new ACallback<BaseTResp2<InsertBean>>() {
                    @Override
                    public void onSuccess(BaseTResp2<InsertBean> data) {
                        if (data.isSuccess()) {
                            InsertBean bean = data.getData();
                            long orderId = bean.getId();
                            String orderNo = bean.getOrderNo();
                            switch (payType) {
                                case 1:
                                    pay(orderId, orderNo, payType);
                                    break;
                                case 2:
                                    pay(orderId, orderNo, payType);
                                    break;
                                default:
                                    ToastUtil.show("暂时没有这种支付类型");
                                    break;
                            }
                        } else {
                            ToastUtil.show(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });
    }

    /*支付*/
    private void pay(long orderId, String orderNo, int payType) {
        Map<String, String> map = new HashMap<>();
        map.put("orderId", String.valueOf(orderId));
        map.put("orderNo", orderNo);
        map.put("tradeType", "APP");
        map.put("payType", PAYTYPE[payType - 1]);
        ViseHttp.POST(ApiConstant.PAY)
                .setJson(new JSONObject(map))
                .request(new ACallback<BaseTResp2<PayBean>>() {
                    @Override
                    public void onSuccess(BaseTResp2<PayBean> data) {
                        if (data.isSuccess()) {
                            PayBean bean = data.getData();
                            String payUrl = bean.getPayUrl();
                            switch (payType) {
                                case 1:/*微信*/
                                    try {
                                        weiXinPay(new JSONObject(payUrl));
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case 2:
                                    try {
                                        JSONObject jsonObject = new JSONObject(payUrl);
                                        payUrl = jsonObject.getString("aliPayUrl");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    Log.i(TAG, "onSuccess: " + payUrl);
                                    zhiFuBaoPay(payUrl);
                                    break;
                            }
                        } else {
                            ToastUtil.show(data.getMsg());
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ToastUtil.show(errMsg);
                    }
                });

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
                        Log.i(TAG, "onClick: ");
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
                    insert(1);
                    break;
                case 2:/*支付宝*/
                    insert(2);
                    break;
                case 3:/*未选择*/
                    ToastUtil.show("请选择支付方式");
                    break;
            }
        });
    }

    private void zhiFuBaoPay(String param) {
        if (!TextUtils.isEmpty(getPrice())) {
            Context context = new Context(ZhiFuBaoStrategy.getInstance(this));
            Map<String, String> map = new HashMap<>();
            map.put("orderInfo", param);
            context.pay(map, jPayListener);
        }
    }

    private void weiXinPay(JSONObject jsonObject) {
        Context context = new Context(WeiXinBaoStrategy.getInstance(this));
        Map<String, String> map = new HashMap<>();

        try {
            String appId = jsonObject.getString("appId");
            String partnerId = jsonObject.getString("partnerId");
            String prepayId = jsonObject.getString("prepayId");
            String nonceStr = jsonObject.getString("nonceStr");
            String timeStamp = jsonObject.getString("timeStamp");
            String sign = jsonObject.getString("sign");
            map.put("appId", appId);
            map.put("partnerId", partnerId);
            map.put("prepayId", prepayId);
            map.put("nonceStr", nonceStr);
            map.put("timeStamp", timeStamp);
            map.put("sign", sign);
            context.pay(map, jPayListener);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JPayListener jPayListener = new JPayListener() {
        @Override
        public void onPaySuccess() {
            ToastUtils.showLong("支付成功");
        }

        @Override
        public void onPayError(int error_code, String message) {
            ToastUtils.showLong(error_code + " " + message);
        }

        @Override
        public void onPayCancel() {
            ToastUtils.showLong("支付取消");
        }

        @Override
        public void onUUPay(String dataOrg, String sign, String mode) {

        }
    };

    private String getPrice() {
        String price = SPHelper.getStringSF(mContext, Constant.PRICE, "");
        if (TextUtils.isEmpty(price)) {
            ToastUtil.show("请选择套餐推荐");
        }
        return price;
    }

    private void setPrice(String price) {
        mTvPrice.setText(String.format("￥%s", price));
        SPHelper.setStringSF(mContext, Constant.PRICE, price);
    }


    @Override
    public int getContentLayout() {
        return R.layout.activity_vip;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("VIP会员");
    }
}
