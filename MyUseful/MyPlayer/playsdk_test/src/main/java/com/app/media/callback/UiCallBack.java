package com.app.media.callback;

/**
 * Created by yinxuming on 2018/7/26.
 */
public interface UiCallBack {
    /**
     * 暂无
     */
    int SHOW_MSG_NO = 1;
    /**
     * 暂无下一首
     */
    int SHOW_MSG_NO_NEXT = 2;
    int SHOW_MSG_LOAD_ERROR = 3;
    int SHOW_MSG_NET_LOAD_ERROR = 4;
    int SHOW_MSG_NO_NET = 5;
    int SHOW_MSG_CAN_NOT_PLAY = 6;

    void hide();

    void show();

    void show(int showMsg);

    void show(String showMsgInfo);

    boolean isShow();
}
