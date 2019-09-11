package com.ph.financa.dialog;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import com.ph.financa.R;
import com.ph.financa.constant.Constant;

import tech.com.commoncore.utils.SPHelper;

/**
 * 支付
 */
public class PayDialog extends DialogFragment implements View.OnClickListener {

    private TextView mTvPrice;
    private RadioGroup mRgZhifuBao;
    private RadioGroup mRgWeiXin;
    private AppCompatImageView mIvZhiFuBao;
    private AppCompatImageView mIvWeiXin;

    public static PayDialog show(FragmentManager manager, String tag, OnClickIndex click) {
        PayDialog payDialog = new PayDialog();
        payDialog.show(manager, tag);
        payDialog.setClick(click);
        return payDialog;
    }

    private OnClickIndex click;

    public void setClick(OnClickIndex click) {
        this.click = click;
    }


    public interface OnClickIndex {
        void onIndex(int index);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_pay, container);
        view.findViewById(R.id.tv_pay).setOnClickListener(this);
        view.findViewById(R.id.iv_close).setOnClickListener(this);
        mTvPrice = view.findViewById(R.id.tv_price);
        mRgZhifuBao = view.findViewById(R.id.rg_zhifubao);
        mRgWeiXin = view.findViewById(R.id.rg_weixin);
        mIvZhiFuBao = view.findViewById(R.id.iv_zhifubao_complete);
        mIvWeiXin = view.findViewById(R.id.iv_weixin_complete);
        mTvPrice.setText(String.format("￥%s", SPHelper.getStringSF(getContext(), Constant.PRICE, "")));

        mRgZhifuBao.setOnClickListener(v -> {
            mIvZhiFuBao.setVisibility(View.VISIBLE);
            mIvWeiXin.setVisibility(View.GONE);
        });

        mRgWeiXin.setOnClickListener(v -> {
            mIvZhiFuBao.setVisibility(View.GONE);
            mIvWeiXin.setVisibility(View.VISIBLE);
        });
        return view;
    }

    @Override
    public void onClick(View view) {
        if (null != click) {
            int index = 0;
            switch (view.getId()) {
                case R.id.iv_close:
                    index = 0;
                    break;
                case R.id.tv_pay:
                    if (mIvWeiXin.getVisibility() == View.VISIBLE) {
                        index = 1;
                    } else if (mIvZhiFuBao.getVisibility() == View.VISIBLE) {
                        index = 2;
                    } else {
                        index = 3;
                    }
                    break;
            }
            click.onIndex(index);
        }
    }

    /*设置透明*/
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        /*windowParams.dimAmount = 0.0f;*/
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        /*window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);*/
        window.setGravity(Gravity.BOTTOM);
        window.setBackgroundDrawable(null);
        window.setAttributes(windowParams);
    }
}