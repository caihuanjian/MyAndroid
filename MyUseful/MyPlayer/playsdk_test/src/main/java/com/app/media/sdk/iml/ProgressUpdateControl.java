package com.app.media.sdk.iml;

import android.content.Context;
import android.content.SharedPreferences;

import com.app.media.manager.PlayInfoChangedCallbackManager;
import com.app.media.player.IPlayHelper;
import com.app.media.util.PlayIdUtil;
import com.app.media.util.SpUtil;

import java.util.Timer;
import java.util.TimerTask;

import cn.yinxm.playsdk.test.LogUtil;

import static com.app.media.sdk.iml.PlayListRecovery.FILE_NAME_PLAY_LIST_CONFIG;


/**
 * 播放进度回调
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/3
 */
public class ProgressUpdateControl {

    long initialDelay = 0L;
    long period = 1000L;

    private Context mContext;
    private Timer mTimer;
    private TimerTask mTimerTask;

    public ProgressUpdateControl(Context context) {
        mContext = context;
        mTimer = new Timer();
    }

    public void updateProgress(boolean isPlaying, IPlayHelper playHelper) {
        // TODO: 2018/9/5 check
//        if (playHelper != null && playHelper.isPlaying()) {
        if (isPlaying) {
            startProgressCallback(playHelper);
        } else {
            stopProgressCallback();
        }
    }

    private void stopProgressCallback() {
        if (mTimerTask != null) {
            mTimerTask.cancel();
        }
        mTimerTask = null;
    }

    private void startProgressCallback(final IPlayHelper playHelper) {
        if (mTimerTask == null) {
            final SharedPreferences sp = SpUtil.getSharedPreferences(mContext, FILE_NAME_PLAY_LIST_CONFIG);
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    try {
                        if (playHelper.isPlaying()) {
                            long duration = playHelper.getDuration();
                            long progress = playHelper.getCurrentPosition();
                            long bufferedProgress = playHelper.getBufferedPosition();
                            // 记录播放进度
                            PlayListRecovery.getInstance().saveProgress(sp, PlayIdUtil.getPlayingId(playHelper), duration, progress);
                            // 回调
                            PlayInfoChangedCallbackManager.getInstance()
                                    .notifyProgressChanged(duration, progress, bufferedProgress);
                        }
                    } catch (Exception e) {
                        LogUtil.e(e);
                    }
                }
            };
            mTimer.schedule(mTimerTask, initialDelay, period);
        }
    }
}
