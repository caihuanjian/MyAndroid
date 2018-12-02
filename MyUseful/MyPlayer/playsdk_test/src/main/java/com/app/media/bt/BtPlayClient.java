package com.app.media.bt;

import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.text.TextUtils;

import com.app.media.bean.base.MediaBean;
import com.app.media.sdk.IPlayController;
import com.app.media.sdk.IPlayData;

import cn.yinxm.playsdk.test.LogUtil;

/**
 * 蓝牙播放客户端：连上蓝牙后将正在播放的信息发送给服务端
 * Created by yinxm on 2018/8/20.
 */

public class BtPlayClient {


    private static final String TAG = "MediaSessionManagers";

    private Context mContext;
    IPlayController mPlayController;
    IPlayData mPlayInfo;

    //音频管理器
    private AudioManager audioManager;
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener;
    private ComponentName mediaButtonReceiveCompName;
    private MediaSessionCompat mMediaSession;
    private PendingIntent mPendingIntent;
    private Handler mHandler;

    //指定可以接收的来自锁屏页面的按键信息
    private static final long MEDIA_SESSION_ACTIONS =
            PlaybackStateCompat.ACTION_PLAY
                    | PlaybackStateCompat.ACTION_PAUSE
                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                    | PlaybackStateCompat.ACTION_STOP
                    | PlaybackStateCompat.ACTION_SEEK_TO
            //                    | PlaybackStateCompat.ACTION_FAST_FORWARD
            ;

    public BtPlayClient(Context context, IPlayController playController, IPlayData playInfo) {
        this.mContext = context;
        this.mPlayController = playController;
        this.mPlayInfo = playInfo;
    }

    /**
     * 初始化并激活 MediaSession
     */
    public void initMediaSession() {
        //        第二个参数 tag: 这个是用于调试用的,随便填写即可
        mMediaSession = new MediaSessionCompat(mContext, TAG);// Caused by: java.lang.IllegalArgumentException: MediaButtonReceiver component may not be null.
        //指明支持的按键信息类型
        mMediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS
                        | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
        );
        mMediaSession.setCallback(callback);
        mMediaSession.setActive(true);

    }


    /**
     * 更新播放状态， 播放／暂停
     */
    public void updatePlaybackState() {
        int state = mPlayInfo.isPlaying() ? PlaybackStateCompat.STATE_PLAYING:
                PlaybackStateCompat.STATE_PAUSED;

        mMediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
                .setActions(MEDIA_SESSION_ACTIONS)
                .setState(state, mPlayInfo.getPlayListPosition(), 1)
                .build());
    }

    /**
     * 更新正在播放的音乐信息，切换歌曲时调用
     */
    public void updatePlayInfo() {
        LogUtil.d("updatePlayInfo");
        final MediaBean mediaBean = mPlayInfo.getPlayMediaBean();
        if (mediaBean == null) {
            LogUtil.d("mediaBean is null");
            return;
        }
//        if (!mMediaSession.isActive()) {
//            mMediaSession.setActive(true);
//        }
        //同步当前的播放状态和播放时间
        PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();

        int state = PlaybackStateCompat.STATE_STOPPED;
        if (mPlayInfo.isPlaying()) {
            state = PlaybackStateCompat.STATE_PLAYING;
        } else if (mPlayInfo.isPaused()) {
            state = PlaybackStateCompat.STATE_PAUSED;
        } else {
            state = PlaybackStateCompat.STATE_STOPPED;
        }
        stateBuilder.setState(state, mPlayInfo.getPlayListPosition(), 1.0f);
        mMediaSession.setPlaybackState(stateBuilder.build());

        //同步歌曲信息
        MediaMetadataCompat.Builder md = new MediaMetadataCompat.Builder();
        md.putString(MediaMetadataCompat.METADATA_KEY_TITLE, mediaBean.getTitle());
        md.putString(MediaMetadataCompat.METADATA_KEY_ARTIST, mediaBean.getArtist());
        md.putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, mediaBean.getArtist());
        md.putString(MediaMetadataCompat.METADATA_KEY_ALBUM, mediaBean.getAlbumName());
        md.putLong(MediaMetadataCompat.METADATA_KEY_DURATION, mediaBean.getDuration());

        md.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, getCoverBitmap(mediaBean));
//        md.putBitmap(MediaMetadataCompat.METADATA_KEY_ART, mAlbumCover);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            md.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, mPlayInfo.getPlayListSize());
        }
        mMediaSession.setMetadata(md.build());
    }


    //根据QueueItem构造MediaMetadata
    private MediaMetadataCompat buildMetadata(MediaBean bean) {

        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, bean.getId())  //歌曲ID
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, bean.getTitle())       //歌曲标题
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, bean.getArtist())     //歌曲作者
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, bean.getAlbumName())   //专辑标题
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, bean.getPlayUrl())  //歌曲播放地址
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, bean.getDuration())  //歌曲时长
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, bean.getCover())   //歌曲插图-高清
                // TODO: 2018/8/21  
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, bean.getLrcUrl())  //歌词链接
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, bean.getThumb())  //歌曲插图-普通
                .putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, mPlayInfo.getPlayListSize())       //播放列表数量
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, mPlayInfo.getPlayListPosition())       //歌曲在播放列表的位置
                .build();
    }




    private Bitmap getCoverBitmap(MediaBean mediaBean) {
        if (!TextUtils.isEmpty(mediaBean.getCover())) {
//            return BitmapFactory.decodeFile(info.getAlbum_path());
            return null;
        } else {
//            return BitmapFactory.decodeResource(context.getResources(), R.drawable.default_song);

            return null;
        }
    }

    /**
     * 释放MediaSession，退出播放器时调用
     */
    public void release() {
        mMediaSession.setCallback(null);
        mMediaSession.setActive(false);
        mMediaSession.release();
    }


    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            return super.onMediaButtonEvent(mediaButtonEvent);
        }
    };



