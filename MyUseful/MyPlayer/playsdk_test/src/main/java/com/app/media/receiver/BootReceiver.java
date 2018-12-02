
package com.app.media.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.yinxm.playsdk.test.LogUtil;

/**
 * Created by lishicong on 2017/5/15.
 */

public class BootReceiver extends BroadcastReceiver {

    private static final String TAG = "BootReceiver";

    private static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private static final String CODRIVER_CUSTOM_START = "com.app.media.START";
    private static final String CAR_RADIO_START = "com.app.media..START";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.i("onReceive");
        if (intent == null) {
            return;
        }

        LogUtil.i("BootReceiver：" + intent.getAction());
        if (intent.getAction().equals(BOOT_COMPLETED)) {
            LogUtil.i("Action: " + BOOT_COMPLETED);
            String format = new SimpleDateFormat("hh:mm:ss").format(new Date(System.currentTimeMillis()));
            Log.e(TAG, "开机  === 收到广播了 === 时间 ： " + format);
        } else if (intent.getAction().equals(CODRIVER_CUSTOM_START)) {
            LogUtil.i("Action: " + CODRIVER_CUSTOM_START);
        }
    }

}
