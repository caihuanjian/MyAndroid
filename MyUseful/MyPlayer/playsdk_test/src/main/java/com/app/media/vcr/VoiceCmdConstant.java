package com.app.media.vcr;

/**
 * Created by yinxuming on 2018/8/1.
 */
public class VoiceCmdConstant {
    // 新数据格式

    /**
     * 下一首
     */
    public static final String NEXT = "com.app.media..NEXT";
    /**
     * 上一首
     */
    public static final String PREVIOUS = "com.app.media..PRE";

    /**
     * 我要听XXX的XX歌曲
     */
    public static final String QUERY = "com.app.media..QUERY";
    /**
     * 我要听有声节目
     */
    public static final String RADIO = "com.app.media..OPEN_PRIVATE_RADIO";


    // 旧的数据格式

    /**
     * 播放
     */
    public static final String PLAY = "com.app.media..PLAY";


    // 处理跳转页面的传参
    public static final int PLAY_FRAGMENT_SEARCH = 5;
    public static final String PLAY_FRAGMENT_REQUEST_TYPE = "request_music_type";


}
