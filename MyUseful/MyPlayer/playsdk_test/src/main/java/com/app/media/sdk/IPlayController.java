package com.app.media.sdk;

/**
 * Created by yinxm on 2016/11/20.
 * 功能: 音乐播放Service控制
 */

public interface IPlayController {

    /**
     * 新开始播放，
     */
    void startPlay();

    /**
     * 是否正在播放
     *
     * @return
     */
    boolean isPlaying();

    /**
     * 是否暂停，包括临时中断
     *
     * @return
     */
    boolean isPaused();

    /**
     * 继续播放，paused-》play
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 瞬时中断，被其他事件暂时打断
     *
     * @param eventName 建议穿className，方便排查问题
     * @return 返回中断标识id
     */
    int pauseByEvent(String eventName);

    /**
     * 中断恢复播放
     *
     * @param pauseId 中断标识id
     * @return 是否恢复成功
     */
    boolean resumeByEvent(int pauseId);

    /**
     * 下一首
     */
    void playNext();

    /**
     * 上一首
     */
    void playPrevious();

    /**
     * 播放指定项
     *
     * @param position
     */
    void playPosition(int position);

    /**
     * 拖动进度到指定播放，也可用于快进快退
     *
     * @param positionMs
     */
    void seekTo(long positionMs);

    /**
     * 停止播放
     */
    void stop();

    /**
     * 退出
     */
    void exit();
}
