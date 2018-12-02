package com.app.media.manager;


import com.app.media.callback.UiCallBack;
import com.app.media.callback.iml.DefaultUiCallBack;

/**
 * Created by yinxuming on 2018/7/26.
 */
public class UiCallBackManager {

    private UiCallBack mUiCallBackToast;
    private UiCallBack mUiCallBackDialog;

    private UiCallBackManager() {
    }

    private static class SingleHolder {
        static final UiCallBackManager INSTANCE = new UiCallBackManager();
    }

    public static UiCallBackManager getInstance() {
        return SingleHolder.INSTANCE;
    }

    public UiCallBack getUiCallBackToast() {
        if (mUiCallBackToast == null) {
            mUiCallBackToast = new DefaultUiCallBack();
        }
        return mUiCallBackToast;
    }

    public void setUiCallBackToast(UiCallBack uiCallBackToast) {
        mUiCallBackToast = uiCallBackToast;
    }

    public UiCallBack getUiCallBackDialog () {
        if (mUiCallBackDialog == null) {
            mUiCallBackDialog = new DefaultUiCallBack();
        }
        return mUiCallBackDialog;
    }

    public void setUiCallBackDialog(UiCallBack uiCallBackDialog) {
        mUiCallBackDialog = uiCallBackDialog;
    }
}
