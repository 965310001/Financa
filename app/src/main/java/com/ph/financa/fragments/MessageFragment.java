package com.ph.financa.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.ph.financa.R;
import com.ph.financa.activity.bean.TabEntity;

import java.util.ArrayList;

import tech.com.commoncore.base.BaseFragment;

/**
 * 留言
 */
public class MessageFragment extends BaseFragment {

    private CommonTabLayout mTabLayoutMessage;

    private String[] titles = {"本周", "上周", "一月内", "半年内"};

    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {
        mTabLayoutMessage = mContentView.findViewById(R.id.tab_message);
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], R.mipmap.ic_home_selected, R.mipmap.ic_home_selected));
            mFragments.add(ChatListFragment.newInstance(titles[i]));
        }

        final ViewPager mViewPager = mContentView.findViewById(R.id.vp);
        MyPagerAdapter mAdapter = new MyPagerAdapter(getChildFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mFragments.size());
        mTabLayoutMessage.setOnTabSelectListener(new OnTabSelectListener() {
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
                mTabLayoutMessage.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mTabLayoutMessage.setTabData(mTabEntities);


        //        mMainTab.setTabData(mTabEntities, getActivity(), R.id.fl_see, getFragments());
        ///
//        EaseConversationListFragment fragment = new EaseConversationListFragment();
//        fragment.hideTitleBar();
//        fragment.setConversationListItemClickListener(conversation -> {
//            try {
//                Bundle bundle = new Bundle();
//                Map<String, Object> ext = conversation.getLastMessage().ext();
//                bundle.putString(FriendTable.FRIEND_NAME, ext.get("otherUserNickName").toString());
//                bundle.putString(FriendTable.FRIEND_HEAD, ext.get("otherUserPortrait").toString());
//                bundle.putString(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
//                FastUtil.startActivity(mContext, CustomerActivity.class, bundle);
//            } catch (Exception e) {
//                Log.i(TAG, "initView: " + e.toString());
//            }
//        });
//        getChildFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
//
//        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
//        Log.i(TAG, "initView:聊天记录 " + conversations.size());
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

    @Override
    public int getContentLayout() {
        return R.layout.fragment_message;
    }
}