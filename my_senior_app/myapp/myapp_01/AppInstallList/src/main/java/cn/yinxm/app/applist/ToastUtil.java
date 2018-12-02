package cn.yinxm.app.applist;

import android.content.Context;
import android.widget.Toast;

/**
 * 功能：
 * Created by yinxm on 2017/5/23.
 */

public class ToastUtil {
    private static Toast toast = null;

    public ToastUtil() {
    }

    public static void showTextToast(Context context, String msg) {
        if (toast == null) {
            toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }

        toast.show();
    }
}
