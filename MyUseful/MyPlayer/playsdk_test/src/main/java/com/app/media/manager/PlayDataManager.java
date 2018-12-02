package com.app.media.manager;

import android.support.annotation.IntDef;

import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaList;
import com.app.media.bean.base.MediaPlayListType;
import com.app.media.player.IPlayHelper;
import com.app.media.sdk.IPlayData;
import com.app.media.sdk.MediaPlayMode;
import com.app.media.sdk.MediaPlayState;
import com.app.media.service.MediaPlayService;
import com.app.media.util.DataValidCheck;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Random;

import cn.yinxm.playsdk.test.LogUtil;

import static com.app.media.manager.PlayDataManager.NotifyLevel.NOTIFY_SERVICE;
import static com.app.media.manager.PlayDataManager.NotifyLevel.NOTIFY_SERVICE_UI;
import static com.app.media.manager.PlayDataManager.NotifyLevel.NOTIFY_UI;

/**
 * 播放数据管理
 * Created by yinxm on 2018/8/20.
 */
// TODO: 2018/9/9 多线程同步问题list position
public class PlayDataManager implements IPlayData {

    private MediaList mMediaList = new MediaList();
    private int mCurrentPosition;
    private MediaBean mCurrentMediaBean;

    private MediaPlayMode mPlayMode = MediaPlayMode.MODE_LIST_LOOP;


    private MediaPlayListType mPlayListType = MediaPlayListType.DEFAULT_UNKNOWN;

//    MediaPlayListType.UNKNOWN


    //    // 当前音乐播放状态，默认为等待
//    private int targetPlayState = PlayerConstant.STATE_WAIT;//目标播放状态状态

    /**
     * 当前播放状态
     */
    private int mCurrentPlayState = MediaPlayState.STATE_IDLE_STOPPED;

    /**
     * 上一次实际播放状态
     */
    private int oldCurrentPlayState;

    private PlayDataManager() {
    }

    public static PlayDataManager getInstance() {
        return SingletonHolder.INSTANCE;
    }


    private static class SingletonHolder {
        private static PlayDataManager INSTANCE = new PlayDataManager();
    }

    @Override
    public int getPlayListSize() {
        return mMediaList.getList().size();
    }

    @Override
    public MediaList getPlayList() {
        return mMediaList;
    }

    @Override
    public int getPlayListPosition() {
        return mCurrentPosition;
    }

    @Override
    public MediaBean getPlayMediaBean() {
        return mCurrentMediaBean;
    }

    @Override
    public MediaPlayListType getPlayListType() {
        return mPlayListType;
    }

    @Override
    @MediaPlayState
    public int getPlayState() {
        return mCurrentPlayState;
    }

    @Override
    public MediaPlayMode getPlayMode() {
        return mPlayMode;
    }

    @Override
    public boolean isPlaying() {
        return MediaPlayState.STATE_PLAYING == getPlayState()
                || MediaPlayState.STATE_PREPARING == getPlayState();
    }

    @Override
    public boolean isPaused() {
        int playState = getPlayState();
        return playState == MediaPlayState.STATE_PAUSED
                || playState == MediaPlayState.STATE_PAUSED_BY_EVENT;
    }

    @Override
    public int getNextPosition() {
        return getNextPosition(mCurrentPosition);
    }

    private int getNextPosition(int currentPosition) {
        return getInternalNextPosition(currentPosition, false, false);
    }

    @Override
    public int getPrePosition() {
        return getPrePosition(mCurrentPosition);
    }

    private int getPrePosition(int currentPosition) {
        return getInternalNextPosition(currentPosition, true, false);
    }

    @Override
    public IPlayHelper getPlayer() {
        MediaPlayService service = MediaPlayServiceManger.getInstance().getMediaPlayService();
        if (service != null) {
            return service.getPlayer();
        }
        return null;
    }


