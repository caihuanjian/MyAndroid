package com.app.media.manager;


import android.support.annotation.IntDef;

import com.app.media.sdk.IPlayController;
import com.app.media.sdk.iml.PlayQueueController;
import com.app.media.service.MediaPlayService;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.app.media.manager.PlayControlManager.PlayerAction.ACTION_CONTINUE_PLAY;
import static com.app.media.manager.PlayControlManager.PlayerAction.ACTION_NEXT;
import static com.app.media.manager.PlayControlManager.PlayerAction.ACTION_NONE;
import static com.app.media.manager.PlayControlManager.PlayerAction.ACTION_PAUSE;
import static com.app.media.manager.PlayControlManager.PlayerAction.ACTION_PLAY_ITEM;
import static com.app.media.manager.PlayControlManager.PlayerAction.ACTION_PREV;
import static com.app.media.manager.PlayControlManager.PlayerAction.ACTION_SEEK_TO;
import static com.app.media.manager.PlayControlManager.PlayerAction.ACTION_START_PLAY;
import static com.app.media.manager.PlayControlManager.PlayerAction.ACTION_STOP;


/**
 * 播放控制器，方便调用，结合播放数据控制{@link PlayDataManager#setPlayList}
 * 和{@link PlayDataManager#playPosition(int)}等，控制service播放
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/29
 */
public class PlayControlManager implements IPlayController {

    private PlayQueueController playQueueController;

    private PlayControlManager() {
        playQueueController = new PlayQueueController();
    }

    public static PlayControlManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static PlayControlManager INSTANCE = new PlayControlManager();
    }

    @IntDef({ACTION_NONE, ACTION_START_PLAY, ACTION_CONTINUE_PLAY, ACTION_PAUSE,
            ACTION_NEXT, ACTION_PREV, ACTION_STOP,
            ACTION_PLAY_ITEM, ACTION_SEEK_TO})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PlayerAction {
        /**
         * 默认无操作
         */
        int ACTION_NONE = 0;

        /**
         * 新开始播放，
         */
        int ACTION_START_PLAY = 1;

        /**
         * 继续播放，paused-》play
         */
        int ACTION_CONTINUE_PLAY = 2;

        /**
         * 暂停
         */
        int ACTION_PAUSE = 3;

        /**
         * 下一首
         */
        int ACTION_NEXT = 4;

        /**
         * 上一首
         */
        int ACTION_PREV = 5;


        /**
         * 停止播放
         */
        int ACTION_STOP = 6;

        /**
         * 播放指定项
         * int:position
         */
        int ACTION_PLAY_ITEM = 7;

        /**
         * 拖动进度到指定播放，也可用于快进快退
         * long:seekTo
         */
        int ACTION_SEEK_TO = 8;
    }


    private IPlayController getPlayController() {
        MediaPlayService mediaPlayService = MediaPlayServiceManger.getInstance().getMediaPlayService();
        if (mediaPlayService != null) {
            return mediaPlayService;
        } else {
            MediaPlayServiceManger.getInstance().startAndBindService(
                    AppManager.getInstance().getApplicationContext());
            // TODO: 2018/9/15 test
            return playQueueController;
        }
    }

    @Override
    public void startPlay() {
        getPlayController().startPlay();
    }

    @Override
    public boolean isPlaying() {
        return getPlayController().isPlaying();
    }

    @Override
    public boolean isPaused() {
        return getPlayController().isPaused();
    }

    @Override
    public void play() {
        getPlayController().play();
    }

    @Override
    public void pause() {
        getPlayController().pause();
    }

    @Override
    public int pauseByEvent(String eventName) {
        return getPlayController().pauseByEvent(eventName);
    }

    @Override
    public boolean resumeByEvent(int pauseId) {
        return getPlayController().resumeByEvent(pauseId);
    }

    @Override
    public void playNext() {
        getPlayController().playNext();
    }

    @Override
    public void playPrevious() {
        getPlayController().playPrevious();
    }

    @Override
    public void playPosition(int position) {
        getPlayController().playPosition(position);
    }

    @Override
    public void seekTo(long positionMs) {
        getPlayController().seekTo(positionMs);
    }

    @Override
    public void stop() {
        getPlayController().stop();
    }

    @Override
    public void exit() {
        getPlayController().exit();
    }

    public PlayQueueController getPlayQueueController() {
        return playQueueController;
    }
}
