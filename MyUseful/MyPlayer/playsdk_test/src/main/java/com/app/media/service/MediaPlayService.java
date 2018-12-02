package com.app.media.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.app.media.bean.base.MediaBean;
import com.app.media.manager.AppActivityManager;
import com.app.media.manager.PlayDataManager;
import com.app.media.manager.PlayFeedbackManager;
import com.app.media.manager.PlayInfoChangedCallbackManager;
import com.app.media.manager.UiCallBackManager;
import com.app.media.player.IPlayHelper;
import com.app.media.player.OnPlayerCallback;
import com.app.media.player.PlayerManager;
import com.app.media.player.constant.PlayerType;
import com.app.media.receiver.MediaButtonIntentReceiver;
import com.app.media.sdk.AudioTempPauseRecorder;
import com.app.media.sdk.IMediaNotification;
import com.app.media.sdk.IPlayCallback;
import com.app.media.sdk.IPlayController;
import com.app.media.sdk.IPlayData;
import com.app.media.sdk.IPlayService;
import com.app.media.sdk.MediaPlayState;
import com.app.media.sdk.TaskExecutor;
import com.app.media.sdk.VolumeController;
import com.app.media.sdk.iml.PlayDetailHelper;
import com.app.media.sdk.iml.PlayListRecovery;
import com.app.media.sdk.iml.ServiceOnPlayChangedCallback;
import com.app.media.util.DataValidCheck;
import com.app.media.util.NetworkUtil;
import com.app.media.util.PlayIdUtil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.yinxm.playsdk.test.LogUtil;

import static com.app.media.callback.UiCallBack.SHOW_MSG_CAN_NOT_PLAY;
import static com.app.media.callback.UiCallBack.SHOW_MSG_NET_LOAD_ERROR;
import static com.app.media.callback.UiCallBack.SHOW_MSG_NO_NET;


/**
 * 多媒体播放服务
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/28
 */
public class MediaPlayService extends Service implements IPlayController, IPlayService {
    private static final String TAG = "MediaPlayService";


    private static final int MSG_ACTION_START_PLAY = 1;
    private static final int MSG_ACTION_PLAY = 2;
    private static final int MSG_ACTION_PAUSE = 3;
    private static final int MSG_ACTION_STOP = 4;


    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    private boolean isHaveAudioFocus = false;
    private ComponentName mediaButtonReceive;
    //    private IPlayController mPlayController;
    private IPlayHelper mPlayHelper;
    private IPlayData mPlayData;
    private AudioTempPauseRecorder mTempPauseRecorder;
    private ExecutorService mExecutorService;
    private @PlayerType int userChoosePlayerType = PlayerType.NONE;

    /**
     * Service统一播放信息回调，用于非UI的回调
     */
    private ServiceOnPlayChangedCallback mPlayChangedCallback;

    /**
     * 因为网络播放异常
     */
    private boolean isPlayNetError = false;


