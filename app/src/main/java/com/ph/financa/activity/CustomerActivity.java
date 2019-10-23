package com.ph.financa.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.aries.ui.view.title.TitleBarView;
import com.githang.statusbar.StatusBarCompat;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;
import com.ph.financa.R;
import com.ph.financa.constant.Constant;
import com.ph.financa.ease.FriendTable;
import com.ph.financa.utils.SoftKeyboardFixerForFullscreen;

import java.util.UUID;

import tech.com.commoncore.base.BaseTitleActivity;
import tech.com.commoncore.utils.DateUtil;
import tech.com.commoncore.utils.SPHelper;
import tech.com.commoncore.utils.SPUtil;

/**
 * 我的客服
 */
public class CustomerActivity extends BaseTitleActivity {

    @Override
    public void initView(Bundle savedInstanceState) {
        StatusBarCompat.setStatusBarColor(mContext, getResources().getColor(R.color.white));

        SoftKeyboardFixerForFullscreen.assistActivity(this);
//        AndroidBottomSoftBar.assistActivity(mContentView, this);

        Intent intent = getIntent();

        /*聊天*/
        EaseChatFragment chatFragment = new EaseChatFragment();
        Bundle args = new Bundle();
        if (intent.hasExtra(EaseConstant.EXTRA_USER_ID)) {
            String userId = intent.getStringExtra(EaseConstant.EXTRA_USER_ID);
            args.putString(EaseConstant.EXTRA_USER_ID, userId);
            if (!TextUtils.isEmpty(userId) && userId.equals(Constant.CUSTOMSERVICE)) {
                sendEmmessage();
                mTitleBar.setTitleMainText("我的客服");
            } else {
                EaseUser user = EaseUserUtils.getUserInfo(userId);
                if (user != null) {
                    mTitleBar.setTitleMainText(user.getNickname());
                }
            }
        }
        args.putString(Constant.USERID, SPHelper.getStringSF(mContext, Constant.USERID));
        chatFragment.setArguments(args);
        chatFragment.hideTitleBar();
        chatFragment.setChatFragmentHelper(mHelper);
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();
    }

    /*判断是否有客服信息*/
    private void sendEmmessage() {
        EMConversation conversation = EMClient.getInstance().chatManager().getConversation(Constant.CUSTOMSERVICE);
        if (conversation == null || conversation.getAllMessages().size() == 0) {
            String content = String.format("Hi，%s，%s好，想问什么尽管问哦~直接发送编辑好的问题过来，我会尽快给您答复！",
                    SPHelper.getStringSF(mContext, Constant.USERNAME), DateUtil.getTimePhase());
            EMMessage msg = EMMessage.createReceiveMessage(EMMessage.Type.CMD);
            msg.setChatType(EMMessage.ChatType.Chat);
            msg.setFrom(Constant.CUSTOMSERVICE);
            msg.setUnread(true);

            String head = "http://file.phscitech.com/dev/pic/banner/20191015/201910151445558570cd4633b040eca9e0d4a833bb9b4a.jpg";
            msg.setAttribute("nickName", "我的客服");
            msg.setAttribute("UserPortrait", head);

            SPUtil.put(mContext, msg.getFrom() + "name", "我的客服");
            SPUtil.put(mContext, msg.getFrom() + "head", head);

            msg.setMsgId(UUID.randomUUID().toString());
            msg.addBody(new EMTextMessageBody(content));
            // 保存邀请消息
            EMClient.getInstance().chatManager().saveMessage(msg);
//            DemoHelper.getInstance().getNotifier().notify(msg);

//            Log.i(TAG, "sendEmmessage: ");
        }
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