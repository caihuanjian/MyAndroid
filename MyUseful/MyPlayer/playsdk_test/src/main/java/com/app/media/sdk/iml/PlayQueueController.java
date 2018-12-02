package com.app.media.sdk.iml;


import com.app.media.manager.AppActivityManager;
import com.app.media.manager.MediaPlayServiceManger;
import com.app.media.manager.PlayControlManager;
import com.app.media.sdk.IPlayController;

import java.util.HashMap;
import java.util.Map;

import cn.yinxm.playsdk.test.LogUtil;

/**
 * 播放队列控制，用于处理当Service还没有准备好、点击太频繁等处理
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/2
 */
public class PlayQueueController implements IPlayController {
    private static final String TAG = "PlayQueueController";

    private int actionWait = PlayControlManager.PlayerAction.ACTION_NONE;
    private Map<String, Object> extParams = new HashMap<>();

    private static final String KEY_POSITION = "position";
    private static final String KEY_SEEK_TO = "seekTo";


    public boolean isHaveWaitAction() {
        return actionWait != PlayControlManager.PlayerAction.ACTION_NONE;
    }

    public void processWaitAction() {
        LogUtil.d(TAG, "processWaitAction act=" + actionWait + ", " + extParams + ", "
                + MediaPlayServiceManger.getInstance().getMediaPlayService());

        if (!isHaveWaitAction()) {
            return;
        }

        if (MediaPlayServiceManger.getInstance().getMediaPlayService() == null) {
            return;
        }
        LogUtil.d(TAG, "switch act=" + actionWait);

        try {
            synchronized (PlayQueueController.class) {
                switch (actionWait) {
                    case PlayControlManager.PlayerAction.ACTION_START_PLAY:
                        PlayControlManager.getInstance().startPlay();
                        break;
                    case PlayControlManager.PlayerAction.ACTION_CONTINUE_PLAY:
                        PlayControlManager.getInstance().play();
                        break;
                    case PlayControlManager.PlayerAction.ACTION_PAUSE:
                        PlayControlManager.getInstance().pause();
                        break;
                    case PlayControlManager.PlayerAction.ACTION_NEXT:
                        PlayControlManager.getInstance().playNext();
                        break;
                    case PlayControlManager.PlayerAction.ACTION_PREV:
                        PlayControlManager.getInstance().playPrevious();
                        break;
                    case PlayControlManager.PlayerAction.ACTION_STOP:
                        PlayControlManager.getInstance().stop();
                        break;
                    case PlayControlManager.PlayerAction.ACTION_PLAY_ITEM:
                        if (extParams.containsKey(KEY_POSITION)) {
                            PlayControlManager.getInstance().playPosition((Integer) extParams.get(KEY_POSITION));
                        }
                        break;
                    case PlayControlManager.PlayerAction.ACTION_SEEK_TO:
                        if (extParams.containsKey(KEY_SEEK_TO)) {
                            PlayControlManager.getInstance().seekTo((Long) extParams.get(KEY_SEEK_TO));
                        }
                        break;
                    default:
                        break;
                }

                actionWait = PlayControlManager.PlayerAction.ACTION_NONE;
                extParams.clear();
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }

    }

    @Override
    public void startPlay() {
        actionWait = PlayControlManager.PlayerAction.ACTION_START_PLAY;
    }

    @Override
    public boolean isPlaying() {
        return false;
    }

    @Override
    public boolean isPaused() {
        return false;
    }

    @Override
    public void play() {
        actionWait = PlayControlManager.PlayerAction.ACTION_CONTINUE_PLAY;
    }

    @Override
    public void pause() {
        actionWait = PlayControlManager.PlayerAction.ACTION_PAUSE;
    }

    @Override
    public int pauseByEvent(String eventName) {
        return 0;
    }

    @Override
    public boolean resumeByEvent(int pauseId) {
        return false;
    }

    @Override
    public void playNext() {
        actionWait = PlayControlManager.PlayerAction.ACTION_NEXT;
    }

    @Override
    public void playPrevious() {
        actionWait = PlayControlManager.PlayerAction.ACTION_PREV;
    }

    @Override
    public void playPosition(int position) {
        actionWait = PlayControlManager.PlayerAction.ACTION_PLAY_ITEM;
        extParams.put(KEY_POSITION, position);
    }

    @Override
    public void seekTo(long positionMs) {
        actionWait = PlayControlManager.PlayerAction.ACTION_SEEK_TO;
        extParams.put(KEY_SEEK_TO, positionMs);
    }

    @Override
    public void stop() {
        actionWait = PlayControlManager.PlayerAction.ACTION_STOP;
    }

    @Override
    public void exit() {
        // TODO: 2018/9/15 test
        AppActivityManager.getInstance().exitApp(true);
    }
}
