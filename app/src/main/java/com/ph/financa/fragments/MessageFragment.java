package com.ph.financa.fragments;

import android.os.Bundle;
import android.util.Log;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.ph.financa.R;
import com.ph.financa.activity.CustomerActivity;
import com.ph.financa.ease.FriendTable;

import java.util.Map;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.utils.FastUtil;

/**
 * 留言
 */
public class MessageFragment extends BaseFragment {

    @Override
    public int getContentLayout() {
        return R.layout.fragment_message;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
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
        Log.i(TAG, "initView:聊天记录 "+conversations.size());
    }
}
