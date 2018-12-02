package com.app.media.sdk;


import android.os.Handler;
import android.os.Message;

import com.app.media.player.IPlayHelper;

import cn.yinxm.playsdk.test.LogUtil;


/**
 * <p>
 *
 * @author yinxuming
 * @date 2018/8/30
 */
public class VolumeController extends Handler {
    /**
     * 降低音量
     */
    public static final int MSG_VOLUME_DOWN = 1;
    /**
     * 增大音量
     */
    public static final int MSG_VOLUME_UP = 2;
    /**
     * 恢复音量
     */
    public static final int MSG_VOLUME_RESET = 3;


    private static final float VOLUME_LOW = 0.2f;


    IPlayHelper mPlayHelper;

    private float mCurrentVolume = 1.0f;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case MSG_VOLUME_DOWN:
                mCurrentVolume -= 0.05f;
                if (mCurrentVolume > VOLUME_LOW) {
                    // 480ms
                    sendEmptyMessageDelayed(MSG_VOLUME_DOWN, 30);
                } else {
                    mCurrentVolume = VOLUME_LOW;
                }
                setPlayerVolume(mCurrentVolume);
                break;
            case MSG_VOLUME_UP:
                mCurrentVolume += 0.01f;
                if (mCurrentVolume < 1.0f) {
                    // 2s 400ms
                    sendEmptyMessageDelayed(MSG_VOLUME_UP, 30);
                } else {
                    mCurrentVolume = 1.0f;
                }
                setPlayerVolume(mCurrentVolume);
                break;
            case MSG_VOLUME_RESET:
                mCurrentVolume = 1.0f;
                setPlayerVolume(mCurrentVolume);
                break;
            default:
                break;

        }

    }

    /**
     * 降低音量
     *
     * @param playHelper
     */
    public void lowerVolume(IPlayHelper playHelper) {
        mPlayHelper = playHelper;
        removeMessages(MSG_VOLUME_UP);
        removeMessages(MSG_VOLUME_DOWN);
        sendEmptyMessage(MSG_VOLUME_DOWN);
    }

    /**
     * 恢复音量
     */
    public void resumeVolume() {
        removeMessages(MSG_VOLUME_DOWN);
        removeMessages(MSG_VOLUME_UP);
        sendEmptyMessage(MSG_VOLUME_UP);
    }

    /**
     * 重置音量
     */
    public void resetVolume() {
        removeCallbacksAndMessages(null);
        sendEmptyMessage(MSG_VOLUME_RESET);
    }

    /**
     * 设置播放器音量
     *
     * @param volume
     */
    public void setPlayerVolume(float volume) {
        try {
            LogUtil.d("setPlayerVolume volume=" + volume);
            if (mPlayHelper != null) {
                mPlayHelper.setVolume(volume);
            }
        } catch (Exception e) {
            LogUtil.e(e);
        }
    }
}
