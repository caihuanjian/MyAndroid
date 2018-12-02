package com.app.media.player;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 播放器播放状态回调
 * Created by yinxm on 2017/1/12.
 */
public interface OnPlayerCallback {

    /**
     * 播放状态回调
     *
     * @param state {@link State}
     */
    void onPlayerState(@State int state);

    /**
     * 播放器播放异常：一般是格式不支持
     *
     * @param playHelper
     * @param playUrl
     * @param errorMsg
     */
    void onPlayError(IPlayHelper playHelper, String playUrl, String errorMsg);

    @IntDef({State.STATE_IDLE, State.STATE_BUFFERING, State.STATE_READY, State.STATE_ENDED})
    @Retention(RetentionPolicy.SOURCE)
    @interface State {

        /**
         * 播放器处于空转状态
         */
        int STATE_IDLE = 1;
        /**
         * 开始缓冲
         */
        int STATE_BUFFERING = 2;
        /**
         * 缓冲完毕，准备好，可以开始播放
         */
        int STATE_READY = 3;
        /**
         * 播放完毕
         */
        int STATE_ENDED = 4;
    }

}
