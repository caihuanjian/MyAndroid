package cn.yinxm.app.applist.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import cn.yinxm.lib.receiver.ApkInstalledReceiver;

public class MainActivity extends SingleFragmentActivity {
    private Fragment mFragment;

	@Override
    protected Fragment createFragment() {
//        return new AppListFragment();
        mFragment =  AppInstallFragment.newInstance();
        return mFragment;
    }

    ApkInstalledReceiver mApkInstalledReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApkInstalledReceiver = new ApkInstalledReceiver(){
            @Override
            public void onApkInstalled(String packageName) {
                ((AppInstallFragment)mFragment).loadData();
            }

            @Override
            public void onApkUninstalled(String packageName) {
                ((AppInstallFragment)mFragment).loadData();
            }
        };

        registerReceiver(mApkInstalledReceiver, mApkInstalledReceiver.getIntentFileter());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mApkInstalledReceiver);
    }
}
