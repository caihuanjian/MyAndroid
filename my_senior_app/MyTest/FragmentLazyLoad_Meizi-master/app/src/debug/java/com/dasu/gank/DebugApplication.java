package com.dasu.gank;

import com.facebook.stetho.Stetho;
import com.squareup.picasso.Picasso;

/**
 * Created by dasu on 2016/9/30.
 * https://github.com/woshidasusu/Meizi
 */
public class DebugApplication extends GankApplication {

    private static final String TAG = DebugApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
//        ButterKnife.setDebug(true);
        Picasso.with(getApplicationContext())
                .setDebugging(true);

    }
}
