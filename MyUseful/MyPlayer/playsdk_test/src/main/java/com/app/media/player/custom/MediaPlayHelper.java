package com.app.media.player.custom;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.app.media.manager.UiCallBackManager;
import com.app.media.player.IPlayHelper;
import com.app.media.player.OnPlayerCallback;

import java.util.Map;

import cn.yinxm.playsdk.test.LogUtil;


/**
 * Created by yinxm on 2017/1/12.
 * 功能:
 */

public class MediaPlayHelper implements IPlayHelper {

    public static final int BUFFERED_FULL_NOTIFY_TIMES = 3;


    private MediaPlayer myMedia;
    private OnPlayerCallback playFinishCallback;
    boolean isMediaPlayerReady = false;
    public int bufferPercent = 0;
    public int bufferedFullTims = 0;
    boolean flag = false; // 是否存在上一个MediaPlayer对象
    String playUrl;

    public MediaPlayHelper() {
        myMedia = new MediaPlayer();
        myMedia.setAudioStreamType(AudioManager.STREAM_MUSIC);
    }

    @Override
    public void play(String url) {
        play(url, null, null);
    }

    @Override
    public void play(String url, OnPlayerCallback playFinishCallback) {
        play(url, null, playFinishCallback);
    }

    @Override
    public void play(String url, Map<String, Object> extendParams, OnPlayerCallback callback) {
        flag = false;
        isMediaPlayerReady = false;
        LogUtil.d(TAG, "PlayerHelper paly url=" + url);
        if (TextUtils.isEmpty(url)) {
            LogUtil.e(TAG, "playUrl 不能为空");
            if (callback != null) {
                callback.onPlayerState(OnPlayerCallback.State.STATE_IDLE);
            }
            return;
        }

        try {
            if (myMedia != null) {
                try {
                    flag = true;
                    myMedia.stop();
                    myMedia.release();
                } catch (Exception e) {
                    LogUtil.e(TAG, "myMedia=" + myMedia + " stop release异常" + e);
                } finally {
                    myMedia = null;
                }
            }
            this.playUrl = url;
            this.playFinishCallback = callback;
            flag = false;
            myMedia = new MediaPlayer();
            myMedia.setDataSource(playUrl);
//                                myMedia.setDataSource("http://trslbs.kaolafm.cn/016f63815d64d4db/1600000000512/playlist.m3u8");
            bufferPercent = 0;
            bufferedFullTims = 0;
            myMedia.prepareAsync();
            if (playFinishCallback != null) {
                playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_BUFFERING);
            }
            myMedia.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    LogUtil.d(TAG, "MediaPlayer onPrepared");

                    UiCallBackManager.getInstance().getUiCallBackDialog().hide();

                    isMediaPlayerReady = true;

                    if (playFinishCallback != null) {
                        playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_READY);
                    }
                }
            });

            myMedia.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    // 歌曲播放完毕，根据播放模式选择下一首播放歌曲的position
                    LogUtil.e(TAG, "MediaPlayer当前播放完毕，自动下一首 flag=" + flag);
                    if (playFinishCallback != null) {
                        playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_ENDED);
                    }
                }

            });

            myMedia.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() { // 缓冲流改变回调
                @Override
                public void onBufferingUpdate(MediaPlayer mp, int percent) {
                    bufferPercent = percent;
                    if (bufferPercent >= 100) {
                        bufferPercent = 100;
                        bufferedFullTims++;
                    }
                    if (bufferedFullTims <= BUFFERED_FULL_NOTIFY_TIMES) {
                        LogUtil.d(TAG, "bufferedFullTims <= BUFFERED_FULL_NOTIFY_TIMES");
//                                                notifyBufferingUpdateListener(percent);
                    }
                }
            });
            myMedia.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
//                                        ToastUtil.showTextToast("当前歌曲无法播放");
//                                             http://image.kaolafm.net/mz/filmchipsmp3_32/201507/9347a567-9df4-49f3-8966-0b975c272ed3.mp3?appid=kfxmv5890&deviceid=100016501&audioid=1000001732372
                    LogUtil.e(TAG, "MediaPlayer播放异常mp=" + mp + ", what=" + what + ", extra=" + extra + ", playUrl="
                            + playUrl);
                    if (playFinishCallback != null) {
                        playFinishCallback.onPlayError(MediaPlayHelper.this, playUrl,
                                "mediaPlayer onError what" + what + ", "
                                        + "extra=" + extra + " , playUrl=" + playUrl);
                    }
                    return true;
                }
            });
        } catch (Exception e) {
            isMediaPlayerReady = false;
            LogUtil.e(TAG, "播放器错误" + e);
            if (callback != null) {
                callback.onPlayerState(OnPlayerCallback.State.STATE_IDLE);
            }
        }
    }

    @Override
    public void play() {
        if (isMediaPlayerReady && myMedia != null) {
            myMedia.start();
        }
    }

    @Override
    public void pause() {
        if (isMediaPlayerReady && myMedia != null) {
            myMedia.pause();
        }
    }

    @Override
    public void playToggle() {
        if (isPlaying()) {
            pause();
        } else {
            play();
        }
    }

    @Override
    public void stop() {
        try {
            if (myMedia != null) {
                myMedia.stop();
                myMedia.release();
            }
        } catch (Exception e) {
            LogUtil.e(TAG, "播放器结束异常" + e);
        } finally {
            myMedia = null;
        }
    }

    @Override
    public void release() {
        stop();
    }

    @Override
    public long getDuration() {
        if (isMediaPlayerReady && myMedia != null) {
            return myMedia.getDuration();
        } else {
            return 0;
        }
    }

    @Override
    public long getCurrentPosition() {
        if (isMediaPlayerReady && myMedia != null) {
            return myMedia.getCurrentPosition();
        } else {
            return 0;
        }
    }

    @Override
    public int getBufferedPercentage() {
        return bufferPercent;
    }

    @Override
    public long getBufferedPosition() {
        return bufferPercent * getDuration() / 100;
    }

    @Override
    public boolean isPlaying() {
        if (isMediaPlayerReady && myMedia != null) {
            return myMedia.isPlaying();
        } else {
            return false;
        }
    }

    @Override
    public void rewind() {
        seekTo(DEFAULT_REWIND_MS);
    }

    @Override
    public void fastForward() {
        seekTo(DEFAULT_FAST_FORWARD_MS);
    }

    @Override
    public void seekTo(long positionMs) {
        if (isMediaPlayerReady && myMedia != null) {
            myMedia.seekTo((int) positionMs);
            LogUtil.d(TAG, "音频 seekTo " + positionMs);
            myMedia.start();
        }
    }

    @Override
    public void setVolume(float volume) {
        myMedia.setVolume(volume, volume);
    }

    @Override
    public MediaPlayer getPlayerInstance() {
        return myMedia;
    }

    @Override
    public String getPlayUrl() {
        return playUrl;
    }
}
