package com.app.media.sdk;


import com.app.media.player.IPlayHelper;

/**
 * 播放服务
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/28
 */
public interface IPlayService {

    /**
     * 获取播放控制器
     *
     * @return
     */
    IPlayController getPlayController();

    /**
     * 获取当前播放信息
     *
     * @return
     */
    IPlayData getCurrentPlayInfo();

    /**
     * 获取播放回调
     *
     * @return
     */
    IPlayCallback getPlayCallback();

    /**
     * 获取当前播放器，如果要控制播放，直接使用{@link #getPlayController}
     *
     * @return
     */
    IPlayHelper getPlayer();

    /**
     * 是否获取到焦点
     *
     * @return
     */
    boolean isHaveAudioFocus();

    /**
     * 是否正在播放
     *
     * @return
     */
    boolean isPlaying();

    /**
     * 是否暂停
     *
     * @return
     */
    boolean isPaused();

    IMediaNotification getMediaNotification();
}
