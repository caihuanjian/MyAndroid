
package com.zixuan.xmusic.music;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaButtonReceiver;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zixuan.xmusic.R;
import com.zixuan.xmusic.api.BMA;
import com.zixuan.xmusic.bean.SongBean;
import com.zixuan.xmusic.constant.Constant;
import com.zixuan.xmusic.global.XMusicApplication;
import com.zixuan.xmusic.ui.activity.SongPlayingActivity;
import com.zixuan.xmusic.utils.LogHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Response;


public class MediaPlayerHelper implements MediaPlayer.OnPreparedListener, MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener {
    public static final String TAG = "MediaPlayerHelper";

    //播放模式-随机播放
    public static final int PLAYMODE_RANDOM = 0;
    //播放模式-列表循环
    public static final int PLAYMODE_CIRCLE = 1;
    //播放模式-单曲循环
    public static final int PLAYMODE_SINGLE = 2;

    //action 切换播放模式
    public static final String ACTION_SWITCH_PLAYMODE = "com.zixuan.xmusic.playmode";
    //action 清空播放列表
    public static final String ACTION_CLEAR_PLAYLIST = "com.zixuan.xmusic.clear";
    //action 删除列表中的歌曲
    public static final String ACTION_DELETE_ITEM = "com.zixuan.xmusic.delete";

    private MediaPlayer mMediaPlayer;
    private PlaybackStateCompat mPlaybackState;
    private MediaSessionCompat mMediaSession;
    private MediaControllerCompat mMediaController;
    private Context mContext;

    //播放列表
    private List<MediaSessionCompat.QueueItem> mPlayingQueue;
    //当前播放曲目
//    private MediaSessionCompat.QueueItem mCurrentItem;
    private SongBean mCurrentSong;
    //当前播放曲目索引位置
    private int last_index;
    //当前播放模式
    private int mCurrentPlaymode = PLAYMODE_CIRCLE;

    public MediaPlayerHelper(Context mContext) {
        this.mContext = mContext;
        initService(mContext);
    }

