package com.ph.financa.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.hyphenate.easeui.utils.GlideManager;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;
import com.zhengsr.viewpagerlib.bean.PageBean;
import com.zhengsr.viewpagerlib.callback.PageHelperListener;
import com.zhengsr.viewpagerlib.view.GlideViewPager;

import java.util.ArrayList;
import java.util.List;

import tech.com.commoncore.base.BaseActivity;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;

/**
 * 引导页
 */
public class WelcomeGuideActivity extends BaseActivity {

    private Button mBtnStart;

    @Override
    public void initView(Bundle savedInstanceState) {
        GlideViewPager bannerViewPager = findViewById(R.id.gvp_banner);
        mBtnStart = findViewById(R.id.btn_start);
        mBtnStart.setOnClickListener(v -> {
            SPHelper.setBooleanSF(mContext, Constant.ISGUIDE, true);
            FastUtil.startActivity(mContext, LoginActivity.class);
        });


        List<Integer> images = new ArrayList<>();
        images.add(R.mipmap.ic_bg_guide1);
        images.add(R.mipmap.ic_bg_guide2);
        images.add(R.mipmap.ic_bg_guide3);
        images.add(R.mipmap.ic_bg_guide4);
        PageBean bean = new PageBean.Builder<Integer>()
                .setDataObjects(images)
                .setOpenView(mBtnStart)
                .builder();
        bannerViewPager.setPageListener(bean, R.layout.image_layout, (PageHelperListener<Integer>) (view, data) -> {
            //通过获取到这个view，你可以随意定制你的内容
            ImageView imageView = view.findViewById(R.id.iv_bg);
            /*imageView.setImageResource(data);*/
            GlideManager.loadImg(data, imageView);
        });

        bannerViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 3) {
                    mBtnStart.setVisibility(View.VISIBLE);
                } else {
                    mBtnStart.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_guide;
    }


}
