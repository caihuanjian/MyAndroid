package com.app.media.sdk;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 播放状态
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/28
 */
@IntDef({MediaPlayState.STATE_IDLE_STOPPED, MediaPlayState.STATE_PREPARING, MediaPlayState.STATE_PLAYING, MediaPlayState.STATE_PAUSED, MediaPlayState.STATE_PAUSED_BY_EVENT})
@Retention(RetentionPolicy.SOURCE)
public @interface MediaPlayState {
    /**
     * 空闲、停止状态
     */
    int STATE_IDLE_STOPPED = 0;

    /**
     * 准备播放中
     */
    int STATE_PREPARING = 1;

    /**
     * 播放状态
     */
    int STATE_PLAYING = 2;
    /**
     * 暂停状态
     */
    int STATE_PAUSED = 3;
    /**
     * 被暂时打断状态，后续会恢复播放
     */
    int STATE_PAUSED_BY_EVENT = 4;
}
