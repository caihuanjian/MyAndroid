package com.app.media.player.ijk;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.app.media.player.IPlayHelper;
import com.app.media.player.OnPlayerCallback;

import java.util.Map;


import cn.yinxm.playsdk.test.LogUtil;
import cn.yinxm.playsdk.test.StringUtil;
import tv.danmaku.ijk.media.player.AndroidMediaPlayer;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 功能：
 * Created by yinxm on 2017/6/28.
 */

public class IJKAudioPlayer implements IPlayHelper {
    private static final String TAG = IJKAudioPlayer.class.getSimpleName();
    Context context;
    private IMediaPlayer mMediaPlayer = null;
    private String playUrl;
    private OnPlayerCallback playFinishCallback;

    //IJK配置
    private boolean usingAndroidPlayer = false;
    private boolean usingMediaCodec = false;
    private boolean usingMediaCodecAutoRotate = false;
    private boolean usingOpenSLES = false;
    private String pixelFormat = "";//Auto Select=,RGB 565=fcc-rv16,RGB 888X=fcc-rv32,YV12=fcc-yv12,默认为RGB 888X
    private boolean enableBackgroundPlay = false;

    // all possible internal states
    public static final int STATE_ERROR = -1;
    public static final int STATE_IDLE = 0;
    public static final int STATE_PREPARING = 1;
    public static final int STATE_PREPARED = 2;
    public static final int STATE_PLAYING = 3;
    public static final int STATE_PAUSED = 4;
    public static final int STATE_PLAYBACK_COMPLETED = 5;

    // mCurrentState is a VideoView object's current state.
    // mTargetState is the state that a method caller intends to reach.
    // For instance, regardless the VideoView object's current state,
    // calling pause() intends to bring the object to a target state
    // of STATE_PAUSED.
    private int mCurrentState = STATE_IDLE;
    private int mTargetState = STATE_IDLE;

    private IMediaPlayer.OnCompletionListener mOnCompletionListener;
    private IMediaPlayer.OnPreparedListener mOnPreparedListener;
    private int mCurrentBufferPercentage;
    private IMediaPlayer.OnErrorListener mOnErrorListener;
    private IMediaPlayer.OnInfoListener mOnInfoListener;
    private long mSeekWhenPrepared;  // recording the seek position while preparing
    private boolean mCanPause = true;


    public IJKAudioPlayer(Context context) {
        this.context = context;
    }


    @Override
    public void play(String url) {
        play(url, null);
    }

    @Override
    public void play(String url, OnPlayerCallback playFinishCallback) {
        play(url, null, playFinishCallback);
    }

    @Override
    public void play(String url, Map<String, Object> extendParams, OnPlayerCallback playFinishCallback) {

        if (StringUtil.isBlank(url)) {
            LogUtil.e("playUrl 不能为空");
            if (playFinishCallback != null) {
                playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_IDLE);
            }
            return;
        }
        this.playUrl = url;
        this.playFinishCallback = playFinishCallback;
        Uri mUri = Uri.parse(playUrl);

        // we shouldn't clear the target state, because somebody might have
        // called start() previously
        release(false);

        try {
            LogUtil.d("usingAndroidPlayer=" + usingAndroidPlayer + ", usingMediaCodec=" + usingMediaCodec
                    + ", usingMediaCodecAutoRotate=" + usingMediaCodecAutoRotate + ", usingOpenSLES=" + usingOpenSLES);
            if (usingAndroidPlayer) {
                mMediaPlayer = new AndroidMediaPlayer();
            } else {
                IjkMediaPlayer ijkMediaPlayer = null;
                if (playUrl != null) {
                    ijkMediaPlayer = new IjkMediaPlayer();
                    ijkMediaPlayer.native_setLogLevel(IjkMediaPlayer.IJK_LOG_DEBUG);

                    if (usingMediaCodec) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1);
                        if (usingMediaCodecAutoRotate) {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1);
                        } else {
                            ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 0);
                        }
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 0);
                    }

                    if (usingOpenSLES) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 1);
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "opensles", 0);
                    }

                    if (TextUtils.isEmpty(pixelFormat)) {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", IjkMediaPlayer.SDL_FCC_RV32);
                    } else {
                        ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "overlay-format", pixelFormat);
                    }
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "framedrop", 1);
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "start-on-prepared", 0);

                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "http-detect-range-support", 0);
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "timeout", 10000000);
                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_FORMAT, "reconnect", 1);

                    ijkMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_CODEC, "skip_loop_filter", 48);
                }
                mMediaPlayer = ijkMediaPlayer;
            }
            LogUtil.d("mMediaPlayer=" + mMediaPlayer);

            // TODO: 2017/6/28  
