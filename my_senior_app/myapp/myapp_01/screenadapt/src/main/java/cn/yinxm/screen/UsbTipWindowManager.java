package cn.yinxm.screen;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import cn.yinxm.lib.screen.ScreenAdapter;
import cn.yinxm.lib.utils.log.LogUtil;


/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/10/18
 */
public class UsbTipWindowManager {
    private static final String TAG = "UsbTipWindowManager";

    //  用于控制在屏幕上添加或移除悬浮窗
    private WindowManager mWindowManager;
    private static UsbTipWindowManager mFloatWindowManager;
    private WindowManager.LayoutParams mParams;
    private View mLayoutView;

    //    private WeakReference<View> mLayoutRef;
    private UsbTipWindowManager() {
    }


    public static UsbTipWindowManager getInstance() {
        if (mFloatWindowManager == null) {
            mFloatWindowManager = new UsbTipWindowManager();
        }
        return mFloatWindowManager;
    }

    /**
     * 创建MIC浮窗
     *
     * @param context 必须为应用程序的Context.
     */
    public void showDiscoveryUsb(Context context) {
        context = context.getApplicationContext();
        if (mLayoutView != null) {
            LogUtil.i(TAG, "last tip still show, should remove all event");
            removeWindow();
        }
        mWindowManager = getWindowManager(context);
        // 赋值WindowManager&LayoutParam.
        mParams = new WindowManager.LayoutParams();
        // 设置type.系统提示型窗口，一般都在应用程序窗口之上.
        //         if (CoDriverUtil.getSDKVersion() >= 19) {
        mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        mParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY; // api 26
        //         } else {
        //             mParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        //         }
        // 设置效果为背景透明.
//        mParams.format = PixelFormat.RGBA_8888;
        // 效果为黑色背景
        mParams.format = PixelFormat.TRANSLUCENT;
        // 设置flags.不可聚焦及不可使用按钮对悬浮窗进行操控.
//        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager
//                .LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        mParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
//                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        // 设置窗口初始停靠位置.
        mParams.gravity = Gravity.LEFT | Gravity.TOP;
        mParams.x = 0;
        mParams.y = 0;
        // 设置悬浮窗口长宽数据.
        mParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        ScreenAdapter.getInstance().adaptUpdate(context);
        LayoutInflater inflater = LayoutInflater.from(context);

        // 获取浮动窗口视图所在布局.
        mLayoutView = inflater.inflate(R.layout.dialog_usb_discovery_port, null);
        mWindowManager.addView(mLayoutView, mParams);
        LogUtil.i(TAG, "createWindow mLightLayout = " + mLayoutView);

        mLayoutView.post(new Runnable() {
            @Override
            public void run() {
                LogUtil.e("mWindowManager w=" + mLayoutView.getWidth() + ", h=" + mLayoutView.getHeight());
            }
        });

        mLayoutView.postDelayed(new Runnable() {
            @Override
            public void run() {
                removeWindow();
            }
        }, 5000);
    }

    /**
     * 如果WindowManager还未创建，则创建新的WindowManager返回。否则返回当前已创建的WindowManager
     *
     * @param context
     */
    private WindowManager getWindowManager(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
        }
        return mWindowManager;
    }

    public void removeWindow() {
        if (mLayoutView == null) {
            LogUtil.e(TAG, "removeWindow mLightLayout is already null");
            return;
        }
        LogUtil.i(TAG, "removeWindow mLightLayout = " + mLayoutView);
        if (mLayoutView != null) {
            LogUtil.e("mWindowManager w=" + mLayoutView.getWidth() + ", h=" + mLayoutView.getHeight());
//            mWindowManager.removeViewImmediate(mLayoutView);
            mWindowManager.removeView(mLayoutView);
            mLayoutView = null;
        }
    }
}
