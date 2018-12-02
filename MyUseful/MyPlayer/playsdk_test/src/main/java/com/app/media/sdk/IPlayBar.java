package com.app.media.sdk;

import android.support.annotation.LayoutRes;

import com.app.media.bean.base.MediaBean;


/**
 * 页面中的快捷播放条
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/8
 */
public interface IPlayBar {

    /**
     * 播放条布局
     */
    @LayoutRes
    int getLayoutResID();

    /**
     * 打开播放页
     */
    void openPlayActivity();

    /**
     * 当有列表的时候显示
     */
    void showBar();

    /**
     * GONE隐藏
     */
    void hideBar();

    /**
     * onResume时刷新数据
     */
    void onResumeUpdatePlayInfo();

    void onPlayStateChanged(boolean isPlaying);

    void onPlayBeanChanged(MediaBean bean);

    void onProgressChanged(long duration, long currentPlayPosition, long bufferedPosition);

}
