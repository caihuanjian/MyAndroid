package cn.yinxm.app.applist;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

/**
 * Created by yinxm on 2016/6/12.
 */
public class BaseApplication extends Application{
    @Override
    public void onCreate() {
        super.onCreate();
        startService(new Intent(this, MyService.class));
        Log.d("yinxm", "启动服务成功");
    }


}
