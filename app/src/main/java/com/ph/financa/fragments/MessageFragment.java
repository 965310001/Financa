package com.ph.financa.fragments;

import android.os.Bundle;

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
//            Intent intent = new Intent(mContext, ChatMsgActivity2.class);
//            try {
//                Map<String, Object> map = conversation.getLastMessage().ext();
//                intent.putExtra(FriendTable.FRIEND_NAME, map.get("otherUserNickName").toString());
//                intent.putExtra(FriendTable.FRIEND_HEAD, map.get("otherUserPortrait").toString());
//                intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            startActivity(intent);


            Bundle bundle = new Bundle();
            Map<String, Object> ext = conversation.getLastMessage().ext();
            bundle.putString(FriendTable.FRIEND_NAME, ext.get("otherUserNickName").toString());
            bundle.putString(FriendTable.FRIEND_HEAD, ext.get("otherUserPortrait").toString());
            bundle.putString(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
            FastUtil.startActivity(mContext, CustomerActivity.class, bundle);
        });
        getFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }

    @Override
    public void loadData() {
        super.loadData();

    }
}