    /**
     * @param currentPosition
     * @param isPrev          是否是上一首
     * @param isAutoChange    是否是程序自动切换(播放完成后，自动下一首)，区别于用户切换（用户切换，即使是单曲模式，也要切换到下一首）
     * @return
     */
    public int getInternalNextPosition(int currentPosition, boolean isPrev, boolean isAutoChange) {
        int position = -1;
        int size = getPlayListSize();
        if (currentPosition < 0 || currentPosition >= size) {
            return position;
        }

        switch (mPlayMode) {
            case MODE_RANDOM:
                // TODO: 2018/8/30 上一首，下一首能切换回来
                if (size == 1) {
                    position = currentPosition;
                } else {
                    Random random = new Random();
                    int pos = random.nextInt(size);
                    while (pos == currentPosition) {
                        pos = random.nextInt(size);
                    }
                    position = pos;
                }
                break;
            case MODE_SINGLE:
                if (isAutoChange) {
                    position = currentPosition;
                } else {
                    // 用户主动切换到上一首，逻辑同列表循环
                    if (isPrev) {
                        position = currentPosition + size - 1 % size;
                    } else {
                        position = currentPosition + 1 % size;
                    }
                }
                break;
            case MODE_LIST_LOOP:
                if (isPrev) {
                    position = (currentPosition + size - 1) % size;
                } else {
                    position = (currentPosition + 1) % size;
                }
                break;
            default:
                break;
        }
        return position;
    }

    /**
     * 设置 待播放 的指定项数据
     *
     * @param position
     */
    public void setPlayListPosition(int position) {
        if (DataValidCheck.isInValidPosition(position, getPlayListSize())) {
            if (getPlayListSize() > 0) {
                LogUtil.e("传入播放index不合法，默认播放");
                position = 0;
            } else {
                LogUtil.e("传入播放index不合法，当前无可播放资源");
                return;
            }
        }
        mCurrentPosition = position;
        setCurrentMediaBean(mMediaList.getList().get(mCurrentPosition));
    }

    public void setPlayList(MediaList mediaList) {
        if (mediaList == null) {
            mediaList = new MediaList();
        }
        mMediaList = mediaList;
        PlayInfoChangedCallbackManager.getInstance().notifyPlayListChanged(mPlayListType, getPlayList());
    }


    /**
     * 内部接口，UI层不要使用，UI层使用playPosition
     *
     * @param currentMediaBean
     */
    public void setCurrentMediaBean(MediaBean currentMediaBean) {
//        MediaBean oldValue = mCurrentMediaBean;
        mCurrentMediaBean = currentMediaBean;
//        if (mCurrentMediaBean != oldValue) {
        PlayInfoChangedCallbackManager.getInstance().notifyPlayBeanChange(mCurrentPosition, currentMediaBean);
//        }
    }

    public void setPlayMode(MediaPlayMode playMode) {
        if (playMode == null) {
            LogUtil.e("IllegalArgumentException set playMode null");
            return;
        }
        MediaPlayMode oldValue = mPlayMode;
        mPlayMode = playMode;
        if (mPlayMode != oldValue) {
            PlayInfoChangedCallbackManager.getInstance().notifyPlayModeChanged(playMode);
        }
    }

    public void setPlayListType(MediaPlayListType playListType) {
        if (playListType == null) {
            LogUtil.e("IllegalArgumentException set playListType null");
            return;
        }
        mPlayListType = playListType;
    }

    public void setCurrentPlayState(@MediaPlayState int currentPlayState) {
//        int oldValue = mCurrentPlayState;
        mCurrentPlayState = currentPlayState;
        PlayInfoChangedCallbackManager.getInstance()
                .notifyPlayStateChanged(currentPlayState, isPlaying());
    }

    @IntDef({NOTIFY_SERVICE, NOTIFY_UI, NOTIFY_SERVICE_UI})
    @Retention(RetentionPolicy.SOURCE)
    public @interface NotifyLevel {
        int NOTIFY_SERVICE = 1;
        int NOTIFY_UI = 2;
        int NOTIFY_SERVICE_UI = 3;
    }

    /**
     * 是否是普通的多媒体，即不是：蓝牙、收音机
     *
     * @return
     */
    public boolean isNormalMedia() {
        boolean isNormalMedia = true;
        MediaPlayListType mediaPlayListType = getPlayListType();
        if (MediaPlayListType.BT_MUSIC == mediaPlayListType
                || MediaPlayListType.NATIVE_RADIO == mediaPlayListType) {
            isNormalMedia = false;
        }
        return isNormalMedia;
    }

    /**
     * 播放时，是否需要网络
     *
     * @return
     */
    public boolean isPlayNeedNet() {
        boolean isPlayNeedNet = true;
        MediaPlayListType mediaPlayListType = getPlayListType();
        // 本地、蓝牙、收音机不需要网络也能播放
        if (MediaPlayListType.LOCAL == mediaPlayListType
                || MediaPlayListType.BT_MUSIC == mediaPlayListType
                || MediaPlayListType.NATIVE_RADIO == mediaPlayListType) {
            isPlayNeedNet = false;
        }
        return isPlayNeedNet;

    }

}