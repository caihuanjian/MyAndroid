package com.app.media.bean.base;

import android.annotation.SuppressLint;

/**
 * 音乐播放列表类型
 */
@SuppressLint("unused")
public enum MediaPlayListType {
    /**
     * 未知
     */
    DEFAULT_UNKNOWN(0, ""),

    /**
     * 每日随心
     */
    DAILY(1, "每日随心"),

    /**
     * 我喜欢的
     */
    LIKE(2, "我喜欢的"),

    /**
     * 最近播放的歌曲
     */
    RECENT_PLAY(3, "最近播放的歌曲"),

    /**
     * USB、SD卡等本地扫描出来的音乐
     */
    LOCAL(4, "本地音乐"),

    /**
     * 语音搜索出来的列表
     */
    VOICE(5, "语音"),

    /**
     * 有声
     */
    AUDIO(6, "有声"),

    /**
     * 新闻
     */
    NEWS(7, "新闻"),

    /**
     * 音乐
     */
    MUSIC(8, "音乐"),


    /**
     * 搜索-音乐
     */
    SEARCH_MUSIC(9, "我搜索的"),
    /**
     * 搜索-有声
     */
    SEARCH_AUDIO(10, "我搜索的"),
    /**
     * 搜索-新闻
     */
    SEARCH_NEWS(11, "我搜索的"),

    BT_MUSIC(12, "蓝牙音乐"),

    NATIVE_RADIO(13, "收音机");

    private int index;
    /**
     * 用于播放页显示
     */
    private String name;

    MediaPlayListType(int index, String name) {
        this.index = index;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

    public static MediaPlayListType fromIndex(int index) {
        MediaPlayListType[] values = MediaPlayListType.values();
        for (MediaPlayListType type : values) {
            if (type.getIndex() == index) {
                return type;
            }
        }
        return null;
    }

}
