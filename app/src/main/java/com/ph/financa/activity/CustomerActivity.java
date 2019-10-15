package com.ph.financa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;
import com.ph.financa.ease.FriendTable;
import com.ph.financa.utils.AndroidBottomSoftBar;
import com.ph.financa.utils.AndroidBug5497Workaround;

import org.json.JSONObject;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.utils.SPHelper;

/**
 * 我的客服
 */
public class CustomerActivity extends BaseTitleActivity {

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));

        AndroidBottomSoftBar.assistActivity(mContentView, this);
        AndroidBug5497Workaround.assistActivity(this);

        Intent intent = getIntent();
//        if (intent.hasExtra(FriendTable.FRIEND_NAME)) {
////            mTitleBar.setTitleMainText(intent.getStringExtra(FriendTable.FRIEND_NAME));
//        } else {
//            mTitleBar.setTitleMainText("我的客服");
//        }

        /*聊天*/
        EaseChatFragment chatFragment = new EaseChatFragment();
        Bundle args = new Bundle();
        if (intent.hasExtra(EaseConstant.EXTRA_USER_ID)) {
            String userId = intent.getStringExtra(EaseConstant.EXTRA_USER_ID);
            args.putString(EaseConstant.EXTRA_USER_ID, userId);
            if (!TextUtils.isEmpty(userId) && userId.equals(Constant.CUSTOMSERVICE)) {
                /*sendEmmessage();*/
                mTitleBar.setTitleMainText("我的客服");
            } else {
                EaseUser user = EaseUserUtils.getUserInfo(userId);
                if (user != null) {
                    mTitleBar.setTitleMainText(user.getNickname());
                }
            }
        }
//        else {
//            sendEmmessage();
//            args.putString(EaseConstant.EXTRA_USER_ID, Constant.CUSTOMSERVICE);
//        }
        args.putString(Constant.USERID, SPHelper.getStringSF(mContext, Constant.USERID));
        chatFragment.setArguments(args);
        chatFragment.hideTitleBar();
        chatFragment.setChatFragmentHelper(mHelper);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    /*判断是否有客服信息*/
    private void sendEmmessage() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(SPHelper.getStringSF(mContext, Constant.CUSTOMSERVICE));
        if (conversation == null || conversation.getAllMessages().size() == 0) {
            String content = "Hi，{用户名}，{上午}好，想问什么尽管问哦~直接发送编辑好的问题过来，我会尽快给您答复！";
            EMMessage message = EMMessage.createTxtSendMessage(content, Constant.CUSTOMSERVICE);
            try {
                JSONObject weichat = new JSONObject();
                message.setDirection(EMMessage.Direct.RECEIVE);
                message.setAttribute("otherUserPortrait", SPHelper.getStringSF(mContext, Constant.USERHEAD));
                message.setAttribute("otherUserNickName", SPHelper.getStringSF(mContext, Constant.USERNAME));

                message.setAttribute("nickName", "我的客服");
                message.setAttribute("UserPortrait", "https://img01.sogoucdn.com/net/a/04/link?url=https%3A%2F%2Fi04piccdn.sogoucdn.com%2Ff658e707bfde45c5&appid=122");

                JSONObject visitor = new JSONObject();
                visitor.put("userNickname", "aa");
                visitor.put("trueName", "aa");
                visitor.put("phone", "15949629525");
                visitor.put("description", "15949629525");
                visitor.put("email", "15949629525");
                weichat.put("visitor", visitor);
                weichat.put("queueName", "15949629525");
//                    message.setAttribute("visitor",visitor);
                message.setAttribute("weichat", weichat);
                Log.i(TAG, "sendEmmessage: ");
                EMClient.getInstance().chatManager().sendMessage(message);
            } catch (Exception e) {
                Log.i(TAG, "sendEmmessage: " + e.toString());
            }
        }
    }

    private EaseChatFragment.EaseChatFragmentHelper mHelper = new EaseChatFragment.EaseChatFragmentHelper() {
        @Override
        public void onSetMessageAttributes(EMMessage message) {
//            message.getFrom();
//            message.getTo();
//            Log.i(TAG, "onSetMessageAttributes: " + message.getFrom() + " " + message.getTo());/*获取消息发送者的用户名   接受者*/

//            if (message.getFrom().equals(SPHelper.getStringSF(mContext, Constant.USERID))) {
//            }

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