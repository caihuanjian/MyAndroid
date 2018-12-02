package com.app.media.player.exo;

import android.content.Context;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.app.media.player.IPlayHelper;
import com.app.media.player.OnPlayerCallback;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.AdaptiveMediaSourceEventListener;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.source.smoothstreaming.DefaultSsChunkSource;
import com.google.android.exoplayer2.source.smoothstreaming.SsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.HttpDataSource;
import com.google.android.exoplayer2.util.Util;

import java.util.Map;

import cn.yinxm.playsdk.test.LogUtil;


/**
 * Created by yinxm on 2017/1/12.
 * 功能: exo 播放器
 */

public class ExoAudioPlayHelper implements ExoPlayer.EventListener, IPlayHelper {
    public static final int BUFFERED_FULL_NOTIFY_TIMES = 3;


    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private int playTimes = 0; // 检测ExoPlayer是否可用


    private SimpleExoPlayer player;
    Handler mainHandler;
    private DataSource.Factory mediaDataSourceFactory;
    private boolean isUseCache = true;

    private String playUrl;
    private OnPlayerCallback playFinishCallback;
    private boolean playing = false;
    private boolean isPreparing = false;
    boolean isPlayerReady = false;
    private ExoPlayer.EventListener eventListener;

    public SimpleExoPlayer getPlayer() {
        return player;
    }

    public ExoAudioPlayHelper(Context context) {

        playTimes = 0;
        // 全局初始化一次
        ExoPlayerUtil.getInstance().init(context);

        if (isUseCache) {
            mediaDataSourceFactory = ExoPlayerUtil.getInstance()
                    .builCachedDataSourceFactory(context.getApplicationContext());
        } else {
            mediaDataSourceFactory = ExoPlayerUtil.getInstance()
                    .buildDataSourceFactory(true, BANDWIDTH_METER);
        }

        // 1. Create a default TrackSelector
        mainHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                LogUtil.d(TAG, "handler msg=" + msg);
            }
        };
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

// 2. Create a default LoadControl
//        LoadControl loadControl = new DefaultLoadControl();
        LoadControl loadControl = new FastPlayLoadControl();

// 3. Create the player
        player = ExoPlayerFactory.newSimpleInstance(context, trackSelector, loadControl);
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);

        player.addListener(this);
        player.setPlayWhenReady(false);

