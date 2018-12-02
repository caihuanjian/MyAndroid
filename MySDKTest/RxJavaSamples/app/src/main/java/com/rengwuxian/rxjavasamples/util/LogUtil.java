package com.rengwuxian.rxjavasamples.util;

import android.util.Log;

/**
 * 功能：
 * Created by yinxm on 2017/11/3.
 */

public class LogUtil {

    public static String TAG = "yika";

    public static void v(String msg) {
        v(null, msg);
    }

    public static void v(String tag, String msg) {
        d(tag, msg);
    }

    /**
     * 调试
     *
     * @param msg 内容
     */
    public static void d(String msg) {
        d(TAG, msg);
    }

    /**
     * 调试
     *
     * @param tag
     * @param msg 内容
     */
    public static void d(String tag, String msg) {
        Log.d(tag, msg);
    }
}
