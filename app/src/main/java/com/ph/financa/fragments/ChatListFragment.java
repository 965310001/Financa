package com.ph.financa.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.ph.financa.R;
import com.ph.financa.activity.CustomerActivity;
import com.ph.financa.constant.Constant;
import com.ph.financa.ease.FriendTable;

import java.util.Map;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.utils.FastUtil;
import tech.com.commoncore.utils.SPHelper;

/**
 * 聊天记录
 */
public class ChatListFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;

    public ChatListFragment() {
    }

    public static ChatListFragment newInstance(String param1) {
        ChatListFragment fragment = new ChatListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        Log.i(TAG, "initView: ");
        getLastMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        getLastMessage();
    }

    private void getLastMessage() {
        Map<String, EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        if (conversations.size() > 1) {
            EaseConversationListFragment fragment = new EaseConversationListFragment();
            fragment.hideTitleBar();
            fragment.setConversationListItemClickListener(conversation -> {
                try {
                    Bundle bundle = new Bundle();
                    Map<String, Object> ext = conversation.getLastMessage().ext();
                    Log.i(TAG, "getLastMessage: " + ext.get("UserPortrait").toString());/*我*/
                    Log.i(TAG, "getLastMessage: " + ext.get("nickName").toString());/*我*/

                    Log.i(TAG, "getLastMessage: " + ext.get("otherUserNickName").toString());/*她*/
                    Log.i(TAG, "getLastMessage: " + ext.get("otherUserPortrait").toString());/*她*/

                    if (conversation.getLastMessage().direct() == EMMessage.Direct.SEND &&
                            conversation.conversationId().equals(SPHelper.getStringSF(mContext, Constant.USERID))) {

                        bundle.putString(FriendTable.FRIEND_NAME, ext.get("UserPortrait").toString());
                        bundle.putString(FriendTable.FRIEND_HEAD, ext.get("nickName").toString());
//                GlideManager.loadCircleImg(map.get("otherUserPortrait").toString(), userAvatarView);
//                EaseUserUtils.setUserNick(map.get("otherUserNickName").toString(), usernickView);

//                        holder.name.setText(map.get("otherUserNickName").toString());
//                        otherUserPortrait = map.get("otherUserPortrait").toString();
                    } else {
                        bundle.putString(FriendTable.FRIEND_NAME, ext.get("otherUserNickName").toString());
                        bundle.putString(FriendTable.FRIEND_HEAD, ext.get("otherUserPortrait").toString());

//                GlideManager.loadCircleImg(map.get("UserPortrait").toString(), userAvatarView);
//                /*Glide.with(getContext()).load(map.get("otherUserPortrait").toString()).into(userAvatarView);*/
//                EaseUserUtils.setUserNick(map.get("nickName").toString(), usernickView);

//                        holder.name.setText(map.get("nickName").toString());
//                        otherUserPortrait = map.get("UserPortrait").toString();
                    }


//                    bundle.putString(FriendTable.FRIEND_NAME, ext.get("otherUserNickName").toString());
//                    bundle.putString(FriendTable.FRIEND_HEAD, ext.get("otherUserPortrait").toString());
                    bundle.putString(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                    FastUtil.startActivity(mContext, CustomerActivity.class, bundle);
                } catch (Exception e) {
                    Log.i(TAG, "initView: " + e.toString());
                }
            });
            mContentView.findViewById(R.id.container).setVisibility(View.VISIBLE);
            mContentView.findViewById(R.id.ll_blank).setVisibility(View.GONE);
            getChildFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
        } else {
            mContentView.findViewById(R.id.container).setVisibility(View.GONE);
            mContentView.findViewById(R.id.ll_blank).setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_chat_list;
    }
}