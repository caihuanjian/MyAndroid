package com.app.media.manager;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.app.media.service.MediaPlayService;


/**
 * 播放Service实例全局管理器
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/28
 */
public class MediaPlayServiceManger implements ServiceConnection {

    private MediaPlayService mService;


    private MediaPlayServiceManger() {
    }

    public static MediaPlayServiceManger getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static MediaPlayServiceManger INSTANCE = new MediaPlayServiceManger();
    }


    public MediaPlayService getMediaPlayService() {
        // TODO: 2018/8/29  
        return mService;
    }


    public void startAndBindService(Context ctx) {
        Context context = ctx.getApplicationContext();
        Intent intent = new Intent(context, MediaPlayService.class);
        context.startService(intent);
        context.bindService(intent, this, 0);
    }


    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        // TODO: 2018/8/29
        if (service != null && service instanceof MediaPlayService.MediaPlayBinder) {
            mService = ((MediaPlayService.MediaPlayBinder) service).getService();

            // 检查是否有wait action
            PlayControlManager.getInstance().getPlayQueueController().processWaitAction();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    @Override
    public void onBindingDied(ComponentName name) {

    }
}
