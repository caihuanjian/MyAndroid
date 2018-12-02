
package com.app.media.sdk;


import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaList;
import com.app.media.bean.base.MediaPlayListType;
import com.app.media.player.IPlayHelper;

/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/21
 */
public interface IPlayData {
    /**
     * 播放列表大小
     *
     * @return
     */
    int getPlayListSize();

    /**
     * 播放列表
     *
     * @return
     */
    MediaList getPlayList();

    /**
     * 播放项
     *
     * @return
     */
    int getPlayListPosition();

    /**
     * 当前播放节目信息
     *
     * @return
     */
    MediaBean getPlayMediaBean();

    /**
     * 播放列表类型
     *
     * @return
     */
    MediaPlayListType getPlayListType();

    /**
     * 播放状态
     *
     * @return
     */
    @MediaPlayState
    int getPlayState();

    /**
     * 播放模式
     *
     * @return
     */
    MediaPlayMode getPlayMode();

    /**
     * 是否播放
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

    /**
     * 获取下一个
     *
     * @return
     */
    int getNextPosition();

    /**
     * 获取上一个
     *
     * @return
     */
    int getPrePosition();

    /**
     * 获取当前播放器里面的相关信息
     *
     * @return
     */
    IPlayHelper getPlayer();

}
