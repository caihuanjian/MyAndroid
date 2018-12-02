package com.app.media.vcr;

/**
 * 接收语音控制广播action和参数配置
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/22
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class VoiceCmdReceiveConstant {

    /**
     * 播放
     */
    public static final String PLAY = "com.app.media..PLAY";

    /**
     * 继续播放
     */
    public static final String CONTINUEPLAY = "com.app.media..CONTINUEPLAY";

    /**
     * 暂停
     */
    public static final String PAUSE = "com.app.media..PAUSE";

    /**
     * 下一首
     */
    public static final String NEXT = "com.app.media..NEXT";

    /**
     * 上一首
     */
    public static final String PREVIOUS = "com.app.media..PRE";

    /**
     * 换一个
     */
    public static final String CHANGE = "com.app.media..CHANGE";

    /**
     * 退出
     */
    public static final String EXIT = "com.app.media..EXIT";

    /**
     * 收藏
     */
    public static final String LIKE = "com.app.media..LIKE";

    /**
     * 切换播放模式
     */
    public static final String SWITCHMODE = "com.app.media..SWITCHMODE";

    /**
     * 我要听XXX的XX歌曲
     */
    public static final String QUERY = "com.app.media..QUERY";

    /**
     * 我要听有声节目
     */
    public static final String RADIO = "com.app.media..OPEN_PRIVATE_RADIO";

    /**
     * 打开随心听
     */
    public static final String OPEN_MEDIA = "com.app.media..START";


    /***********Intent 接收的一些字段参数**********/

    public static final String KEY_AUDIO_LIST = "audio_list";
    public static final String KEY_UUID = "uuid";
    public static final String KEY_OPERATE = "operate";
    public static final String KEY_PLAY_MODE = "play_mode";
    public static final String KEY_QUERY = "query";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ARTIST = "artist";


    /**************处理跳转页面的传参**************/

    public static final int PLAY_FRAGMENT_SEARCH = 5;
    public static final String PLAY_FRAGMENT_REQUEST_TYPE = "request_music_type";

}