//            if (enableBackgroundPlay) {
//                mMediaPlayer = new TextureMediaPlayer(mMediaPlayer);
//            }


            // REMOVED: mAudioSession
            mMediaPlayer.setOnPreparedListener(mPreparedListener);
//            mMediaPlayer.setOnVideoSizeChangedListener(mSizeChangedListener);
            mMediaPlayer.setOnCompletionListener(mCompletionListener);
            mMediaPlayer.setOnErrorListener(mErrorListener);
            mMediaPlayer.setOnInfoListener(mInfoListener);
            mMediaPlayer.setOnBufferingUpdateListener(mBufferingUpdateListener);
            mCurrentBufferPercentage = 0;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
//                mMediaPlayer.setDataSource(context, mUri, mHeaders);
                mMediaPlayer.setDataSource(context, mUri, null);
            } else {
                mMediaPlayer.setDataSource(mUri.toString());
            }
//            bindSurfaceHolder(mMediaPlayer, mSurfaceHolder);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setScreenOnWhilePlaying(true);
            mMediaPlayer.prepareAsync();

            // REMOVED: mPendingSubtitleTracks

            // we don't set the target state here either, but preserve the
            // target state that was there before.
            mCurrentState = STATE_PREPARING;
            mTargetState = STATE_PLAYING;
