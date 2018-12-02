package com.app.media.sdk.iml;

import android.content.Context;

import com.app.media.util.SpUtil;


/**
 * 播放进度恢复
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/3
 */
public class PlayProgressRecovery {

    /**
     * 记录播放位置
     */
    public static final String FILE_NAME_PLAY_LIST_CONFIG = "play_list_config";
    /**
     * 当前播放进度
     */
    public static final String KEY_CURRENT_PLAY_POSITION = "current_play_position";
    /**
     * 当前音频播放位置
     */
    public static final String KEY_CURRENT_PLAY_TIME = "current_play_time";
    /**
     * 当前播放类型
     */
    public static final String KEY_CURRENT_PLAY_TYPE = "current_play_type";
    /**
     * 播放url和进度之间的连接符
     */
    public static final String KEY_PLAY_URL_PROGRESS = "#";
    /**
     * 播放进度和最大进度连接符
     */
    public static final String KEY_CURRENT_PROGRESS_2_MAX = "_";

    public boolean checkCanRecovery() {
        return false;
    }

    public static void saveProgress(Context context, String palyUrl, long duration, long progress) {
        String record = palyUrl + KEY_PLAY_URL_PROGRESS + progress + KEY_CURRENT_PROGRESS_2_MAX + duration;
        // http://other.web.rh01.sycdn.kuwo.cn/63a6e262c8397f20802e288cfa74b7db/5b8f9c93/resource/n1/28/6/3272064309.mp3#99806_187088
//        LogUtil.d("record=" + record);
        SpUtil.spWriteStr(context, FILE_NAME_PLAY_LIST_CONFIG, KEY_CURRENT_PLAY_TIME, record);
    }


}
