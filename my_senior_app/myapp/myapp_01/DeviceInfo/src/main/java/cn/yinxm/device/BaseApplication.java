package cn.yinxm.device;

import android.app.Application;

import cn.yinxm.lib.constant.SPConstant;
import cn.yinxm.lib.api.manager.AppManager;
import cn.yinxm.lib.api.IAppConfig;
import cn.yinxm.lib.api.iml.DefaultAppConfigIml;
import cn.yinxm.lib.utils.SpUtil;
import cn.yinxm.lib.utils.log.LogUtil;
import cn.yinxm.lib.utils.log.log4j.ConfigureLog4J;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * 功能：
 * Created by yinxm on 2017/12/4.
 */

public class BaseApplication extends Application {
    private static String TAG = "BaseApplication";

    AppManager mAppManager;
    @Override
    public void onCreate() {
        super.onCreate();
        initAppconfig();
    }

    private void initAppconfig() {
        mAppManager = AppManager.getInstance();
        mAppManager.setApplicationContext(this);
        IAppConfig appConfig = AppManager.getInstance().getAppConfig();
//        ((DefaultAppConfigIml)appConfig).setLogEnabled(BuildConfig.LOG_DEBUG);
//        ((DefaultAppConfigIml)appConfig).setDeviceId(BuildConfig.LOG_DEBUG);
        initLog(appConfig);
        mAppManager.setAppConfig(appConfig);

    }

    /**
     * 初始化log
     * @param appConfig
     */
    private void initLog(IAppConfig appConfig) {
        boolean isLogOpen = BuildConfig.LOG_DEBUG;
        if (!isLogOpen) {//apk如果默认关闭log，读取本地缓存，看有没有打开log
            //读取本地缓存
            isLogOpen = SpUtil.readBoolean(getApplicationContext(), SPConstant.FILE_NAME_CONFIG, SPConstant.SP_KEY_LOG_IS_OPEN, false);
        }
        ((DefaultAppConfigIml)appConfig).setLogEnabled(isLogOpen);
        LogUtil.d(TAG, "" + appConfig.isLogEnabled());
        //初始化log to file
        ConfigureLog4J configureLog4J = new ConfigureLog4J(getApplicationContext());
        String logFileName = "device.log";

        configureLog4J.setFileName(logFileName);
        LogConfigurator logConfigurator = configureLog4J.getDefaultLogConfig();
        if (logConfigurator != null) {
            logConfigurator.setUseLogCatAppender(false);
            configureLog4J.init(logConfigurator);
        }
    }
}
