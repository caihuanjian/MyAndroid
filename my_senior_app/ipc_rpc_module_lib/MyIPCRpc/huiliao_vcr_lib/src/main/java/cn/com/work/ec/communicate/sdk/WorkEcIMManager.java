package cn.com.work.ec.communicate.sdk;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

import dalvik.system.PathClassLoader;


/**
 * IM 功能调用入口
 */
public abstract class WorkEcIMManager implements cn.com.work.ec.communicate.sdk.IWorkEcIMApi {
    private static final String TAG = WorkEcIMManager.class.getSimpleName();
    private static Context context;
    private static WorkEcIMManager instance = null;
    private static String packageNameStr, apiImlClassNameStr;

    /**
     * 获取IM功能调用实例
     * @return IWorkEcIMApi
     */
    public static WorkEcIMManager getInstance() {
        init(context);
        return instance;
    }

    /**
     * 初始化IM API调用
     * @param applicationContext
     * @return 是否初始化成功
     */
    public synchronized static boolean init(Context applicationContext) {
        boolean flag = false;
        try {
            if (applicationContext != null && instance == null) {
                context = applicationContext.getApplicationContext();
               if (TextUtils.isEmpty(packageNameStr)) {
                   packageNameStr = "com.work.ec.btphone";
               }
               if (TextUtils.isEmpty(apiImlClassNameStr)) {
                   apiImlClassNameStr = "cn.com.work.ec.communicate.sdk.iml.WorkEcIMApiIml";
               }

                Intent intent = new Intent();
                intent.setPackage(packageNameStr);
                PackageManager packageManager = context.getPackageManager();
                List<ResolveInfo> list = packageManager.queryIntentActivities(intent, 0);
                Log.d(TAG, "list=" + list);
                if (list != null && list.size() > 0 && list.get(0) != null) {
                    //  获得指定的activity的信息
                    ActivityInfo activityInfo = list.get(0).activityInfo;
                    // 获得apk的目录或者jar的目录
                    String apkPath = activityInfo.applicationInfo.sourceDir;
                    //  native代码的目录
                    String libPath = activityInfo.applicationInfo.nativeLibraryDir;
                    Log.d(TAG, "apkPath=" + apkPath+", libPath="+libPath +", classLoader="+context.getClassLoader());
                    // 创建类加载器，把dex加载到虚拟机中
                    // 第一个参数：是指定apk安装的路径，这个路径要注意只能是通过actInfo.applicationInfo.sourceDir来获取
                    // 第二个参数：是C/C++依赖的本地库文件目录,可以为null
                    // 第三个参数：是上一级的类加载器
                    PathClassLoader pcl = new PathClassLoader(apkPath, libPath, context.getClassLoader());
                    Class clazz = pcl.loadClass(apiImlClassNameStr);
                    Object object =  clazz.newInstance();
                    Log.d(TAG, "object=" + object);
                    if (object != null && object instanceof WorkEcIMManager) {
                        instance = (WorkEcIMManager) object;
                        Log.d(TAG, "instance=" + instance);
                    }
                }
            }
            if (instance != null) {
                flag = true;
            }
        } catch (Exception e) {
            Log.d(TAG, "" + e);
        }
        return flag;
    }

    /**
     * 初始化IM API调用
     * @param applicationContext
     * @param packageName app的包名
     * @param apiImlClassName api 实现类全路径
     * @return 是否初始化成功
     */
    public synchronized static boolean init(Context applicationContext, String packageName, String apiImlClassName) {
        packageNameStr = packageName;
        apiImlClassNameStr = apiImlClassName;
        return init(applicationContext);
    }

    public static Context getContext() {
        return context;
    }
}
