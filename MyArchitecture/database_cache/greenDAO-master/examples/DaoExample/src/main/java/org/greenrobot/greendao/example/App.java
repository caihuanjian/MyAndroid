package org.greenrobot.greendao.example;

import android.app.ActivityManager;
import android.app.Application;
import android.util.Log;

import org.greenrobot.greendao.example.model.db.DBManager;

import java.util.List;


public class App extends Application {

    public static App instance;

    DBManager mDBManager;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (isMainProcess()) {
            mDBManager = DBManager.getInstance().initDb(this);
        }
    }

    public static App getInstance() {
        return instance;
    }

    /**
     * 判断是否是主进程
     *
     * @return
     */
    public boolean isMainProcess() {
        boolean flag = false;

        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> list = activityManager.getRunningAppProcesses();
        int myPid = android.os.Process.myPid();
        String mainProcessName = getPackageName();
        if (list != null && list.size() > 0 && mainProcessName != null) {
            for (ActivityManager.RunningAppProcessInfo info : list) {
                if (info != null && myPid == info.pid && mainProcessName.equals(info.processName)) {
                    flag = true;
                    break;
                }
            }
        }
        Log.d("yinxm", "isMainProcess=" + flag);
        return flag;
    }
}
