package com.app.media.vcr;

/**
 * 对外发送广播信息
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/21
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class VoiceCmdSendConstant {
    /**
     * 发送播放状态
     */
    public static final String NOTIFY_PLAY_STATE = "com.app.media..notify.PLAY_STATE";
    /**
     * 发送播放信息
     */
    public static final String NOTIFY_PLAY_INFO = "com.app.media..notify.PLAY_INFO";

    /***********Intent 接收的一些字段参数**********/

    public static final String KEY_PLAY_STATE = "play_state";

    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_COVER = "cover";
    public static final String KEY_DURATION = "duration";
    public static final String KEY_PROGRESS = "progress";
    public static final String KEY_ALBUM_NAME = "album_name";
    public static final String KEY_IS_PLAYING = "is_playing";

}
