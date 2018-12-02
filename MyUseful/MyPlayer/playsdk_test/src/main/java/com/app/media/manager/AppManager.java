package com.app.media.manager;

import android.content.Context;

/**
 * Created by yinxm on 2016/8/11.
 */
public  class AppManager {
    private AppManager(){}
    private Context applicationContext;


    public static AppManager getInstance() {
        return AppManagerFactory.instance;
    }
    private static class AppManagerFactory{
        private static AppManager instance = new AppManager();
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }


}
