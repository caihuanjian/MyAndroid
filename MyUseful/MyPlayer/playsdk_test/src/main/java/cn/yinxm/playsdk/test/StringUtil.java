package cn.yinxm.playsdk.test;

/**
 * Created by yinxm on 2017/2/13.
 * 功能:
 */

public class StringUtil {
    public static final boolean isBlank(final String str) {
        return str == null || str.trim().equals("") || str.equals("Null") || str.equals("null");
    }

    public static final boolean isNotBlank(final String str) {
        return (!isBlank(str));
    }
}
