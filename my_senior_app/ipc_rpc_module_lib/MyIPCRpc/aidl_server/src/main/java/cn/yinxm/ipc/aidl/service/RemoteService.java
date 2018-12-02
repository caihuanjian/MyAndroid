package cn.yinxm.ipc.aidl.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import cn.yinxm.ipc.aidl.IRemoteCallback;
import cn.yinxm.ipc.aidl.IRemoteServerInterface;

public class RemoteService extends Service {
    private static final String TAG = "yinxm";
    private String mData = "默认数据";
    boolean isRunning = false;

    private Handler mHandler = new Handler();

    //client回调集合，server端实时推送
    private RemoteCallbackList<IRemoteCallback> remoteCallbackList = new RemoteCallbackList<>();

    public RemoteService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new RemoteBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        new Thread(new Runnable() {
            @Override
            public void run() {
                isRunning = true;
                while (isRunning) {//实时监测状态
                    Log.d(TAG, "mData=" + mData);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "RemoteService.onStartCommand intent=" + intent + ", flags=" + flags + ", startId=" + startId);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isRunning = false;
    }

    class RemoteBinder extends IRemoteServerInterface.Stub {
//               @Override
//            public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {
//
//            }

        @Override
        public void setData(final String data) throws RemoteException {
            mData = data;
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //获取进程回调接口列表，广播发送给每个client
                    int count = remoteCallbackList.beginBroadcast();
                    for (int i=0 ;i <count; i++) {
                        try {
                            remoteCallbackList.getBroadcastItem(i).onResponse("Server端已收到消息="+data);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                    remoteCallbackList.finishBroadcast();
                }
            }, 1000);
        }

        @Override
        public String getData() throws RemoteException {
            return mData;
        }

        @Override
        public void regRemoteCallback(IRemoteCallback callback) throws RemoteException {
            remoteCallbackList.register(callback);
        }

        @Override
        public void unregRemoteCallback(IRemoteCallback callback) throws RemoteException {
            remoteCallbackList.unregister(callback);
        }
    }
}
