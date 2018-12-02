package com.app.media.player.constant;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * 目前支持的播放器类型
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/7
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({PlayerType.NONE, PlayerType.DEFAULT_EXO, PlayerType.MEDIA_PLAYER})
public @interface PlayerType {
    int NONE = 0;
    int DEFAULT_EXO = 1;
    int MEDIA_PLAYER = 2;
}
