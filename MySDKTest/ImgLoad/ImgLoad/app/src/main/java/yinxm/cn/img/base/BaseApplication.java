package yinxm.cn.img.base;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * 功能：
 * Created by yinxm on 2018/1/9.
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
