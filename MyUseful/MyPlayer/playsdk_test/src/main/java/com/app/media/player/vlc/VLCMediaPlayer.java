/* == This file is part of Tomahawk Player - <http://tomahawk-player.org> ===
 *
 *   Copyright 2014, Enno Gottschalk <mrmaffen@googlemail.com>
 *
 *   Tomahawk is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Tomahawk is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Tomahawk. If not, see <http://www.gnu.org/licenses/>.
 */
package com.app.media.player.vlc;

import android.content.Context;
import android.media.session.PlaybackState;
import android.net.Uri;
import android.util.Log;

import com.app.media.player.IPlayHelper;
import com.app.media.player.OnPlayerCallback;

import org.videolan.libvlc.LibVLC;
import org.videolan.libvlc.Media;
import org.videolan.libvlc.MediaPlayer;

import java.util.Map;


import cn.yinxm.playsdk.test.LogUtil;

/**
 * This class wraps a libvlc mediaplayer instance.
 */
public class VLCMediaPlayer implements IPlayHelper {

    private static final String TAG = VLCMediaPlayer.class.getSimpleName();
    Context context;

    private  MediaPlayer sMediaPlayer;

    private  LibVLC sLibVLC;

//    static {
//        ArrayList<String> options = new ArrayList<>();
//        options.add("--http-reconnect");
//        options.add("--network-caching=2000");
////        sLibVLC = new LibVLC(options);
//        sLibVLC = new LibVLC(TomahawkApp.getContext());
//        sMediaPlayer = new MediaPlayer(sLibVLC);
//    }

//    private TomahawkMediaPlayerCallback mMediaPlayerCallback;
//
//    private Query mPreparedQuery;
//
//    private Query mPreparingQuery;

    private int mPlayState = PlaybackState.STATE_NONE;

    private class MediaPlayerListener implements MediaPlayer.EventListener {

        @Override
        public void onEvent(final MediaPlayer.Event event) {

            Runnable r = new Runnable() {
                @Override
                public void run() {
                    switch (event.type) {
                        case MediaPlayer.Event.EncounteredError:
                            Log.d(TAG, "MediaPlayer.Event.EncounteredError");
//                            mPreparedQuery = null;
//                            mPreparingQuery = null;
//                            if (mMediaPlayerCallback != null) {
//                                mMediaPlayerCallback.onError(
//                                        VLCMediaPlayer.this, "LibVLC encountered an error");
//                            } else {
//                                Log.e(TAG, "Wasn't able to call onError because callback"
//                                        + " object is null");
//                            }
                            break;
                        case MediaPlayer.Event.EndReached:
                            Log.d(TAG, "MediaPlayer.Event.EndReached");
//                            if (mMediaPlayerCallback != null) {
//                                mMediaPlayerCallback.onCompletion(
//                                        VLCMediaPlayer.this, mPreparedQuery);
//                            } else {
//                                Log.e(TAG, "Wasn't able to call onCompletion because callback"
//                                        + " object is null");
//                            }
                            break;
                    }
                }
            };
            new Thread(r).run();
//            ThreadManager.get().executePlayback(VLCMediaPlayer.this, r);
        }
    }

    public VLCMediaPlayer(Context context) {
        Log.d(TAG, "VLCMediaPlayer context="+context);
        sLibVLC = new LibVLC(context);
        sMediaPlayer = new MediaPlayer(sLibVLC);

        sMediaPlayer = new MediaPlayer(sLibVLC);
//        if (PreferenceUtils.getBoolean(PreferenceUtils.EQUALIZER_ENABLED)) {
//            MediaPlayer.Equalizer equalizer = MediaPlayer.Equalizer.create();
//            float[] bands = PreferenceUtils.getFloatArray(PreferenceUtils.EQUALIZER_VALUES);
//            equalizer.setPreAmp(bands[0]);
//            for (int i = 0; i < MediaPlayer.Equalizer.getBandCount(); i++) {
//                equalizer.setAmp(i, bands[i + 1]);
//            }
//            sMediaPlayer.setEqualizer(equalizer);
//        }
        sMediaPlayer.setEventListener(new MediaPlayerListener());
    }

    public  LibVLC getLibVlcInstance() {
        return sLibVLC;
    }

    public  MediaPlayer getMediaPlayerInstance() {
        return sMediaPlayer;
    }

    @Override
    public void play(String url) {
        Log.d(TAG, "play="+url +", sLibVLC="+sLibVLC);
        try {
            Media media = new Media(sLibVLC, Uri.parse(url));
            sMediaPlayer.setMedia(media);
            sMediaPlayer.play();

//        media.release();
            Log.d(TAG, "media.release");

//            sMediaPlayer.play();
            Log.d(TAG, "play start");
        }catch (Exception e) {
            LogUtil.e(e);
        }

//        sMediaPlayer.addSlave(Media.Slave.Type.Audio, url, true);
    }

