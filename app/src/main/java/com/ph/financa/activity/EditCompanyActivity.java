package com.ph.financa.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

import com.aries.ui.view.title.TitleBarView;
import com.ph.financa.MainActivity;
import com.ph.financa.R;
import com.ph.financa.activity.bean.BaseTResp2;
import com.vise.xsnow.http.ViseHttp;
import com.vise.xsnow.http.callback.ACallback;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.constant.ApiConstant;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.ToastUtil;

/**
 * 填写你的公司、机构名称
 */
public class EditCompanyActivity extends BaseTitleActivity {

    private AppCompatEditText mEtCompany;

    @Override
    public int getContentLayout() {
        return R.layout.activity_edit_company;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mEtCompany = findViewById(R.id.et_company);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_complete:/*完成*/
                if (checkCompany()) {
                    doGet();
                }
                break;
            case R.id.tv_jump:/*跳过输入*/
                goMainActivity();
                break;
        }
    }

    private void doGet() {
        ViseHttp.POST(ApiConstant.EDIT_COMPANY)
                .addParam("companyName", getCompany())
                .request(new ACallback<BaseTResp2>() {
                    @Override
                    public void onSuccess(BaseTResp2 data) {
                        if (data.isSuccess()) {
                            goMainActivity();
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

    private void goMainActivity() {
        /*SPHelper.setBooleanSF(mContext, Constant.ISCOMPANY, true);*/
        FastUtil.startActivity(mContext, MainActivity.class);
    }

    private String getCompany() {
        return mEtCompany.getText().toString().trim();
    }

    private boolean checkCompany() {
        String company = mEtCompany.getText().toString().trim();
        if (TextUtils.isEmpty(company)) {
            ToastUtil.show("请填写正确的公司/机构名称");
            return false;
        }
        return true;
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {

    }
}