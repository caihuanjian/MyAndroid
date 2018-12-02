package cn.yinxm.app.base;

import android.app.Application;

import cn.yinxm.app.BuildConfig;
import cn.yinxm.lib.constant.SPConstant;
import cn.yinxm.lib.api.manager.AppManager;
import cn.yinxm.lib.api.IAppConfig;
import cn.yinxm.lib.api.iml.DefaultAppConfigIml;
import cn.yinxm.lib.utils.HardwareUID;
import cn.yinxm.lib.utils.MetaReadUtil;
import cn.yinxm.lib.utils.SpUtil;
import cn.yinxm.lib.utils.StringUtil;
import cn.yinxm.lib.utils.log.LogUtil;
import cn.yinxm.lib.utils.log.log4j.ConfigureLog4J;
import de.mindpipe.android.logging.log4j.LogConfigurator;

/**
 * 功能：Application工具类
 * Created by yinxm on 2017/11/15.
 */

public abstract class BaseApplicationUtil extends Application {
    private static final String TAG = "BaseApplicationUtil";

    private AppManager mAppManager;


    @Override
    public void onCreate() {
        super.onCreate();
        initWorkEcConfig();
    }
    /**
     * 初始化WorkEc配置
     */
    private void initWorkEcConfig() {
        mAppManager = AppManager.getInstance();
        AppManager.getInstance().setApplicationContext(getApplicationContext());
        IAppConfig appConfig = AppManager.getInstance().getAppConfig();
        if (appConfig == null) {
            appConfig = new DefaultAppConfigIml();
        }

        //初始化打印log
        initLog(appConfig);
        //初始化设备唯一标识
        initDeviceId(appConfig);

        //初始化应用信息
//        appConfig.setAppTypeName(AppTypeName.HUI_TING);

        //取友盟渠道id值作为WorkEc appId
        String appId = MetaReadUtil.readMetaDataFromApplication(this, "UMENG_CHANNEL");
        LogUtil.d(TAG, " appId=" + appId);
        if (StringUtil.isNotBlank(appId)) {
            ((DefaultAppConfigIml) appConfig).setAppChannelId(appId);
        }
        AppManager.getInstance().setAppConfig(appConfig);
        initLoginInfo();

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
        String logFileName = getLogFileName();
        if (StringUtil.isBlank(logFileName)) {
            logFileName = "work_ec.log";
        }
        configureLog4J.setFileName(logFileName);
        LogConfigurator logConfigurator = configureLog4J.getDefaultLogConfig();
        if (logConfigurator != null) {
            logConfigurator.setUseLogCatAppender(false);
            configureLog4J.init(logConfigurator);
        }
    }

    /**
     * 初始化设备唯一标识
     */
    private void initDeviceId(IAppConfig appConfig) {
        try {
            String deviceId = SpUtil.spReadStr(this, SPConstant.FILE_NAME_CONFIG, SPConstant.SP_KEY_DEVICE_ID);

            LogUtil.d(TAG, "1，deviceId=" + deviceId);
            if (StringUtil.isBlank(deviceId)) {
                deviceId = HardwareUID.getDeviceUID(this);
            }
            if (StringUtil.isNotBlank(deviceId)) {
                SpUtil.spWriteStr(this, SPConstant.FILE_NAME_CONFIG, SPConstant.SP_KEY_DEVICE_ID, deviceId);
                ((DefaultAppConfigIml)appConfig).setDeviceId(deviceId);
            }
            LogUtil.d(TAG, "deviceId=" + appConfig.getDeviceId());
        } catch (Exception e) {
            LogUtil.e("获取appid出错", e);
        }
    }

    /**
     * 初始化登录信息，将登录信息写入AppManager中，供全局调用
     */
    protected abstract void initLoginInfo();

    /**
     * 本地打印log文件名称
     * @return
     */
    protected abstract String getLogFileName();
}