    @Override
    public void play(String url, OnPlayerCallback playFinishCallback) {

    }

    @Override
    public void play(String url, Map<String, Object> extendParams, OnPlayerCallback playFinishCallback) {

    }


    /**
     * Start playing the previously prepared org.tomahawk.libtomahawk.collection.Track
     */
    @Override
    public void play() throws IllegalStateException {
        Log.d(TAG, "play()");
        mPlayState = PlaybackState.STATE_PLAYING;
        handlePlayState();
    }

    /**
     * Pause playing the current org.tomahawk.libtomahawk.collection.Track
     */
    @Override
    public void pause() throws IllegalStateException {
        Log.d(TAG, "pause()");
        mPlayState = PlaybackState.STATE_PAUSED;
        handlePlayState();
    }

    @Override
    public void playToggle() {

    }

    @Override
    public void stop() {

    }

    /**
     * Seek to the given playback position (in ms)
     */
    @Override
    public void seekTo(long msec) throws IllegalStateException {
//        Log.d(TAG, "seekTo()");
//        if (mPreparedQuery != null && !TomahawkApp.PLUGINNAME_BEATSMUSIC.equals(
//                mPreparedQuery.getPreferredTrackResult().getResolvedBy().getId())) {
//            getMediaPlayerInstance().setTime(msec);
//        }
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

//    /**
//     * Prepare the given url
//     */
//    public void prepare(final Query query, TomahawkMediaPlayerCallback callback) {
//        Log.d(TAG, "prepare() query: " + query);
//        mMediaPlayerCallback = callback;
//        getMediaPlayerInstance().stop();
//        mPreparedQuery = null;
//        mPreparingQuery = query;
//        getStreamUrl(query.getPreferredTrackResult()).done(new DoneCallback<String>() {
//            @Override
//            public void onDone(String url) {
//                Log.d(TAG, "Received stream url: " + url + " for query: " + query);
//                if (mPreparingQuery != null && mPreparingQuery == query) {
//                    Log.d(TAG, "Starting to prepare stream url: " + url + " for query: " + query);
//                    Media media = new Media(sLibVLC, AndroidUtil.LocationToUri(url));
//                    getMediaPlayerInstance().setMedia(media);
//                    mPreparedQuery = mPreparingQuery;
//                    mPreparingQuery = null;
//                    mMediaPlayerCallback.onPrepared(VLCMediaPlayer.this, mPreparedQuery);
//                    handlePlayState();
//                    Log.d(TAG, "onPrepared() url: " + url + " for query: " + query);
//                } else {
//                    Log.d(TAG, "Ignoring stream url: " + url + " for query: " + query
//                            + ", because preparing query is: " + mPreparingQuery);
//                }
//            }
//        });
//    }

    @Override
    public void release() {
        Log.d(TAG, "release()");
//        mPreparedQuery = null;
//        mPreparingQuery = null;
        getMediaPlayerInstance().stop();
//        mMediaPlayerCallback = null;
    }

    @Override
    public long getDuration() {
        return 0;
    }

    @Override
    public long getCurrentPosition() {
//        if (mPreparedQuery != null) {
            return getMediaPlayerInstance().getTime();
//        } else {
//            return 0L;
//        }
    }

    @Override
    public int getBufferedPercentage() {
        return 0;
    }

    @Override
    public long getBufferedPosition() {
        return 0;
    }

    @Override
    public boolean isPlaying() {
//        return isPrepared(query) && getMediaPlayerInstance().isPlaying();
        return getMediaPlayerInstance().isPlaying();
    }

    @Override
    public void rewind() {

    }

    @Override
    public void fastForward() {

    }

//    @Override
//    public boolean isPlaying(Query query) {
//        return isPrepared(query) && getMediaPlayerInstance().isPlaying();
//    }

//    @Override
//    public boolean isPreparing(Query query) {
//        return mPreparingQuery != null && mPreparingQuery == query;
//    }
//
//    @Override
//    public boolean isPrepared(Query query) {
//        return mPreparedQuery != null && mPreparedQuery == query;
//    }

    private void handlePlayState() {
//        if (mPreparedQuery != null) {
            if (mPlayState == PlaybackState.STATE_PAUSED
                    && getMediaPlayerInstance().isPlaying()) {
                getMediaPlayerInstance().pause();
            } else if (mPlayState == PlaybackState.STATE_PLAYING
                    && !getMediaPlayerInstance().isPlaying()) {
                getMediaPlayerInstance().play();
            }
//        }
    }
}
