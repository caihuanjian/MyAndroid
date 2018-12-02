package cn.yinxm.app.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.multidex.MultiDex;

import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;

import cn.yinxm.app.BuildConfig;
import cn.yinxm.lib.api.manager.AppActivityManager;
import cn.yinxm.lib.utils.log.LogUtil;

/**
 * 功能：Application实现类
 * Created by yinxm on 2017/11/14.
 */

public class BaseApplication extends BaseApplicationUtil{
    private static final String TAG = "BaseApplication";

    private static BaseApplication instance;
    /**
     * 用来存放内存数据和页面跳转时不能互传的数据
     * 注意使用后要删除数据
     */
    public static WeakHashMap<String,Objects> weakHashMap=new WeakHashMap<>();

    @Override
    public void onCreate() {
        super.onCreate();

            instance = this;
        if (isMainProcess()) {
            //bugly初始化
            initBugly();
        }
        //activity生命周期管理
        initActivityLifecycleCallbacks();
    }

    private void initBugly() {
//        CrashReport.initCrashReport(getApplicationContext(), "89e47eb440", BuildConfig.BUGLY_DEBUG);//发布时false，测试true
        //集成升级sdk后统一初始化方法
        Bugly.init(getApplicationContext(), "89e47eb440", BuildConfig.BUGLY_DEBUG);
    }

    @Override
    protected void initLoginInfo() {

        // TODO: 2017/11/15 初始化登录状态
/*        boolean isLogin = LoginUtil.isLogin();
        LogUtil.d(TAG, "isLogin=" + isLogin);
        if (isLogin) {
            //获取登录信息
            ILoginInfo loginInfo = AppManager.getInstance().getLoginInfo();
            ((DefaultLoginInfoIml) loginInfo).setLogin(isLogin);
            String loginId = LoginUtil.getLoginId();
            ((DefaultLoginInfoIml) loginInfo).setLoginId(loginId);
            String userId = LoginUtil.getUserId();
            ((DefaultLoginInfoIml) loginInfo).setUserId(userId);
            String mark = LoginUtil.getMark();
            LogUtil.d(TAG, "查看mark值" + mark);
            ((DefaultLoginInfoIml) loginInfo).setMark(mark);
            AppManager.getInstance().setLoginInfo(loginInfo);
            LogUtil.d(TAG, "isLogin=" + AppManager.getInstance().getLoginInfo().isLogin() + ", loginId=" + AppManager.getInstance().getLoginInfo().getLoginId() + ", mark=" + AppManager.getInstance().getLoginInfo().getMark() + ", userId=" + AppManager.getInstance().getLoginInfo().getUserId());
        }*/
    }

    @Override
    protected String getLogFileName() {
        // TODO: 2017/11/15 修改成指定项目的打印log名称
        return "app.log";
    }

    /**
     * 判断是否是主进程
     * @return
     */
    public boolean isMainProcess() {
        boolean flag = false;
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcessInfoList = activityManager.getRunningAppProcesses();

        int pid = android.os.Process.myPid();
        String packageName = getPackageName();
        if (runningAppProcessInfoList == null || runningAppProcessInfoList.size() == 0) {
            return flag;
        }

        for (ActivityManager.RunningAppProcessInfo appProcessInfo : runningAppProcessInfoList) {
            if (appProcessInfo != null &&
                    pid == appProcessInfo.pid
                    && packageName != null && packageName.equals(appProcessInfo.processName)) {
                flag  = true;
                break;
            }
        }
        return flag;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }


    /**
     * 不能多线程调用
     * @return
     */
    public static BaseApplication getInstance() {
        return instance;
    }

    /**
     * activity生命周期管理
     */
    private void initActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                LogUtil.d("ActivityLifecycleCallbacks.onActivityCreated: " + activity);
                AppActivityManager.getInstance().setCurrentShowingActivity(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {
                LogUtil.d("ActivityLifecycleCallbacks.onActivityStarted: " + activity);
                AppActivityManager.getInstance().setCurrentShowingActivity(activity);
                AppActivityManager.getInstance().addCount(1);
            }

            @Override
            public void onActivityResumed(Activity activity) {
                LogUtil.d("ActivityLifecycleCallbacks.onActivityResumed: " + activity);
                AppActivityManager.getInstance().setCurrentShowingActivity(activity);
            }

            @Override
            public void onActivityPaused(Activity activity) {
                LogUtil.d("ActivityLifecycleCallbacks.onActivityPaused: " + activity);
//                LoadingUtils.getInstances().cancel();
            }

            @Override
            public void onActivityStopped(Activity activity) {
                LogUtil.d("ActivityLifecycleCallbacks.onActivityStopped: " + activity);
                AppActivityManager.getInstance().addCount(-1);
            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
                LogUtil.d("ActivityLifecycleCallbacks.onActivitySaveInstanceState: " + activity);

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
                LogUtil.d("ActivityLifecycleCallbacks.onActivityDestroyed: " + activity);

            }
        });
    }

}
