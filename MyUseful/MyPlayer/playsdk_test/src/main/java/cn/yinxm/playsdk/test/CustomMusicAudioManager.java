package cn.yinxm.playsdk.test;

import android.content.ComponentName;
import android.content.Context;
import android.media.AudioManager;

import static android.content.Context.AUDIO_SERVICE;

/**
 * 蓝牙音乐 音频焦点管理
 *
 * todo 测试，蓝牙降音未处理；
 * 改成不用焦点
 * <p>
 *
 * @author yinxuming
 * @date 2018/10/30
 */
public class CustomMusicAudioManager {
    private static final String TAG = "CustomMusicAudioManager";

    private Context mContext;
    private AudioManager mAudioManager;
    private AudioManager.OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    private ComponentName mediaButtonReceive;
    private boolean isHaveAudioFocus = false;

    private CustomMusicAudioManager() {
    }

    public static CustomMusicAudioManager getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static CustomMusicAudioManager INSTANCE = new CustomMusicAudioManager();
    }

    public void init(Context context) {
        mContext = context.getApplicationContext();
//        mediaButtonReceive = new ComponentName(mContext.getPackageName(), MediaButtonIntentReceiver.class.getName());
        mOnAudioFocusChangeListener = new CustomOnAudioFocusChangeListener();
        mAudioManager = (AudioManager) mContext.getSystemService(AUDIO_SERVICE);
    }


    public boolean requestAudioFocus() {
        LogUtil.d(TAG, "CustomMusicAudioManager.requestAudioFocus before isHaveAudioFocus=" + isHaveAudioFocus);
        if (!isHaveAudioFocus) {
            int result = mAudioManager.requestAudioFocus(mOnAudioFocusChangeListener, AudioManager.STREAM_MUSIC,
                    AudioManager.AUDIOFOCUS_GAIN);
            isHaveAudioFocus = AudioManager.AUDIOFOCUS_REQUEST_GRANTED == result;
            LogUtil.d(TAG, "requestAudioFocus after isHaveAudioFocus=" + isHaveAudioFocus);
        }
        return isHaveAudioFocus;
    }

    public void abandAudioFocus() {
        LogUtil.d(TAG, "abandAudioFocus before isHaveAudioFocus=" + isHaveAudioFocus);
        mAudioManager.abandonAudioFocus(mOnAudioFocusChangeListener);
        isHaveAudioFocus = false;
    }

    public boolean isHaveAudioFocus() {
        return isHaveAudioFocus;
    }

    public void setHaveAudioFocus(boolean haveAudioFocus) {
        isHaveAudioFocus = haveAudioFocus;
    }

    private class CustomOnAudioFocusChangeListener implements AudioManager.OnAudioFocusChangeListener {
//        VolumeController mVolumeController = new VolumeController();
        private int focusPauseId;
        private boolean isDucking = false;

        @Override
        public void onAudioFocusChange(int focusChange) {
            LogUtil.d(TAG, "onAudioFocusChange  focusChange="
                    + focusChange + ", before isHaveAudioFocus=" + isHaveAudioFocus);
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                    isHaveAudioFocus = false;
                    mAudioManager.unregisterMediaButtonEventReceiver(mediaButtonReceive);
                    // TODO: 2018/10/30  
//                    BtMusicPlayControlManager.getInstance().pause(true);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                    isHaveAudioFocus = false;

//                    BtMusicPlayControlManager.getInstance().pause(false);
                    // TODO: 2018/10/30  
//                    focusPauseId = pauseByEvent(AudioManager.class.getCanonicalName(), false);
                    break;
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    // 降低音量
                    if (!isDucking) {
                        isDucking = true;
//                        mVolumeController.lowerVolume(getPlayer());
                    }
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    isHaveAudioFocus = true;
                    mAudioManager.registerMediaButtonEventReceiver(mediaButtonReceive);
                    if (isDucking) {
                        isDucking = false;
//                        mVolumeController.resumeVolume();
                    } else {
//                        mVolumeController.resetVolume();
//                        resumeByEvent(focusPauseId);
//                        BtMusicPlayControlManager.getInstance().play();
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

}