    private Handler mPlayActionHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ACTION_START_PLAY:
                    break;
                case MSG_ACTION_PLAY:
                    mPlayHelper.play();
                    break;
                case MSG_ACTION_PAUSE:
                    break;
                case MSG_ACTION_STOP:
                    break;
                default:
                    break;
            }
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mMediaPlayBinder;
    }

    @Override
    public void onCreate() {
        LogUtil.d(TAG, "MediaPlayService onCreate");

        mOnAudioFocusChangeListener = new CustomOnAudioFocusChangeListener();
        mAudioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        mediaButtonReceive = new ComponentName(getPackageName(), MediaButtonIntentReceiver.class.getName());

        mPlayHelper = PlayerManager.getInstance().initDefaultStrategy(getApplicationContext());
        mPlayData = PlayDataManager.getInstance();
        mTempPauseRecorder = new AudioTempPauseRecorder();
        mPlayChangedCallback = new ServiceOnPlayChangedCallback(getApplicationContext(), this);
        PlayInfoChangedCallbackManager.getInstance().setServicePlayChangedCallback(mPlayChangedCallback);
        mExecutorService = new ThreadPoolExecutor(1, 1, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());

        mExecutorService.submit(new Runnable() {
            @Override
            public void run() {
//                PlayListRecovery.getInstance().recoverPlayList(getApplicationContext());
            }
        });


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d(TAG, "onStartCommand");
        return START_STICKY;
    }

    @Override
    public void startPlay() {
        LogUtil.d(TAG, "PlayController——》startPlay");

        mTempPauseRecorder.clearAllTempPauseMusicEvent();
        if (!NetworkUtil.isNetworkConnected(this) && PlayDataManager.getInstance().isPlayNeedNet()) {
            UiCallBackManager.getInstance().getUiCallBackToast().show(SHOW_MSG_NO_NET);
        }

        if (mPlayData.getPlayMediaBean() != null) {
            UiCallBackManager.getInstance().getUiCallBackDialog().hide();
            final MediaBean mediaBean = mPlayData.getPlayMediaBean();
            try {

                TaskExecutor.io(new Runnable() {
                    @Override
                    public void run() {
                        boolean isUpdate = PlayDetailHelper.updateMediaDetail(mediaBean, mPlayData.getPlayListType(),
                                true);
                        TaskExecutor.ui(new Runnable() {
                            @Override
                            public void run() {
                                UiCallBackManager.getInstance().getUiCallBackDialog().hide();
                                if (mediaBean == null || TextUtils.isEmpty(mediaBean.getPlayUrl())) {
                                    UiCallBackManager.getInstance().getUiCallBackToast().show(SHOW_MSG_NET_LOAD_ERROR);
                                }
                            }
                        });

                        LogUtil.d(TAG, "isUpdate=" + isUpdate + ", mediaBean=" + mediaBean);
                        if (mediaBean == null || TextUtils.isEmpty(mediaBean.getPlayUrl())) {
                            return;
                        }

                        if (isUpdate) {
                            PlayDataManager.getInstance().setCurrentMediaBean(mediaBean);
                        }
                        String playUrl = mediaBean.getPlayUrl();

                        if (!requestAudioFocus()) {
                            return;
                        }

                        IPlayHelper playHelper = PlayerManager.getInstance().getPlayer(mPlayHelper, playUrl);
                        if (playHelper != null && playHelper != mPlayHelper) {
                            try {
                                LogUtil.e(TAG, "change player from " + mPlayHelper + " to " + playHelper);
                                if (mPlayHelper != null) {
                                    mPlayHelper.stop();
                                }
                            } catch (Exception e) {
                                LogUtil.e(TAG, e);
                            }
                            mPlayHelper = playHelper;
                        }
                        LogUtil.d(TAG, "mPlayHelper=" + mPlayHelper + ", playUrl=" + playUrl);

                        ((PlayDataManager) mPlayData).setCurrentPlayState(MediaPlayState.STATE_PREPARING);
                        mPlayHelper.play(playUrl, mOnPlayerCallback);
                    }
                });


            } catch (Exception e) {
                LogUtil.e(e);
            }
        } else {
            // TODO: 2018/9/23 test
            LogUtil.e(TAG, "没有指定播放index，进行默认播放项");
            int position = mPlayData.getPlayListPosition();
            if (DataValidCheck.isInValidPosition(position, mPlayData.getPlayListSize())) {
                if (mPlayData.getPlayListSize() > 0) {
                    position = 0;
                } else {
                    return;
                }
            }
            playPosition(position);
        }

    }

    @Override
    public IPlayController getPlayController() {
        return this;
    }

    @Override
    public IPlayData getCurrentPlayInfo() {
        return PlayDataManager.getInstance();
    }

    @Override
    public IPlayCallback getPlayCallback() {
        return PlayInfoChangedCallbackManager.getInstance();
    }

    @Override
    public IPlayHelper getPlayer() {
        return mPlayHelper;
    }

    @Override
    public boolean isHaveAudioFocus() {
        return isHaveAudioFocus;
    }

    @Override
    public boolean isPlaying() {
        return mPlayData.isPlaying();
    }

    @Override
    public boolean isPaused() {
        return mPlayData.isPaused();
    }

    @Override
    public IMediaNotification getMediaNotification() {
        return null;
    }

    @Override
    public void play() {
        LogUtil.d(TAG, "PlayController——》play");

        mTempPauseRecorder.clearAllTempPauseMusicEvent();
        if (isPaused()) {
            if (requestAudioFocus()) {
                ((PlayDataManager) mPlayData).setCurrentPlayState(MediaPlayState.STATE_PLAYING);
                mPlayHelper.play();
            } else {
                playPosition(mPlayData.getPlayListPosition());
            }
        } else if (isPlaying() && isPlayUrlChange()) {
            // 播放url相同
            LogUtil.e("is already playing");
        } else {
            // 停止状态 - 》重新播放
            startPlay();
        }
    }

    @Override
    public void pause() {
        pause(true);
    }

    /**
     * 用户手动暂停
     *
     * @param isAbandFocus
     */
    private void pause(boolean isAbandFocus) {
        LogUtil.d(TAG, "PlayController——》pause " + isAbandFocus);
        mTempPauseRecorder.clearAllTempPauseMusicEvent();
        if (mPlayHelper.isPlaying()) {
            mPlayHelper.pause();
            if (isAbandFocus) {
                abandAudioFocus();
            }
        }
        ((PlayDataManager) mPlayData).setCurrentPlayState(MediaPlayState.STATE_PAUSED);
    }

    @Override
    public int pauseByEvent(String eventName) {
        return pauseByEvent(eventName, true);
    }

    public int pauseByEvent(String eventName, boolean isAbandFocus) {
        LogUtil.d(TAG, "PlayController——》pauseByEvent " + eventName + ", " + isAbandFocus);

        int pauseId = 0;
        // 播放中、已经被打断都可以再次响应中断
        if (isPlaying() || mPlayData.getPlayState() == MediaPlayState.STATE_PAUSED_BY_EVENT) {
            pauseId = mTempPauseRecorder.pauseMusic(eventName);
            if (mPlayHelper.isPlaying()) {
                mPlayHelper.pause();
                ((PlayDataManager) mPlayData).setCurrentPlayState(MediaPlayState.STATE_PAUSED_BY_EVENT);
                if (isAbandFocus) {
                    abandAudioFocus();
                }
            }
        }
        return pauseId;
    }

    @Override
    public boolean resumeByEvent(int pauseId) {
        LogUtil.d(TAG, "PlayController——》resumeByEvent " + pauseId);
        boolean flag = false;
        if (isPaused()) {
            flag = mTempPauseRecorder.resumeMusic(pauseId);
            // 需要恢复播放
            if (flag) {
                play();
            }
        } else {
            // 已经在播放了
            mTempPauseRecorder.clearAllTempPauseMusicEvent();
        }

        return flag;
    }

    @Override
    public void playNext() {
        playNext(false);
    }


    /**
     * @param isAutoChange 是否是程序自动切换到下一首
     */
    private void playNext(boolean isAutoChange) {
        int pos = mPlayData.getNextPosition();
        LogUtil.d(TAG, "PlayController——》playNext " + isAutoChange + ", nextPos=" + pos
                + ", currentPos=" + mPlayData.getPlayListPosition());
        if (DataValidCheck.isInValidPosition(pos, mPlayData.getPlayListSize())) {
            return;
        }
        ((PlayDataManager) mPlayData).setPlayListPosition(pos);
        startPlay();
        PlayFeedbackManager.getInstance().sendFeedback(PlayFeedbackManager.TYPE_NEXT, mPlayData.getPlayMediaBean(),
                mPlayHelper.getCurrentPosition(), mPlayHelper.getDuration());
    }


    @Override
    public void playPrevious() {
        int pos = mPlayData.getPrePosition();
        LogUtil.d(TAG, "PlayController——》playPrevious prevPos " + pos);
        if (DataValidCheck.isInValidPosition(pos, mPlayData.getPlayListSize())) {
            return;
        }
        ((PlayDataManager) mPlayData).setPlayListPosition(pos);
        startPlay();
        PlayFeedbackManager.getInstance().sendFeedback(PlayFeedbackManager.TYPE_PREV, mPlayData.getPlayMediaBean(),
                mPlayHelper.getCurrentPosition(), mPlayHelper.getDuration());
    }

    @Override
    public void playPosition(int position) {
        LogUtil.d(TAG, "PlayController——》playPosition " + position);
        if (DataValidCheck.isInValidPosition(position, mPlayData.getPlayListSize())) {
            return;
        }
        ((PlayDataManager) mPlayData).setPlayListPosition(position);
        startPlay();
    }

    @Override
    public void seekTo(long positionMs) {
        LogUtil.d(TAG, "PlayController——》seekTo " + positionMs);
        // TODO: 2018/8/30 check拖动播放状态改变
        mPlayHelper.seekTo(positionMs);
    }

    @Override
    public void stop() {
        LogUtil.d(TAG, "PlayController——》stop");
        ((PlayDataManager) mPlayData).setCurrentPlayState(MediaPlayState.STATE_IDLE_STOPPED);
        mPlayHelper.stop();
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
    }

    @Override
    public void exit() {
        LogUtil.d(TAG, "PlayController——》exit");
        stopSelf();
        AppActivityManager.getInstance().exitApp(true);
    }

    public int getUserChoosePlayerType() {
        return userChoosePlayerType;
    }

    public void setUserChoosePlayerType(@PlayerType int userChoosePlayerType) {
        this.userChoosePlayerType = userChoosePlayerType;
    }

    /**
     * 播放异常停止：因为无网络等原因
     */
    private void stopByNetError() {
        LogUtil.d(TAG, "stopByNetError");
        if (mPlayHelper.isPlaying()) {
            mPlayHelper.pause();
            abandAudioFocus();
        }
        ((PlayDataManager) mPlayData).setCurrentPlayState(MediaPlayState.STATE_PAUSED);
    }

    private boolean requestAudioFocus() {
//        LogUtil.d(TAG, "requestAudioFocus before isHaveAudioFocus=" + isHaveAudioFocus);
        int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);
        isHaveAudioFocus = AudioManager.AUDIOFOCUS_REQUEST_GRANTED == result;
        LogUtil.d(TAG, "requestAudioFocus after isHaveAudioFocus=" + isHaveAudioFocus);
        return isHaveAudioFocus;
    }

    private void abandAudioFocus() {
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        isHaveAudioFocus = false;
    }

    private boolean isPlayUrlChange() {
        boolean flag = false;
        if (mPlayData.getPlayMediaBean() != null &&
                mPlayData.getPlayMediaBean().getPlayUrl() != null
                && mPlayData.getPlayMediaBean().getPlayUrl().equals(getPlayer().getPlayUrl())) {
            flag = true;
        }
        LogUtil.d(TAG, "isPlayUrlChange=" + flag);
        return flag;
    }


    private class CustomOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
        VolumeController mVolumeController = new VolumeController();
        private int focusPauseId;
        private boolean isDucking = false;

        @Override
        public void onAudioFocusChange(int focusChange) {
            LogUtil.d(TAG, "onAudioFocusChange  focusChange=" + focusChange + ", before isHaveAudioFocus=" +
                    isHaveAudioFocus);
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    isHaveAudioFocus = false;
                    mAudioManager.unregisterMediaButtonEventReceiver(mediaButtonReceive);
                    pause(true);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    isHaveAudioFocus = false;
                    focusPauseId = pauseByEvent(AudioManager.class.getCanonicalName(), false);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // 降低音量
                    if (!isDucking) {
                        isDucking = true;
                        mVolumeController.lowerVolume(getPlayer());
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    isHaveAudioFocus = true;
                    mAudioManager.registerMediaButtonEventReceiver(mediaButtonReceive);
                    if (isDucking) {
                        isDucking = false;
                        mVolumeController.resumeVolume();
                    } else {
//                        mVolumeController.resetVolume();
                        resumeByEvent(focusPauseId);
                    }
                    break;
                case AudioManager.AUDIOFOCUS_REQUEST_FAILED:
                    // TODO: 2018/8/31  
//                            if (isPlaying()) {
//                                pause();
//                            }
                    break;
                default:
                    break;
            }
        }
    }

    // TODO: 2018/8/30  
    OnPlayerCallback mOnPlayerCallback = new OnPlayerCallback() {

        @Override
        public void onPlayerState(int state) {
            switch (state) {
                case State.STATE_BUFFERING:
                    LogUtil.d(TAG, "onPlayerState STATE_BUFFERING ");
                    break;
                case State.STATE_READY:
                    LogUtil.d(TAG, "onPlayerState STATE_READY ");
                    UiCallBackManager.getInstance().getUiCallBackDialog().hide();

                    // TODO: 2018/9/5 检查是否能播放
                    int playState = mPlayData.getPlayState();
                    if (isHaveAudioFocus
                            && (MediaPlayState.STATE_PLAYING == playState
                            || MediaPlayState.STATE_PREPARING == playState)) {
                        // 播放、缓冲中都能播放
                        ((PlayDataManager) mPlayData).setCurrentPlayState(MediaPlayState.STATE_PLAYING);

                        // 检查播放断点
                        long progress = 0;
                        if (PlayListRecovery.getInstance().isBeanCanSaveProgress(mPlayData.getPlayMediaBean())) {
                            progress = PlayListRecovery.getInstance().getPlayHistoryProgress(getApplicationContext(),
                                    PlayIdUtil.getPlayingId(getPlayer()));
                        }
                        if (progress > 0) {
                            mPlayHelper.seekTo(progress);
                        }
                        mPlayHelper.play();

                    } else {
                        LogUtil.d(TAG, "user canceled play");
                        if (mPlayHelper.isPlaying()) {
                            mPlayHelper.pause();
                        }
                    }
                    // 统计用
                    PlayFeedbackManager.getInstance().sendFeedback(PlayFeedbackManager.TYPE_START, mPlayData.getPlayMediaBean(),
                            mPlayHelper.getCurrentPosition(), mPlayHelper.getDuration());

                    break;

                case State.STATE_ENDED:
                    LogUtil.d(TAG, "onPlayerState STATE_ENDED ");

                    // 统计用
                    PlayFeedbackManager.getInstance().sendFeedback(PlayFeedbackManager.TYPE_FINISH, mPlayData.getPlayMediaBean(),
                            mPlayHelper.getCurrentPosition(), mPlayHelper.getDuration());
                    PlayFeedbackManager.getInstance().sendAudioFinishFeedback(
                            mPlayData.getPlayListType(), mPlayData.getPlayMediaBean());
                    UiCallBackManager.getInstance().getUiCallBackDialog().hide();
                    playNext(true);
                    break;
                case State.STATE_IDLE:
                    LogUtil.d(TAG, "onPlayerState TATE_IDLE ");
                    UiCallBackManager.getInstance().getUiCallBackDialog().hide();
                    LogUtil.d(TAG, "无法播放，自动下一首");
                    playNext(true);
                    break;
                default:
                    LogUtil.d(TAG, "onPlayerState " + state);
                    break;

            }
        }

        @Override
        public void onPlayError(IPlayHelper playHelper, String playUrl, String errorMsg) {
            LogUtil.d(TAG, "onPlayError isNetConnected=" + NetworkUtil.isNetworkConnected(getApplicationContext())
                    + "， playUrl=" + playUrl + ", errorMsg=" + errorMsg);
            // 无网络的情况下出现的播放异常，不要切换播放器
            if (NetworkUtil.isNetworkConnected(getApplicationContext())) {
                IPlayHelper nextPlayer = PlayerManager.getInstance().getNextPlayer(playHelper, playUrl);
                LogUtil.d(TAG, "nextPlayer=" + nextPlayer + ", oldPlayer=" + playHelper);
                if (nextPlayer != null) {
                    if (mPlayHelper != null) {
                        mPlayHelper.stop();
                    }
                    mPlayHelper = nextPlayer;
                    mPlayHelper.play(playUrl, this);
                } else {
                    isPlayNetError = true;
                    UiCallBackManager.getInstance().getUiCallBackToast().show(SHOW_MSG_CAN_NOT_PLAY);
                    // 播放异常，自动播放下一首
                    playNext();
                }
            } else {
                isPlayNetError = true;
                UiCallBackManager.getInstance().getUiCallBackToast().show(SHOW_MSG_NO_NET);
                stopByNetError();
            }
        }
    };

    private IBinder mMediaPlayBinder = new MediaPlayBinder();

    public class MediaPlayBinder extends Binder {
        public MediaPlayService getService() {
            return MediaPlayService.this;
        }
    }

}