//        eventLogger = new EventLogger(trackSelector);
//        player.addListener(eventLogger);
//        player.setAudioDebugListener(eventLogger);
//        player.setVideoDebugListener(eventLogger);
//        player.setMetadataOutput(eventLogger);
    }

    public void setEventListener(ExoPlayer.EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void removeEventListener(ExoPlayer.EventListener eventListener) {
        if (eventListener != null && player != null) {
            player.removeListener(eventListener);
        }
    }

    /**
     * @param url 播放地址
     */
    @Override
    public void play(String url) {
        play(url, null, null);
    }

    @Override
    public void play(String url, OnPlayerCallback playFinishCallback) {
        play(url, null, playFinishCallback);
    }

    /**
     * @param url                播放地址
     * @param extendParams       播放额外需要的参数，可为null
     * @param playFinishCallback 播放完毕后回调，可为null
     */
    @Override
    public void play(String url, Map<String, Object> extendParams,
                     OnPlayerCallback playFinishCallback) {
        LogUtil.d(TAG, "ExoAudioPlayHelper player" + player + ", url=" + url);
        if (url == null || "".equals(url.trim()) || player == null) {
            LogUtil.e("playUrl 不能为空 ");
            if (playFinishCallback != null) {
                playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_IDLE);
            }
            return;
        }
        this.playFinishCallback = playFinishCallback;
        this.playUrl = url;
        try {
            isPreparing = false;
            isPlayerReady = false;
            Uri uri = Uri.parse(url);
            MediaSource mediaSource = buildMediaSource(uri, null,
                    mainHandler, mediaDataSourceFactory, null,
                    null);
            playing = false;
            player.prepare(mediaSource);
            isPreparing = true;
            playTimes++;
        } catch (Exception e) {
            LogUtil.e("播放器错误" + e);
            if (playFinishCallback != null) {
                playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_IDLE);
            }
        }
    }

    @Override
    public void play() {
        if (player != null && !player.getPlayWhenReady()) {
            player.setPlayWhenReady(true);
        }
    }

    @Override
    public void pause() {
        if (player != null) {
            player.setPlayWhenReady(false);
        }
    }

    /**
     * 播放-暂停-播放 切换
     */
    @Override
    public void playToggle() {
        if (player != null) {
            player.setPlayWhenReady(!player.getPlayWhenReady());
        }
    }

    /**
     * 停止播放
     */
    @Override
    public void stop() {
        if (player != null) {
            player.stop();
        }
    }

    /**
     * 停止播放，停止了后player不能再被使用
     */
    @Override
    public void release() {
        if (player != null) {
            player.release();
        }
    }

    /**
     * 获取音频时长
     *
     * @return
     */
    @Override
    public long getDuration() {
        long duration = 0L;
        if (player != null && isPlayerReady) {
            duration = player.getDuration();
            if (duration < 0) {
                duration = 0L;
            }
        }
        return duration;
    }

    /**
     * 获取当前播放音频长度
     *
     * @return
     */
    @Override
    public long getCurrentPosition() {
        long position = 0L;
        if (player != null && isPlayerReady) {
            position = player.getCurrentPosition();
        }
        if (position < 0) {
            position = 0L;
        }
        return position;
    }

    /**
     * 获取缓冲百分比
     *
     * @return
     */
    @Override
    public int getBufferedPercentage() {
        int bufferePercent = 0;
        if (player != null) {
            bufferePercent = player.getBufferedPercentage();
        }
        if (bufferePercent < 0) {
            bufferePercent = 0;
        }
        return bufferePercent;
    }

    /**
     * 获取缓冲时长
     *
     * @return
     */
    @Override
    public long getBufferedPosition() {
        long bufferedPos = 0L;
        if (player != null) {
            bufferedPos = player.getBufferedPosition();
        }
        if (bufferedPos < 0) {
            bufferedPos = 0L;
        }
        return bufferedPos;
    }

    @Override
    public boolean isPlaying() {
        if (player != null) {
            return playing;
        } else {
            return false;
        }
    }

    // 快退
    @Override
    public void rewind() {
        int rewindMs = DEFAULT_REWIND_MS;
        if (rewindMs <= 0 || player == null) {
            return;
        }
        seekTo(Math.max(player.getCurrentPosition() - rewindMs, 0));
    }

    // 快进
    @Override
    public void fastForward() {
        int fastForwardMs = DEFAULT_FAST_FORWARD_MS;
        if (fastForwardMs <= 0 || player == null) {
            return;
        }
        seekTo(Math.min(player.getCurrentPosition() + fastForwardMs, player.getDuration()));
    }

    // 播放进度移动到指定位置
    @Override
    public void seekTo(long positionMs) {
        if (player != null) {
            if (positionMs == player.getDuration()) {
                playing = false;
            }
            player.seekTo(positionMs);
        }
    }

    @Override
    public String getPlayUrl() {
        return playUrl;
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        LogUtil.d(TAG, "onTimelineChanged timeline=" + timeline + ", manifest=" + manifest);

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        LogUtil.d(TAG, "onTracksChanged trackGroups=" + trackGroups + ", trackSelections=" + trackSelections);
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        LogUtil.d(TAG, "onLoadingChanged isLoading=" + isLoading);

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        playing = playWhenReady == true && playbackState == ExoPlayer.STATE_READY;
        LogUtil.d(TAG, "onPlayerStateChanged playWhenReady=" + playWhenReady
                + ", playbackState=" + playbackState + ", playing=" + playing);

        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                if (playFinishCallback != null) {
                    playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_BUFFERING);
                }
                break;
            case ExoPlayer.STATE_READY:
                // 未播放，ready
                if (isPreparing) {
                    isPreparing = false;
                    isPlayerReady = true;
                    if (playFinishCallback != null) {
                        playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_READY);
                    }
                }
                break;
            case ExoPlayer.STATE_ENDED:
                if (playFinishCallback != null) {
                    playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_ENDED);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onPlayerError(ExoPlaybackException error) {
        LogUtil.e("playTimes=" + playTimes + ", onPlayerError=" + error);
        try {
            if (playFinishCallback != null) {
                playFinishCallback.onPlayError(this, playUrl, "" + error + ", playUrl=" + playUrl);
            }
        } catch (Exception e) {
            LogUtil.e(e.getMessage());
        }

    }

    @Override
    public void onPositionDiscontinuity() {
        LogUtil.d(TAG, "onPositionDiscontinuity");
    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
        LogUtil.d(TAG, "onPlaybackParametersChanged " + playbackParameters);
    }

    /**
     * 通过url构造MediaSource
     *
     * @param uri
     * @param overrideExtension
     * @param mainHandler
     * @param adaptiveMediaSourceEventListener 直播流时使用
     * @param eventListener                    普通流
     * @return
     */
    public MediaSource buildMediaSource(Uri uri, String overrideExtension, Handler mainHandler,
                                        DataSource.Factory mediaDataSourceFactory,
                                        AdaptiveMediaSourceEventListener adaptiveMediaSourceEventListener,
                                        ExtractorMediaSource.EventListener eventListener) {
        int type = Util.inferContentType(!TextUtils.isEmpty(overrideExtension) ? "."
                + overrideExtension : uri.getLastPathSegment());
        switch (type) {
            case C.TYPE_SS:
                return new SsMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultSsChunkSource.Factory(mediaDataSourceFactory), mainHandler,
                        adaptiveMediaSourceEventListener);
            case C.TYPE_DASH:
                return new DashMediaSource(uri, buildDataSourceFactory(false),
                        new DefaultDashChunkSource.Factory(mediaDataSourceFactory), mainHandler,
                        adaptiveMediaSourceEventListener);
            case C.TYPE_HLS:
                return new HlsMediaSource(uri, mediaDataSourceFactory, mainHandler,
                        adaptiveMediaSourceEventListener);
            case C.TYPE_OTHER:
                return new ExtractorMediaSource(uri, mediaDataSourceFactory,
                        new DefaultExtractorsFactory(),
                        mainHandler, eventListener);
            default: {
                throw new IllegalStateException("Unsupported type: " + type);
            }
        }
    }


    /**
     * Returns a new DataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new DataSource factory.
     */
    private DataSource.Factory buildDataSourceFactory(boolean useBandwidthMeter) {
        return ExoPlayerUtil.getInstance().buildDataSourceFactory(useBandwidthMeter
                ? BANDWIDTH_METER : null);
    }

    /**
     * Returns a new HttpDataSource factory.
     *
     * @param useBandwidthMeter Whether to set {@link #BANDWIDTH_METER} as a listener to the new
     *                          DataSource factory.
     * @return A new HttpDataSource factory.
     */
    private HttpDataSource.Factory buildHttpDataSourceFactory(boolean useBandwidthMeter) {
        return ExoPlayerUtil.getInstance().buildHttpDataSourceFactory(useBandwidthMeter
                ? BANDWIDTH_METER : null);
    }

    @Override
    public void setVolume(float volume) {
        player.setVolume(volume);
    }

    @Override
    public SimpleExoPlayer getPlayerInstance() {
        return player;
    }

    public boolean isUseCache() {
        return isUseCache;
    }

    public void setUseCache(boolean useCache) {
        isUseCache = useCache;
    }
}
