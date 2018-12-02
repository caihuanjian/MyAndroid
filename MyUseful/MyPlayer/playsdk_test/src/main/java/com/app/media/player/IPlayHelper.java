package com.app.media.player;

import java.util.Map;

/**
 * Created by yinxm on 2017/1/12.
 * 功能: 播放接口
 */

public interface IPlayHelper {
    String TAG = "player";

    int DEFAULT_FAST_FORWARD_MS = 15000;
    int DEFAULT_REWIND_MS = 5000;


    /**
     * @param url 播放地址
     */
    void play(String url);

    void play(String url, OnPlayerCallback playFinishCallback);

    /**
     * @param url                播放地址
     * @param extendParams       播放额外需要的参数，可为null
     * @param playFinishCallback 播放完毕后回调，可为null
     */
    void play(String url, Map<String, Object> extendParams, OnPlayerCallback playFinishCallback);

    void play();

    void pause();

    /**
     * 播放-暂停-播放 切换
     */
    void playToggle();

    /**
     * 停止播放
     */
    void stop();

    /**
     * 停止播放，停止了后player不能再被使用
     */
    void release();

    /**
     * 获取音频时长
     *
     * @return
     */
    long getDuration();

    /**
     * 获取当前播放音频长度
     *
     * @return
     */
    long getCurrentPosition();

    /**
     * 获取缓冲百分比
     *
     * @return
     */
    int getBufferedPercentage();

    /**
     * 获取缓冲时长
     *
     * @return
     */
    long getBufferedPosition();

    boolean isPlaying();

    // 快退
    void rewind();

    // 快进
    void fastForward();

    // 播放进度移动到指定位置
    void seekTo(long positionMs);

    /**
     * 获取播放地址
     *
     * @return
     */
    String getPlayUrl();

    void setVolume(float volume);


    /**
     * 获取播放器实例
     *
     * @param <T>
     * @return
     */
    <T> T getPlayerInstance();

}
