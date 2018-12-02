/*
 * Copyright (C) 2018 Baidu, Inc. All Rights Reserved.
 */
package cn.yinxm.app.sdk;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class RemoteChannelService extends Service {
    public static final int NOTIFICATION_ID = 1001;
    private RemoteChannelBinder mBinder;

    public RemoteChannelService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mBinder = new RemoteChannelBinder();
        // TODO: 2018/6/3 前台服务，避免被杀死
        startForeground(NOTIFICATION_ID, new Notification());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
}
