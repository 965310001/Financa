package com.ph.financa.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatEditText;

import com.aries.ui.view.title.TitleBarView;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;
import com.ph.financa.ease.FriendTable;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.utils.SPHelper;

/**
 * 我的客服
 */
public class CustomerActivity extends BaseTitleActivity {

    @Override
    public void initView(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent.hasExtra("title")) {
            mTitleBar.setTitleMainText(intent.getStringExtra("title"));
        } else {
            mTitleBar.setTitleMainText("我的客服");
        }

       /* String id;
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id");
        } else {
            id = SPHelper.getStringSF(mContext, Constant.USERID, "");
        }*/

        TextView mTvSend = findViewById(R.id.tv_send);
        AppCompatEditText mEtInput = findViewById(R.id.et_input);
        mEtInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    mTvSend.setBackgroundResource(R.drawable.shape_send_selector);
                    mTvSend.setTextColor(Color.parseColor("#FFFFFF"));
                } else {
                    mTvSend.setBackgroundResource(R.drawable.shape_send_nomal);
                    mTvSend.setTextColor(Color.parseColor("#BBBBBB"));
                }
            }
        });
        // TODO: 2019/9/12 聊天
        EaseChatFragment chatFragment = new EaseChatFragment();
        Bundle args = new Bundle();
        if (intent.hasExtra(EaseConstant.EXTRA_USER_ID)) {
            args.putString(EaseConstant.EXTRA_USER_ID, intent.getStringExtra(EaseConstant.EXTRA_USER_ID));
        } else {
            args.putString(EaseConstant.EXTRA_USER_ID, Constant.CUSTOMSERVICE);
        }
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

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_send:
                break;
        }
    }

    @Override
    public void setTitleBar(TitleBarView titleBar) {
    }

    @Override
    public int getContentLayout() {
        return R.layout.activity_customer;
    }

}
