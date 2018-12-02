package com.app.media.sdk.iml;

import android.content.Context;
import android.content.Intent;

import com.app.media.bean.base.MediaBean;
import com.app.media.sdk.MediaPlayState;
import com.app.media.vcr.VoiceCmdSendConstant;


/**
 * 通过广播对外发送通知信息
 * <p>
 *
 * @author yinxuming
 * @date 2018/9/21
 */
public class BroadcastNotifyUtil {

    /**
     * 通知播放状态
     *
     * @param context
     * @param state
     * @param isPlaying
     */
    public static void notifyPlayState(Context context, @MediaPlayState int state, boolean isPlaying) {
        Intent intent = new Intent(VoiceCmdSendConstant.NOTIFY_PLAY_STATE);
//        1：BUFFERING
//        2：PLAYING
//        3：PAUSE
//        4：STOP
        int notifyState = 4;
        switch (state) {
            case MediaPlayState.STATE_PREPARING:
                notifyState = 1;
                break;
            case MediaPlayState.STATE_PLAYING:
                notifyState = 2;
                break;
            case MediaPlayState.STATE_PAUSED:
            case MediaPlayState.STATE_PAUSED_BY_EVENT:
                notifyState = 3;
                break;
            case MediaPlayState.STATE_IDLE_STOPPED:
                break;
            default:
                break;
        }
        intent.putExtra(VoiceCmdSendConstant.KEY_PLAY_STATE, notifyState);
        context.sendBroadcast(intent);
    }

    /**
     * 通知播放信息
     *
     * @param context
     * @param bean
     */
    public static void notifyPlayInfo(Context context, final MediaBean bean,
                                      long duration, long progress, boolean isPlaying) {
        Intent intent = new Intent(VoiceCmdSendConstant.NOTIFY_PLAY_INFO);

        intent.putExtra(VoiceCmdSendConstant.KEY_ID, bean.getId());
        intent.putExtra(VoiceCmdSendConstant.KEY_TITLE, bean.getTitle());
        intent.putExtra(VoiceCmdSendConstant.KEY_ARTIST, bean.getArtist());
        intent.putExtra(VoiceCmdSendConstant.KEY_ALBUM_NAME, bean.getAlbumName());
        intent.putExtra(VoiceCmdSendConstant.KEY_COVER, bean.getCover());
        intent.putExtra(VoiceCmdSendConstant.KEY_DURATION, duration);
        intent.putExtra(VoiceCmdSendConstant.KEY_PROGRESS, progress);
        intent.putExtra(VoiceCmdSendConstant.KEY_IS_PLAYING, isPlaying);
        context.sendBroadcast(intent);
    }

}
