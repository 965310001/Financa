package com.ph.financa.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.hyphenate.chat.EMClient;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.ph.financa.R;
import com.ph.financa.activity.CustomerActivity;

import java.util.Map;

import tech.com.commoncore.base.BaseFragment;
import tech.com.commoncore.utils.FastUtil;

/**
 * 聊天记录
 */
public class ChatListFragment extends BaseFragment {

    private static final String ARG_PARAM1 = "param1";

    private String mParam1;
    private EaseConversationListFragment mFragment;

    private FrameLayout mContainer;
    private LinearLayout mLlBlank;

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
//        Log.i(TAG, "initView: ");
        mContainer = mContentView.findViewById(R.id.container);
        mLlBlank = mContentView.findViewById(R.id.ll_blank);

        getLastMessage();
    }

    @Override
    public void onResume() {
        super.onResume();
        /*getLastMessage();*/
    }

    private void getLastMessage() {
        mFragment = new EaseConversationListFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("TIME", mParam1);
        mFragment.setArguments(bundle1);
        mFragment.hideTitleBar();
        mFragment.setConversationListItemClickListener(conversation -> {
            try {
                Bundle bundle = new Bundle();
                /*conversation.conversationId();*/
                Map<String, Object> ext = conversation.getLastMessage().ext();
//                Log.i(TAG, "getLastMessage: " + ext);

//                if (conversation.getLastMessage().direct() == EMMessage.Direct.SEND &&
//                        conversation.conversationId().equals(SPHelper.getStringSF(mContext, Constant.USERID))) {
//                    if (null != ext.get("otherUserNickName")) {
//                        Log.i(TAG, "getLastMessage: " + ext.get("otherUserNickName").toString());/*她*/
//                        bundle.putString(FriendTable.FRIEND_NAME, ext.get("otherUserNickName").toString());
//                    } else {
//                        bundle.putString(FriendTable.FRIEND_NAME, "");
//                    }
//                    if (null != ext.get("otherUserPortrait")) {
//                        Log.i(TAG, "getLastMessage: " + ext.get("otherUserPortrait").toString());/*她*/
//
//                        bundle.putString(FriendTable.FRIEND_HEAD, ext.get("otherUserPortrait").toString());
//                    } else {
//                        bundle.putString(FriendTable.FRIEND_HEAD, "");
//                    }
//
//                } else {
//                    Log.i(TAG, "getLastMessage: 22");
//                    if (null != ext.get("nickName")) {
//                        bundle.putString(FriendTable.FRIEND_NAME, ext.get("nickName").toString());
//                        Log.i(TAG, "getLastMessage: " + ext.get("nickName").toString());/*我*/
//                    } else {
//                        bundle.putString(FriendTable.FRIEND_NAME, "");
//                    }
//                    if (null != ext.get("UserPortrait")) {
//                        Log.i(TAG, "getLastMessage: " + ext.get("UserPortrait").toString());/*我*/
//                        bundle.putString(FriendTable.FRIEND_HEAD, ext.get("UserPortrait").toString());
//                    } else {
//                        bundle.putString(FriendTable.FRIEND_HEAD, "");
//                    }
//                }
//                if (null != ext.get("otherUserNickName")) {
//                    Log.i(TAG, "getLastMessage: " + ext.get("otherUserNickName").toString());/*她*/
//                    bundle.putString(FriendTable.FRIEND_NAME, ext.get("otherUserNickName").toString());
//                } else {
//                    bundle.putString(FriendTable.FRIEND_NAME, "");
//                }
//                if (null != ext.get("otherUserPortrait")) {
//                    Log.i(TAG, "getLastMessage: " + ext.get("otherUserPortrait").toString());/*她*/
//                    bundle.putString(FriendTable.FRIEND_HEAD, ext.get("otherUserPortrait").toString());
//                } else {
//                    bundle.putString(FriendTable.FRIEND_HEAD, "");
//                }

                bundle.putString(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                FastUtil.startActivity(mContext, CustomerActivity.class, bundle);
            } catch (Exception e) {
                Log.i(TAG, "initView: " + e.toString());
            }
        });
        getChildFragmentManager().beginTransaction().replace(R.id.container, mFragment).commit();

        if (getUnreadMsgCountTotal() > 1) {
            mContainer.setVisibility(View.VISIBLE);
            mLlBlank.setVisibility(View.GONE);
        } else {
            mContainer.setVisibility(View.GONE);
            mLlBlank.setVisibility(View.VISIBLE);
        }
    }

    int getUnreadMsgCountTotal() {
        return EMClient.getInstance().chatManager().getAllConversations().size();
    }

    public void refresh() {
        if (null != mFragment) {
            mFragment.refresh();
            if (getUnreadMsgCountTotal() > 1) {
                mContainer.setVisibility(View.VISIBLE);
                mLlBlank.setVisibility(View.GONE);
            } else {
                mContainer.setVisibility(View.GONE);
                mLlBlank.setVisibility(View.VISIBLE);
            }
            /*getLastMessage();*/
        }
    }

    @Override
    public int getContentLayout() {
        return R.layout.fragment_chat_list;
    }
}