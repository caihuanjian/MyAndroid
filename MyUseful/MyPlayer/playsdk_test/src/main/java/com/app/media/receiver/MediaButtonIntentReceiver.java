
package com.app.media.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;

import com.app.media.sdk.MediaConstant;
import com.app.media.service.MediaPlayService;

import cn.yinxm.playsdk.test.LogUtil;


public class MediaButtonIntentReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        KeyEvent keyEvent = intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);

        if (Intent.ACTION_MEDIA_BUTTON.equals(action)) {
            int keyCode = keyEvent.getKeyCode();
            int keyAction = keyEvent.getAction();
            long eventTime = keyEvent.getEventTime();

            LogUtil.i("keyCode:" + keyCode + ",keyAction:" + keyAction + ",eventTime:" + eventTime);

            int command = -1;
            if (KeyEvent.ACTION_DOWN == keyAction) {
                if (KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE == keyCode) {
                    LogUtil.i("KEYCODE_MEDIA_PLAY_PAUSE");
                    command = keyCode;
                }
                if (KeyEvent.KEYCODE_MEDIA_PREVIOUS == keyCode) {
                    LogUtil.i("KEYCODE_MEDIA_PREVIOUS");
                    command = keyCode;
                }
                if (KeyEvent.KEYCODE_MEDIA_NEXT == keyCode) {
                    LogUtil.i("KEYCODE_MEDIA_NEXT");
                    command = keyCode;
                }
                if (KeyEvent.KEYCODE_MEDIA_STOP == keyCode) {
                    LogUtil.i("KEYCODE_MEDIA_STOP");
                    command = keyCode;
                }
                if (KeyEvent.KEYCODE_HEADSETHOOK == keyCode) {
                    LogUtil.i("KEYCODE_HEADSETHOOK");
                    command = keyCode;
                }
            }

            if (command != -1) {
                Intent i = new Intent(context, MediaPlayService.class);
                i.putExtra(MediaConstant.COMMAND_CMDNAME, command);
                context.startService(i);
            }
        }
    }
}
