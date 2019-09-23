package com.ph.financa.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.ph.financa.constant.Constant;

public class MessageReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            if (Constant.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(Constant.KEY_MESSAGE);
                String extras = intent.getStringExtra(Constant.KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(Constant.KEY_MESSAGE + " : " + messge + "\n");
                if (!TextUtils.isEmpty(extras)) {
                    showMsg.append(Constant.KEY_EXTRAS + " : " + extras + "\n");
                }
//                setCostomMsg(showMsg.toString());
            }
        } catch (Exception e) {
        }
    }
}