    //根据QueueItem构造MediaMetadata
    private MediaMetadataCompat buildMetadata(SongBean src) {

        return new MediaMetadataCompat.Builder()
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, src.getSong_id())  //歌曲ID
                .putString(MediaMetadataCompat.METADATA_KEY_TITLE, src.getTitle())       //歌曲标题
                .putString(MediaMetadataCompat.METADATA_KEY_AUTHOR, src.getAuthor())     //歌曲作者
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM, src.getAlbum_title())   //专辑标题
                .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_URI, src.getFile_link())  //歌曲播放地址
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, src.getFile_duration())  //歌曲时长
                .putString(MediaMetadataCompat.METADATA_KEY_ART_URI, src.getPic_premium())   //歌曲插图-高清
                .putString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI, src.getLrclink())  //歌词链接
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, src.getPic_small())  //歌曲插图-普通
                .putLong(MediaMetadataCompat.METADATA_KEY_TRACK_NUMBER, last_index)       //歌曲在播放列表的位置
                .build();
    }


    private MediaSessionCompat.Callback mMediaSessionCallback = new MediaSessionCompat.Callback() {

        @Override
        public boolean onMediaButtonEvent(Intent mediaButtonEvent) {
            MediaButtonReceiver.handleIntent(mMediaSession,mediaButtonEvent);
            return super.onMediaButtonEvent(mediaButtonEvent);
        }

        @Override
        public void onCustomAction(String action, Bundle extras) {
            super.onCustomAction(action, extras);
            switch (action) {
                case ACTION_SWITCH_PLAYMODE:
                    mCurrentPlaymode = (mCurrentPlaymode + 1) % 3;
                    break;
                case ACTION_CLEAR_PLAYLIST:
                    mPlayingQueue.clear();
                    break;
                case ACTION_DELETE_ITEM:

                    break;
            }

        }

        @Override
        public void onPrepare() {
            if ( mPlayingQueue == null){
                return;
            }
            if (mPlayingQueue.size()<1 ){
                return;
            }
            getSongDetailFromWeb(mPlayingQueue.get(last_index));

        }


        @Override
        public void onPause() {
            switch (mPlaybackState.getState()) {
                case PlaybackStateCompat.STATE_PLAYING:
                    mMediaPlayer.pause();
                    mPlaybackState = new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PAUSED, mMediaPlayer.getCurrentPosition(), 1.0f,SystemClock.elapsedRealtime())
                            .build();
                    mMediaSession.setPlaybackState(mPlaybackState);
                    updateNotification();
                    break;

            }
        }

        @Override
        public void onPlay() {
            switch (mPlaybackState.getState()) {
                case PlaybackStateCompat.STATE_PAUSED:
                    mMediaPlayer.start();
                    mPlaybackState = new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_PLAYING, mMediaPlayer.getCurrentPosition(), 1.0f,SystemClock.elapsedRealtime())
                            .build();
                    mMediaSession.setPlaybackState(mPlaybackState);
                    updateNotification();
                    break;
            }
        }

        //下一曲
        @Override
        public void onSkipToNext() {
            super.onSkipToNext();
            if (mPlayingQueue == null) {
                return;
            }
            if (mCurrentPlaymode == PLAYMODE_RANDOM){
                last_index = new Random(SystemClock.elapsedRealtime()).nextInt(mPlayingQueue.size());
            }else if (mCurrentPlaymode == PLAYMODE_CIRCLE){
                if (last_index < mPlayingQueue.size() - 1)
                    last_index++;
                else
                    last_index = 0;
            }

            onPrepare();

        }

        //上一曲
        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();

            if (mPlayingQueue == null) {
                return;
            }

            if (mCurrentPlaymode == PLAYMODE_RANDOM){
                last_index = new Random(SystemClock.elapsedRealtime()).nextInt(mPlayingQueue.size());
            }else if (mCurrentPlaymode == PLAYMODE_CIRCLE){
                if (last_index > 0)
                    last_index--;
                else
                    last_index = mPlayingQueue.size() - 1;
            }
            onPrepare();
        }
    };


    /**
     * 初始化各服务
     *
     * @param mContext
     */
    private void initService(Context mContext) {

        //传递播放的状态信息
        mPlaybackState = new PlaybackStateCompat.Builder().
                setState(PlaybackStateCompat.STATE_NONE, 0, 1.0f)
                .build();
        ComponentName componentName = new ComponentName(mContext.getPackageName(), MediaButtonReceiver.class.getName());
        //初始化MediaSessionCompant
        mMediaSession = new MediaSessionCompat(mContext, Constant.MEDIA_SESSION_TAG, componentName, null);
        mMediaSession.setCallback(mMediaSessionCallback);//设置播放控制回调
        //设置可接受媒体控制
        mMediaSession.setActive(true);
        mMediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSession.setPlaybackState(mPlaybackState);//状态回调

        //初始化MediaPlayer
        mMediaPlayer = new MediaPlayer();
        // 设置音频流类型
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mMediaPlayer.setOnPreparedListener(this);
        mMediaPlayer.setOnBufferingUpdateListener(this);
        mMediaPlayer.setOnCompletionListener(this);

        // 初始化MediaController
        mMediaController = new MediaControllerCompat(mContext,mMediaSession);

        mPlayingQueue = new ArrayList<>();
    }

    private NotificationCompat.Action createAction(int iconResId, String title, String action) {
        Intent intent = new Intent(mContext, AudioPlayerService.class);
        intent.setAction(action);
        PendingIntent pendingIntent = PendingIntent.getService(mContext, 1, intent, 0);
        return new NotificationCompat.Action(iconResId, title, pendingIntent);
    }

    /**
     * 更新通知栏
     */
    private void updateNotification() {
        NotificationCompat.Action playPauseAction = mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING ?
                createAction(R.drawable.ic_notification_pause, mContext.getString(R.string.notification_action_pause), Constant.ACTION_PAUSE) :
                createAction(R.drawable.ic_notification_play, mContext.getString(R.string.notification_action_play), Constant.ACTION_PLAY);

        Intent intent = new Intent(mContext, SongPlayingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(mContext)
                .setContentTitle(mCurrentSong.getTitle())
                .setContentText(mCurrentSong.getAuthor())
                .setOngoing(mPlaybackState.getState() == PlaybackStateCompat.STATE_PLAYING)
                .setShowWhen(false)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification_music)
                .setAutoCancel(false)
                .addAction(createAction(R.drawable.ic_notification_prev, mContext.getString(R.string.notification_action_prev), Constant.ACTION_PREV))
                .addAction(playPauseAction)
                .addAction(createAction(R.drawable.ic_notification_next, mContext.getString(R.string.notification_action_next), Constant.ACTION_NEXT))
                .setStyle(new android.support.v7.app.NotificationCompat.MediaStyle()
                        .setMediaSession(mMediaSession.getSessionToken())
                        .setShowActionsInCompactView(1, 2));

        Notification notification = notificationCompat.build();
        ((NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE)).notify(1, notification);

    }


    //播放前的准备动作回调
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mMediaPlayer.start();
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_PLAYING, 0, 1.0f,SystemClock.elapsedRealtime())
                .build();
        mMediaSession.setPlaybackState(mPlaybackState);
        updateNotification();

    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mPlaybackState = new PlaybackStateCompat.Builder()
                .setState(PlaybackStateCompat.STATE_NONE,0,1.0F,SystemClock.elapsedRealtime())
                .build();
        mMediaSession.setPlaybackState(mPlaybackState);
        updateNotification();

        mMediaController.getTransportControls().skipToNext();
    }


    public void destoryService() {
        if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        if (mMediaSession != null) {
            mMediaSession.release();
            mMediaSession = null;
        }
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }


    public MediaControllerCompat getMediaController() {
        return mMediaController;
    }

    public MediaSessionCompat.Token getMediaSessionToken() {
        return mMediaSession.getSessionToken();
    }


    public void setPlaylist(List<MediaSessionCompat.QueueItem> list_data) {
        mPlayingQueue.clear();
        this.mPlayingQueue.addAll(list_data);
        last_index = 0;
        mMediaSession.setQueue(mPlayingQueue);
    }

    public void addPlayData(List<MediaSessionCompat.QueueItem> list_data) {
        mPlayingQueue.addAll(list_data);
    }

    public PlaybackStateCompat getPlaybackState(){
        return mPlaybackState;
    }

    public void playQueueItem(int index){
        last_index = index;
        mMediaController.getTransportControls().prepare();
    }
    public int getCurrentPlaymode(){
        return mCurrentPlaymode;
    }

    public void getSongDetailFromWeb(MediaSessionCompat.QueueItem item) {
        OkHttpUtils.get().url(BMA.Song.songInfoFromWeb(item.getDescription().getMediaId())).build().execute(new Callback<SongBean>() {

            @Override
            public SongBean parseNetworkResponse(Response response, int id) throws Exception {
                JSONObject source = new JSONObject(response.body().string());
                SongBean bean = XMusicApplication.getGson().fromJson(source.getJSONObject("songinfo").toString(), SongBean.class);
                JSONObject bitrate = source.getJSONObject("bitrate");
                bean.setFile_size(bitrate.getLong("file_size"));
                bean.setFile_duration(bitrate.getInt("file_duration"));
                bean.setFile_link(bitrate.getString("file_link"));

                return bean;
            }

            @Override
            public void onError(Call call, Exception e, int id) {
                LogHelper.e(TAG, "onError : " + e.getMessage());
            }

            @Override
            public void onResponse(SongBean response, int id) {
                mCurrentSong = response;
                playSong(mCurrentSong);
            }
        });
    }

    public void playSong(SongBean song) {
        try {

            switch (mPlaybackState.getState()) {
                case PlaybackStateCompat.STATE_PLAYING:
                case PlaybackStateCompat.STATE_PAUSED:
                case PlaybackStateCompat.STATE_NONE:
                    mMediaPlayer.reset();
                    //设置播放地址
                    mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    mMediaPlayer.setDataSource(song.getFile_link());
                    //异步进行播放
                    mMediaPlayer.prepareAsync();
                    //设置当前状态为连接中
                    mPlaybackState = new PlaybackStateCompat.Builder()
                            .setState(PlaybackStateCompat.STATE_CONNECTING, 0, 1.0f, SystemClock.elapsedRealtime())
                            .build();
                    //告诉MediaSession当前最新的音频状态。
                    mMediaSession.setPlaybackState(mPlaybackState);
                    //设置音频信息；
                    MediaMetadataCompat meta = buildMetadata(song);
                    mMediaSession.setMetadata(meta);
                    break;
            }
        } catch (IOException e) {
            LogHelper.e(TAG, "playSong : ");
        }
    }


    @Override
    public void onBufferingUpdate(MediaPlayer mp, int percent) {

    }
}