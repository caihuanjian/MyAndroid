package com.yinxm.usb;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

public class UsbReceiver extends BroadcastReceiver {
    private static final String TAG = "UsbReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }
        String action = intent.getAction();
        Log.d(TAG, "UsbReceiver action=" + action);
        Toast.makeText(context, "UsbReceiver=" + action, Toast.LENGTH_SHORT).show();
        switch (action) {
            case Intent.ACTION_BOOT_COMPLETED:
                //如果是开机完成，则需要调用另外的方法获取U盘的路径
                break;
            case Intent.ACTION_MEDIA_MOUNTED:
                String mountPath = intent.getData().getPath();
                Log.d(TAG, "mountPath = " + mountPath);
                if (!TextUtils.isEmpty(mountPath)) {
                    //读取到U盘路径再做其他业务逻辑

                }
                Toast.makeText(context, "检测到U盘插入=" + mountPath, Toast.LENGTH_LONG).show();
                break;
            case Intent.ACTION_MEDIA_UNMOUNTED:
            case Intent.ACTION_MEDIA_EJECT:
                Toast.makeText(context, "No services information detecteMedid !", Toast.LENGTH_SHORT).show();
                break;
            case Intent.ACTION_MEDIA_SCANNER_FINISHED:
                Toast.makeText(context, "ACTION_MEDIA_SCANNER_FINISHED", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
