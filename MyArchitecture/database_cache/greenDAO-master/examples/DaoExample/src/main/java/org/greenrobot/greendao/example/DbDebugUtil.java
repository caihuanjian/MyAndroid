package org.greenrobot.greendao.example;

import java.lang.reflect.Method;

/**
 * 功能：
 * Created by yinxm on 2017/12/27.
 */

public class DbDebugUtil {

    public static String getDebugInfo() {
        String str = null;
        try {
            Class debugDb = Class.forName("com.amitshekhar.DebugDB");
            Method getAddressLog = debugDb.getMethod("getAddressLog");
            Object value = getAddressLog.invoke(null);
            str = (String) value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return str;
    }
}
