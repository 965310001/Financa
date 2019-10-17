package com.ph.financa.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;

import com.aries.ui.view.title.TitleBarView;
import com.github.barteksc.pdfviewer.PDFView;
import com.ph.financa.R;
import com.ph.financa.activity.bean.DataBaseBean;

import butterknife.BindView;
import tech.com.commoncore.base.BaseTitleActivity;

/**
 * pdf
 */
public class PdfActivity extends BaseTitleActivity {

    @BindView(R.id.pf)
    PDFView mPf;

    private DataBaseBean mData;

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.hasExtra("data")) {
            mData = (DataBaseBean) intent.getSerializableExtra("data");

            String path = mData.getPath();
            if (!TextUtils.isEmpty(path)) {
                mPf.fromUri(Uri.parse(path));
            }
            mTitleBar.setTitleMainText(mData.getTitle());
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_pdf;
    }


}