//
//    private static final String TAG = "BtPlayClient";
//
//    //指定可以接收的来自锁屏页面的按键信息
//    private static final long MEDIA_SESSION_ACTIONS =
//            PlaybackStateCompat.ACTION_PLAY
//                    | PlaybackStateCompat.ACTION_PAUSE
//                    | PlaybackStateCompat.ACTION_PLAY_PAUSE
//                    | PlaybackStateCompat.ACTION_SKIP_TO_NEXT
//                    | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
//                    | PlaybackStateCompat.ACTION_STOP
//                    | PlaybackStateCompat.ACTION_SEEK_TO;
//
//
//    private IPlayController control;
//    private final Context context;
//    private MediaSessionCompat mMediaSession;
//    private final PlayInfoManager playInfoManager;
//
//    public BtPlayClient(IPlayController control, Context context) {
//        this.control = control;
//        this.context = context;
//        this.playInfoManager = mPlayInfo;
//        setupMediaSession();
//    }
//
//    /**
//     * 初始化并激活 MediaSession
//     */
//    private void setupMediaSession() {
////        第二个参数 tag: 这个是用于调试用的,随便填写即可
//        mMediaSession = new MediaSessionCompat(context, TAG);
//        //指明支持的按键信息类型
//        mMediaSession.setFlags(
//                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
//                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
//        );
//        mMediaSession.setCallback(callback);
//        mMediaSession.setActive(true);
//    }
//
//    /**
//     * 更新播放状态， 播放／暂停／拖动进度条时调用
//     */
//    public void updatePlaybackState() {
//        int state = isPlaying() ? PlaybackStateCompat.STATE_PLAYING :
//                PlaybackStateCompat.STATE_PAUSED;
//
//        mMediaSession.setPlaybackState(new PlaybackStateCompat.Builder()
//                .setActions(MEDIA_SESSION_ACTIONS)
//                .setState(state, getCurrentPosition(), 1)
//                .build());
//    }
//
//    private long getCurrentPosition() {
//        try {
//            return control.get();
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            return 0;
//        }
//    }
//
//    /**
//     * 是否在播放
//     *
//     * @return
//     */
//    protected boolean isPlaying() {
//        try {
//            return control.status() == PlayController.STATUS_PLAYING;
//        } catch (RemoteException e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    /**
//     * 更新正在播放的音乐信息，切换歌曲时调用
//     */
//    public void updateMetaData(String path) {
//        if (!StringUtils.isReal(path)) {
//            mMediaSession.setMetadata(null);
//            return;
//        }
//        MediaBean songInfo = playInfoManager.getSongInfo(context, path);
//
//        MediaMetadataCompat.Builder metaDta = new MediaMetadataCompat.Builder()
//                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, songInfo.getTitle())
//                .putString(MediaMetadataCompat.METADATA_KEY_ARTIST, songInfo.getArtist())
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, songInfo.getAlbum())
//                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ARTIST, songInfo.getArtist())
//                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, songInfo.getDuration())
//                .putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, getCoverBitmap(songInfo));
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            metaDta.putLong(MediaMetadataCompat.METADATA_KEY_NUM_TRACKS, getCount());
//        }
//        mMediaSession.setMetadata(metaDta.build());
//
//    }
//
//    private long getCount() {
//            return playInfoManager.getPlayListSize();
//    }
//
//    private Bitmap getCoverBitmap(MediaBean bean) {
//        if (!TextUtils.isEmpty(bean.cover)) {
//            return BitmapFactory.decodeFile(bean.cover);
//        } else {
//            // TODO: 2018/8/20
//            return BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
//        }
//    }
//
//
//    /**
//     * 释放MediaSession，退出播放器时调用
//     */
//    public void release() {
//        mMediaSession.setCallback(null);
//        mMediaSession.setActive(false);
//        mMediaSession.release();
//    }
//
//
//    /**
//     * API 21 以上 耳机多媒体按钮监听 MediaSessionCompat.Callback
//     */
//    private MediaSessionCompat.Callback callback = new MediaSessionCompat.Callback() {
//
////        接收到监听事件，可以有选择的进行重写相关方法
//
//        @Override
//        public void onPlay() {
//
//        }
//
//        @Override
//        public void onPause() {
//        }
//
//        @Override
//        public void onSkipToNext() {
//        }
//
//        @Override
//        public void onSkipToPrevious() {
//        }
//
//        @Override
//        public void onStop() {
//                control.pause();
//
//        }
//
//        @Override
//        public void onSeekTo(long pos) {
//
//        }
//    };

}
