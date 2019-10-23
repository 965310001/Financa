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
        mContainer = mContentView.findViewById(R.id.container);
        mLlBlank = mContentView.findViewById(R.id.ll_blank);

        getLastMessage();
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