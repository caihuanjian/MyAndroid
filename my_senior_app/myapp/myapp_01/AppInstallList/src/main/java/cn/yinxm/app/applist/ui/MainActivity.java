package cn.yinxm.app.applist.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;

import cn.yinxm.lib.receiver.ApkInstalledReceiver;

public class MainActivity extends SingleFragmentActivity {
    private Fragment mFragment;

    @Override
    protected Fragment createFragment() {
//        return new AppListFragment();
        mFragment = AppInstallFragment.newInstance();
        return mFragment;
    }

    ApkInstalledReceiver mApkInstalledReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mApkInstalledReceiver = new ApkInstalledReceiver() {
            @Override
            public void onApkInstalled(String packageName) {
                ((AppInstallFragment) mFragment).loadData();
            }

            @Override
            public void onApkUninstalled(String packageName) {
                ((AppInstallFragment) mFragment).loadData();
            }
        };

        registerReceiver(mApkInstalledReceiver, mApkInstalledReceiver.getIntentFileter());

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mApkInstalledReceiver);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("yinxm", "keyCode=" + keyCode + ", event=" + event);
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Toast.makeText(MainActivity.this, "返回键无效", Toast.LENGTH_SHORT).show();
//            return true;//return true;拦截事件传递,从而屏蔽back键。
//        }
//        if (KeyEvent.KEYCODE_HOME == keyCode) {
//            Toast.makeText(getApplicationContext(), "HOME 键已被禁用...", Toast.LENGTH_SHORT).show();
//            return true;//同理
//        }
        return super.onKeyDown(keyCode, event);
    }
}