//            attachMediaController();
        } catch (Exception ex) {
            LogUtil.e(TAG, "Unable to open content: " + mUri + ex);
            mCurrentState = STATE_ERROR;
            mTargetState = STATE_ERROR;
            mErrorListener.onError(mMediaPlayer, MediaPlayer.MEDIA_ERROR_UNKNOWN, 0);
            if (playFinishCallback != null) {
                playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_IDLE);
            }
        } finally {
            // REMOVED: mPendingSubtitleTracks.clear();
        }
    }


    @Override
    public void play() {
        try {
            LogUtil.d("play");
            if (isInPlaybackState()) {
                mMediaPlayer.start();
                mCurrentState = STATE_PLAYING;
            }
            mTargetState = STATE_PLAYING;
        } catch (Exception e) {
            LogUtil.e("play " + e);
        }
    }

    @Override
    public void pause() {
        try {
            LogUtil.d("pause");
            if (isInPlaybackState()) {
                if (mMediaPlayer.isPlaying()) {
                    mMediaPlayer.pause();
                    mCurrentState = STATE_PAUSED;
                }
            }
            mTargetState = STATE_PAUSED;
        } catch (Exception e) {
            LogUtil.e("pause " + e);
        }
    }

    @Override
    public void playToggle() {
        try {
            if (isPlaying()) {
                pause();
            } else {
                play();
            }
        } catch (Exception e) {
            LogUtil.e("playToggle " + e);
        }
    }

    @Override
    public void stop() {
        try {
            if (mMediaPlayer != null) {
                LogUtil.d("stop");
                mMediaPlayer.stop();
                mMediaPlayer.release();
                mMediaPlayer = null;
                mCurrentState = STATE_IDLE;
                mTargetState = STATE_IDLE;
            }
        } catch (Exception e) {
            LogUtil.e("stop " + e);
        }
    }

    @Override
    public void release() {
        try {
            release(false);
        } catch (Exception e) {
            LogUtil.e("release " + e);
        }
    }

    @Override
    public long getDuration() {
        try {
            if (isInPlaybackState()) {
                return mMediaPlayer.getDuration();
            }
        } catch (Exception e) {
            LogUtil.e("getDuration " + e);
        }
        return 0;
    }

    @Override
    public long getCurrentPosition() {
        try {
            if (isInPlaybackState()) {
                return mMediaPlayer.getCurrentPosition();
            }
        } catch (Exception e) {
            LogUtil.e("getCurrentPosition " + e);
        }
        return 0;
    }

    @Override
    public int getBufferedPercentage() {
        try {
            if (mMediaPlayer != null) {
                return mCurrentBufferPercentage;
            }
        } catch (Exception e) {
            LogUtil.e("getBufferedPercentage " + e);
        }
        return 0;
    }

    @Override
    public long getBufferedPosition() {
        try {
            return getBufferedPercentage() * getDuration() / 100;
        } catch (Exception e) {
            LogUtil.e("getBufferedPosition " + e);
        }
        return 0;
    }

    @Override
    public boolean isPlaying() {
        try {
            return isInPlaybackState() && mMediaPlayer.isPlaying();
        } catch (Exception e) {
            LogUtil.e("isPlaying " + e);
        }
        return false;
    }

    @Override
    public void rewind() {
        try {
            int rewindMs = DEFAULT_REWIND_MS;
            if (rewindMs <= 0 || mMediaPlayer == null) {
                return;
            }
            seekTo(Math.max(getCurrentPosition() - rewindMs, 0));
        } catch (Exception e) {
            LogUtil.e("rewind " + e);
        }
    }

    @Override
    public void fastForward() {
        try {
            int fastForwardMs = DEFAULT_FAST_FORWARD_MS;
            if (fastForwardMs <= 0 || mMediaPlayer == null) {
                return;
            }
            seekTo(Math.min(getCurrentPosition() + fastForwardMs, getDuration()));
        } catch (Exception e) {
            LogUtil.e("fastForward " + e);
        }
    }

    @Override
    public void seekTo(long positionMs) {
        try {
            if (isInPlaybackState()) {
                mMediaPlayer.seekTo(positionMs);
                mSeekWhenPrepared = 0;
            } else {
                mSeekWhenPrepared = positionMs;
            }
        } catch (Exception e) {
            LogUtil.e("seekTo " + e);
        }

    }

    @Override
    public String getPlayUrl() {
        return null;
    }

    @Override
    public void setVolume(float volume) {

    }

    @Override
    public <T> T getPlayerInstance() {
        return null;
    }


    /*
     * release the media player in any state
     */
    public void release(boolean cleartargetstate) {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer.reset();
                mMediaPlayer.release();
                mMediaPlayer = null;
                // REMOVED: mPendingSubtitleTracks.clear();
                mCurrentState = STATE_IDLE;
                if (cleartargetstate) {
                    mTargetState = STATE_IDLE;
                }
            }
        } catch (Exception e) {
            LogUtil.e("release " + e);

        }
    }

    private boolean isInPlaybackState() {
        return (mMediaPlayer != null &&
                mCurrentState != STATE_ERROR &&
                mCurrentState != STATE_IDLE &&
                mCurrentState != STATE_PREPARING);
    }


    IMediaPlayer.OnPreparedListener mPreparedListener = new IMediaPlayer.OnPreparedListener() {
        public void onPrepared(IMediaPlayer mp) {
            LogUtil.d("onPrepared mp=" + mp + ", mTargetState=" + mTargetState);

            mCurrentState = STATE_PREPARED;

            // Get the capabilities of the player for this stream
            // REMOVED: Metadata

            if (mOnPreparedListener != null) {
                mOnPreparedListener.onPrepared(mMediaPlayer);
            }
//            if (mMediaController != null) {
//                mMediaController.setEnabled(true);
//            }
//            mVideoWidth = mp.getVideoWidth();
//            mVideoHeight = mp.getVideoHeight();

            long seekToPosition = mSeekWhenPrepared;  // mSeekWhenPrepared may be changed after seekTo() call
            if (seekToPosition != 0) {
                seekTo((int) seekToPosition);
            }

            if (mTargetState == STATE_PLAYING) {
                play();
            }

//            if (mVideoWidth != 0 && mVideoHeight != 0) {
//                //Log.i("@@@@", "video size: " + mVideoWidth +"/"+ mVideoHeight);
//                // REMOVED: getHolder().setFixedSize(mVideoWidth, mVideoHeight);
//                if (mRenderView != null) {
//                    mRenderView.setVideoSize(mVideoWidth, mVideoHeight);
//                    mRenderView.setVideoSampleAspectRatio(mVideoSarNum, mVideoSarDen);
//                    if (!mRenderView.shouldWaitForResize() || mSurfaceWidth == mVideoWidth && mSurfaceHeight == mVideoHeight) {
//                        // We didn't actually change the size (it was already at the size
//                        // we need), so we won't get a "surface changed" callback, so
//                        // start the video here instead of in the callback.
//                        if (mTargetState == STATE_PLAYING) {
//                            start();
//                            if (mMediaController != null) {
//                                mMediaController.show();
//                            }
//                        } else if (!isPlaying() &&
//                                (seekToPosition != 0 || getCurrentPosition() > 0)) {
//                            if (mMediaController != null) {
//                                // Show the media controls when we're paused into a video and make 'em stick.
//                                mMediaController.show(0);
//                            }
//                        }
//                    }
//                }
//            } else {
//                // We don't know the video size yet, but should start anyway.
//                // The video size might be reported to us later.
//                if (mTargetState == STATE_PLAYING) {
//                    start();
//                }
//            }
        }
    };

    private IMediaPlayer.OnCompletionListener mCompletionListener =
            new IMediaPlayer.OnCompletionListener() {
                public void onCompletion(IMediaPlayer mp) {
                    mCurrentState = STATE_PLAYBACK_COMPLETED;
                    mTargetState = STATE_PLAYBACK_COMPLETED;
//                    if (mMediaController != null) {
//                        mMediaController.hide();
//                    }
                    if (mOnCompletionListener != null) {
                        mOnCompletionListener.onCompletion(mMediaPlayer);
                    }
                    if (playFinishCallback != null) {
                        playFinishCallback.onPlayerState(OnPlayerCallback.State.STATE_ENDED);
                    }
                }
            };

    private IMediaPlayer.OnInfoListener mInfoListener =
            new IMediaPlayer.OnInfoListener() {
                public boolean onInfo(IMediaPlayer mp, int arg1, int arg2) {
                    if (mOnInfoListener != null) {
                        mOnInfoListener.onInfo(mp, arg1, arg2);
                    }
                    switch (arg1) {
                        case IMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
//                            mVideoRotationDegree = arg2;
//                           LogUtil.d(TAG, "MEDIA_INFO_VIDEO_ROTATION_CHANGED: " + arg2);
//                            if (mRenderView != null)
//                                mRenderView.setVideoRotation(arg2);
//                            break;
                    }
                    return true;
                }
            };

    private IMediaPlayer.OnErrorListener mErrorListener =
            new IMediaPlayer.OnErrorListener() {
                public boolean onError(IMediaPlayer mp, int framework_err, int impl_err) {
                    LogUtil.d(TAG, "Error: " + framework_err + "," + impl_err);
                    mCurrentState = STATE_ERROR;
                    mTargetState = STATE_ERROR;
//                    if (mMediaController != null) {
//                        mMediaController.hide();
//                    }

                    if (playFinishCallback != null) {
                        playFinishCallback.onPlayError(IJKAudioPlayer.this, playUrl, "");
                    }

                    /* If an error handler has been supplied, use it and finish. */
                    if (mOnErrorListener != null) {
                        if (mOnErrorListener.onError(mMediaPlayer, framework_err, impl_err)) {
                            return true;
                        }
                    }

                    /* Otherwise, pop up an error dialog so the user knows that
                     * something bad has happened. Only try and pop up the dialog
                     * if we're attached to a window. When we're going away and no
                     * longer have a window, don't bother showing the user an error.
                     */
//                    if (getWindowToken() != null) {
//                        Resources r = mAppContext.getResources();
//                        String message="Unknown error";
//
//                        if (framework_err == MediaPlayer.MEDIA_ERROR_NOT_VALID_FOR_PROGRESSIVE_PLAYBACK) {
//                            message="Invalid progressive playback";
//                        }
//
//                        new android.app.AlertDialog.Builder(getContext())
//                                .setMessage(message)
//                                .setPositiveButton("error",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int whichButton) {
//                                            /* If we get here, there is no onError listener, so
//                                             * at least inform them that the video is over.
//                                             */
//                                                if (mOnCompletionListener != null) {
//                                                    mOnCompletionListener.onCompletion(mMediaPlayer);
//                                                }
//                                            }
//                                        })
//                                .setCancelable(false)
//                                .show();
//                    }
                    return true;
                }
            };

    private IMediaPlayer.OnBufferingUpdateListener mBufferingUpdateListener =
            new IMediaPlayer.OnBufferingUpdateListener() {
                public void onBufferingUpdate(IMediaPlayer mp, int percent) {
                    mCurrentBufferPercentage = percent;
                }
            };

    /**
     * Register a callback to be invoked when the media file
     * is loaded and ready to go.
     *
     * @param l The callback that will be run
     */
    public void setOnPreparedListener(IMediaPlayer.OnPreparedListener l) {
        mOnPreparedListener = l;
    }

    /**
     * Register a callback to be invoked when the end of a media file
     * has been reached during playback.
     *
     * @param l The callback that will be run
     */
    public void setOnCompletionListener(IMediaPlayer.OnCompletionListener l) {
        mOnCompletionListener = l;
    }

    /**
     * Register a callback to be invoked when an error occurs
     * during playback or setup.  If no listener is specified,
     * or if the listener returned false, VideoView will inform
     * the user of any errors.
     *
     * @param l The callback that will be run
     */
    public void setOnErrorListener(IMediaPlayer.OnErrorListener l) {
        mOnErrorListener = l;
    }

    /**
     * Register a callback to be invoked when an informational event
     * occurs during playback or setup.
     *
     * @param l The callback that will be run
     */
    public void setOnInfoListener(IMediaPlayer.OnInfoListener l) {
        mOnInfoListener = l;
    }
}
