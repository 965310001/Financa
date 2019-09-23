package com.ph.financa.fragments;

import android.os.Bundle;
import android.util.Log;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.ph.financa.R;
import com.ph.financa.activity.CustomerActivity;
import com.ph.financa.activity.bean.TabEntity;
import com.ph.financa.ease.FriendTable;

import java.util.ArrayList;
import java.util.Map;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.utils.FastUtil;

/**
 * 留言
 */
public class MessageFragment extends BaseFragment {

    private CommonTabLayout mTabLayoutMessage;

    private String[] titles = {"本周", "上周", "一月内", "半年内"};
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected};

    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    @Override
    public void initView(Bundle savedInstanceState) {

        mTabLayoutMessage = mContentView.findViewById(R.id.tab_message);

        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], mIconSelectIds[0], mIconSelectIds[0]));
        }
        mTabLayoutMessage.setTabData(mTabEntities);

        EaseConversationListFragment fragment = new EaseConversationListFragment();
        fragment.hideTitleBar();
        fragment.setConversationListItemClickListener(conversation -> {
            Bundle bundle = new Bundle();
            Map<String, Object> ext = conversation.getLastMessage().ext();
            bundle.putString(FriendTable.FRIEND_NAME, ext.get("otherUserNickName").toString());
            bundle.putString(FriendTable.FRIEND_HEAD, ext.get("otherUserPortrait").toString());
            bundle.putString(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
            FastUtil.startActivity(mContext, CustomerActivity.class, bundle);
        });
        getChildFragmentManager().beginTransaction().add(R.id.container, fragment).commit();

        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        Log.i(TAG, "initView:聊天记录 " + conversations.size());
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_message;
    }
}
