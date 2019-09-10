package com.ph.financa.fragments;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ph.financa.R;

import java.util.ArrayList;

import tech.com.commoncore.base.BaseFragment;

/**
 * 谁看了我
 */
public class SeeFragment extends BaseFragment implements View.OnClickListener {

    private String[] titles = {"分享", "转发", "访客", "留言"};
    private ArrayList<Fragment> mFragments;
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public int getContentLayout() {
        return R.layout.fragment_see;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mContentView.findViewById(R.id.tv_vip).setOnClickListener(this);
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

    class TabEntity implements CustomTabEntity {
        public String title;
        public int selectedIcon;
        public int unSelectedIcon;

        public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
            this.title = title;
            this.selectedIcon = selectedIcon;
            this.unSelectedIcon = unSelectedIcon;
        }

        @Override
        public String getTabTitle() {
            return title;
        }

        @Override
        public int getTabSelectedIcon() {
            return selectedIcon;
        }

        @Override
        public int getTabUnselectedIcon() {
            return unSelectedIcon;
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
        list.add(new ShareFragment());
        list.add(new ForwardingFragment());
        list.add(new VisitorsFragment());
        list.add(new MessageFragment());
        return list;
    }
}
