package cn.yinxm.language.test;

import android.app.Application;
import android.util.Log;

/**
 * 功能：切换语言，Application不会重建，Activity会重建
 * Created by yinxm on 2017/11/24.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("yinxm","BaseApplication="+this);
    }
}
