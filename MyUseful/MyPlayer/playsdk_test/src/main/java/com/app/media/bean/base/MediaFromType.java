package com.app.media.bean.base;


import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.app.media.bean.base.MediaFromType.AUDIO;
import static com.app.media.bean.base.MediaFromType.LOCAL;
import static com.app.media.bean.base.MediaFromType.MUSIC;
import static com.app.media.bean.base.MediaFromType.NEWS;

/**
 * 单个播放资源来源类型
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/7
 */
@SuppressWarnings({"unused"})
@IntDef({MUSIC, AUDIO, NEWS, LOCAL})
@Retention(RetentionPolicy.SOURCE)
public @interface MediaFromType {

    /**
     * 音乐
     */
    int MUSIC = 0;

    /**
     * 有声
     */
    int AUDIO = 1;

    /**
     * 新闻
     */
    int NEWS = 2;

    /**
     * 本地音乐、USB音乐
     */
    int LOCAL = 3;

    /**
     * 蓝牙音乐
     */
    int BT_MUSIC = 4;

    /**
     * 本地收音机
     */
    int NATIVE_RADIO = 5;
}
