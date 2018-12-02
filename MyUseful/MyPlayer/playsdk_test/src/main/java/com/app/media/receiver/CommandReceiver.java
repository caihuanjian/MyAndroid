package com.app.media.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.app.media.vcr.VoiceController;

import cn.yinxm.playsdk.test.LogUtil;


public class CommandReceiver extends BroadcastReceiver {

    private static final String TAG = CommandReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            String action = intent.getAction();
            LogUtil.e(TAG, "接收到语音命令=" + action);
            VoiceController.getInstance().processVcrCmd(context, action, intent);
        }
    }
}
