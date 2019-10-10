package com.ph.financa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;
import com.ph.financa.ease.FriendTable;
import com.ph.financa.utils.AndroidBug5497Workaround;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.utils.SPHelper;

/**
 * 我的客服
 */
public class CustomerActivity extends BaseTitleActivity {

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));
        AndroidBug5497Workaround.assistActivity(this);

        Intent intent = getIntent();
        if (intent.hasExtra(FriendTable.FRIEND_NAME)) {
            mTitleBar.setTitleMainText(intent.getStringExtra(FriendTable.FRIEND_NAME));
        } else {
            mTitleBar.setTitleMainText("我的客服");
        }
        /*聊天*/
        EaseChatFragment chatFragment = new EaseChatFragment();
        Bundle args = new Bundle();
        if (intent.hasExtra(EaseConstant.EXTRA_USER_ID)) {
            args.putString(EaseConstant.EXTRA_USER_ID, intent.getStringExtra(EaseConstant.EXTRA_USER_ID));
        } else {
            args.putString(EaseConstant.EXTRA_USER_ID, Constant.CUSTOMSERVICE);
        }
        args.putString(Constant.USERID, SPHelper.getStringSF(mContext, Constant.USERID));
        chatFragment.setArguments(args);
        chatFragment.hideTitleBar();
        chatFragment.setChatFragmentHelper(mHelper);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    private EaseChatFragment.EaseChatFragmentHelper mHelper = new EaseChatFragment.EaseChatFragmentHelper() {
        @Override
        public void onSetMessageAttributes(EMMessage message) {
            message.setAttribute("UserPortrait", SPHelper.getStringSF(mContext, Constant.USERHEAD, ""));
            message.setAttribute("nickName", SPHelper.getStringSF(mContext, Constant.USERNAME, ""));

            message.setAttribute("otherUserPortrait", getIntent().getStringExtra(FriendTable.FRIEND_HEAD));
            message.setAttribute("otherUserNickName", getIntent().getStringExtra(FriendTable.FRIEND_NAME));
        }

        @Override
        public void onEnterToChatDetails() {
        }

        @Override
        public void onAvatarClick(String username) {
        }

        @Override
        public void onAvatarLongClick(String username) {
        }

        @Override
        public boolean onMessageBubbleClick(EMMessage message) {
            return false;
        }

        @Override
        public void onMessageBubbleLongClick(EMMessage message) {
        }

        @Override
        public boolean onExtendMenuItemClick(int itemId, View view) {
            return false;
        }

        @Override
        public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
            return null;
        }
    };

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_customer;
    }
}