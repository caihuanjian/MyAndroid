package cn.yinxm.util;


/**
 * 字符串工具类
 */
public final class StringUtil {
    private StringUtil() {
    }

    public static final boolean isBlank(final String str) {
        return str == null || str.trim().equals("") || str.equals("Null") || str.equals("null");
    }

    public static final boolean isNotBlank(final String str) {
        return (!isBlank(str));
    }

}
