package cn.yinxm.playsdk.test;

import android.app.Application;

import com.app.media.sdk.MediaPlaySdk;

/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/23
 */
public class App extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        MediaPlaySdk.init(getApplicationContext());
    }
}
