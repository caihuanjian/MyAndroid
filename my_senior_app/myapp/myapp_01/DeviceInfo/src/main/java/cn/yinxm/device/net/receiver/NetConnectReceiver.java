package cn.yinxm.device.net.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import cn.yinxm.lib.utils.NetworkUtil;
import cn.yinxm.lib.utils.log.LogUtil;


public class NetConnectReceiver extends BroadcastReceiver {
    private static final String TAG = "[NetConnectReceiver]";

    public NetConnectReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.d(TAG, "onReceive 网络状态改变");
        //初始化网络任务
        try {
            boolean isConnected = NetworkUtil.isNetworkConnected(context);
            LogUtil.d(TAG, "isConnected=" + isConnected);
            if (isConnected) {
                LogUtil.d(TAG, "work_ec 检测到网络已连接, isConnected=" + isConnected);

            }
        } catch (Exception e) {
            LogUtil.e(e);
        }

        try {
            boolean isWifiConnected = false;
            //获得网络连接服务
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            // State state = connManager.getActiveNetworkInfo().getState();
            // 获取WIFI网络连接状态
            NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState();
            // 判断是否正在使用WIFI网络
            if (NetworkInfo.State.CONNECTED == state) {
                isWifiConnected = true;
            } else {
                isWifiConnected = false;
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }
}
