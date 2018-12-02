package com.app.media.sdk;


import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaList;
import com.app.media.bean.base.MediaPlayListType;

/**
 * 播放信息改变回调
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/28
 */
public interface OnPlayInfoCallback {

    /**
     * 播放状态改变
     *
     * @param state
     * @param isPlaying
     */
    void onPlayStateChanged(@MediaPlayState int state, boolean isPlaying);

    /**
     * 当前播放信息改变
     *
     * @param position 当前播放position
     * @param bean
     */
    void onPlayBeanChanged(int position, MediaBean bean);

    /**
     * 播放列表改变
     *
     * @param playListType 播放列表类型
     * @param listBean
     */
    void onPlayListChanged(MediaPlayListType playListType, MediaList listBean);

    /**
     * 播放模式切换
     *
     * @param mode
     */
    void onPlayModeChanged(MediaPlayMode mode);
}
