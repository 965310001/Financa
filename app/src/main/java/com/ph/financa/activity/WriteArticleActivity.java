package com.ph.financa.activity;

import android.os.Bundle;

import com.aries.ui.view.title.TitleBarView;
import com.ph.financa.R;

import tech.com.commoncore.base.BaseTitleActivity;

/**
 * 写文章
 */
public class WriteArticleActivity extends BaseTitleActivity {

    @Override
    public void initView(Bundle savedInstanceState) {

    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
        titleBar.setTitleMainText("写文章");
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_write_article;
    }

}
