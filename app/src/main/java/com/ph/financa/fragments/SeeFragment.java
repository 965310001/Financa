package com.ph.financa.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.githang.statusbar.StatusBarCompat;
import com.ph.financa.R;
import com.ph.financa.activity.bean.TabEntity;
import com.ph.financa.constant.Constant;
import com.ph.financa.utils.StatusBarUtils;

import java.util.ArrayList;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.utils.DisplayUtil;
import tech.com.commoncore.utils.SPHelper;

/**
 * 谁看了我
 */
public class SeeFragment extends BaseFragment implements View.OnClickListener {

    private String[] titles = {"分享", "转发", "访客", "留言"};
    private ArrayList<Fragment> mFragments;
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected};
    private ArrayList<CustomTabEntity> mTabEntities;

    @Override
    public int getContentLayout() {
        return R.layout.fragment_see;
    }

    @Override
    protected void onVisibleChanged(boolean isVisibleToUser) {
        if (null != mContentView) {
            mContentView.setPadding(0, DisplayUtil.getStatusBarHeight(), 0, 0);
            StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));
            StatusBarUtils.immersive(getActivity(), true);
        }
        super.onVisibleChanged(isVisibleToUser);
        Log.i(TAG, "onVisibleChanged: ");
    }


    @Override
    public void initView(Bundle savedInstanceState) {
        View tvVip = mContentView.findViewById(R.id.tv_vip);
        tvVip.setOnClickListener(this);

        switch (SPHelper.getIntergerSF(mContext, Constant.ISVIP, 0)) {
            case 0:
                tvVip.setVisibility(View.GONE);
                break;
            case 1:
                tvVip.setVisibility(View.VISIBLE);
                break;
        }
        mTabEntities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], mIconSelectIds[0], mIconSelectIds[0]));
        }
        final CommonTabLayout mMainTab = mContentView.findViewById(R.id.tab_main);
        mFragments = getFragments();
        final ViewPager mViewPager = mContentView.findViewById(R.id.vp_see);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mMainTab.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mMainTab.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mMainTab.setTabData(mTabEntities, getActivity(), R.id.fl_see, getFragments());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_vip:
                // TODO: 2019/9/9
                break;
        }
    }


    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }

    private ArrayList<Fragment> getFragments() {
        ArrayList<Fragment> list = new ArrayList<>();
        list.add(new ShareFragment2());
        list.add(new ForwardingFragment2());
        list.add(new VisitorsFragment2());
        list.add(new MessageFragment());
        return list;
    }
}
