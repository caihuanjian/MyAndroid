package cn.yinxm.app.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import cn.yinxm.lib.utils.log.LogUtil;


/**
 * 功能：Activity重要生命周期监视；Activity工具类方法
 * 查看时使用BaseActivityUtil筛选log
 *
 */

public class BaseActivityUtil extends FragmentActivity {
    public static final String TAG = BaseActivityUtil.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onCreate  savedInstanceState=" + savedInstanceState);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onStart ");
        super.onStart();
    }

    @Override
    protected void onRestart() {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onRestart ");
        super.onRestart();
    }

    @Override
    protected void onResume() {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onResume ");
        super.onResume();
    }

    @Override
    protected void onPause() {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onPause ");
        super.onPause();
    }

    @Override
    protected void onStop() {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onStop ");
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onDestroy ");
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onNewIntent  intent=" + intent);
        super.onNewIntent(intent);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        LogUtil.d(TAG, getClass().getSimpleName() + "——》onWindowFocusChanged  hasFocus=" + hasFocus);
        super.onWindowFocusChanged(hasFocus);
    }

    /**
     * 简化findViewById
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T findView(@IdRes int viewId) {
        return (T)findViewById(viewId);
    }

//    /**
//     * 加载页展示
//     */
//    public void loadingShow() {
//        LoadingUtils.getInstances().showDialog();
//    }
//
//    /**
//     * 加载页隐藏
//     */
//    public void loadingDissmiss() {
//        LogUtil.d("BaseActivityUtil.loadingDissmiss");
//        LoadingUtils.getInstances().cancel();
//    }


}
