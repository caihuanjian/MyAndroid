package cn.yinxm.playsdk.test;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.MappingTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.DebugTextViewHelper;
import com.google.android.exoplayer2.ui.PlaybackControlView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;

import cn.yinxm.lib.media.player.exo.ExoAudioPlayHelper;
import cn.yinxm.lib.media.player.media.MediaPlayHelper;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, ExoPlayer.EventListener, PlaybackControlView.VisibilityListener {

    EditText etPlayUrl;
    Button btn_play, exo_play, exo_play_btn_next, exo_play_btn_info,exo_play_btn_stop, exo_play_btn_release;
    TextView debug_text_view;
    RadioGroup rg_play;
    private static final DefaultBandwidthMeter BANDWIDTH_METER = new DefaultBandwidthMeter();
    private Handler mainHandler;
    private Timeline.Window window;
    private EventLogger eventLogger;

    private SimpleExoPlayerView simpleExoPlayerView;
    private LinearLayout debugRootView;
    private TextView debugTextView;

    private DataSource.Factory mediaDataSourceFactory;
    private SimpleExoPlayer player;
    private DefaultTrackSelector trackSelector;
    private TrackSelectionHelper trackSelectionHelper;//显示播放信息状态
    private DebugTextViewHelper debugViewHelper;
    private boolean playerNeedsSource;

    boolean isPlaying = false;
    MediaPlayHelper audioPlayHelper;

    ExoAudioPlayHelper exoAudioPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("yinxm", "MainActivity");
        etPlayUrl = (EditText) findViewById(R.id.etPlayUrl);
//        etPlayUrl.setText("http://192.168.1.71/file/mystream.ts");
//        etPlayUrl.setText("http://trslbs.itings.com/016f63815d64d4db/1600000000481/playlist.m3u8");
//        etPlayUrl.setText("https://dco4urblvsasc.cloudfront.net/811/81095_ywfZjAuP/game/index.m3u8");//直播
//        etPlayUrl.setText("http://fm.work_ec.com.cn/hls/mystream.m3u8");//直播
//        etPlayUrl.setText("http://trslbs.itings.com/016f63815d64d4db/1600000000323/playlist.m3u8");//直播
//        etPlayUrl.setText("https://dco4urblvsasc.cloudfront.net/811/81095_ywfZjAuP/game/index.m3u8");//视频直播
//        etPlayUrl.setText("http://trslbs.itings.com/016f63815d64d4db/1600000000323/1486378800000_1486385999000.m3u8");//回放
        etPlayUrl.setText("http://music.work_ec.com.cn/music/5322656.mp3?qcode=werd3819dkeK");
        rg_play = (RadioGroup) findViewById(R.id.rg_play);
        btn_play = (Button) findViewById(R.id.btn_play);

        debug_text_view = (TextView) findViewById(R.id.debug_text_view);
        exo_play_btn_next = (Button) findViewById(R.id.exo_play_btn_next);
        exo_play_btn_info = (Button) findViewById(R.id.exo_play_btn_info);

        exo_play_btn_next.setOnClickListener(this);
        exo_play_btn_info.setOnClickListener(this);

        audioPlayHelper = new MediaPlayHelper();

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setControllerVisibilityListener(this);
        simpleExoPlayerView.requestFocus();
        debugRootView = (LinearLayout) findViewById(R.id.controls_root);
        debugTextView = (TextView) findViewById(R.id.debug_text_view);


        rg_play.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
               if (audioPlayHelper != null && audioPlayHelper.isPlaying()) {
                    audioPlayHelper.pause();
                }
                if (exoAudioPlayer!=null && exoAudioPlayer.isPlaying()) {
                    exoAudioPlayer.pause();
                }
            }
        });

        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                final MediaPlayer mediaPlayer = new MediaPlayer();
                try {
//                    AssetFileDescriptor assetFileDescriptor = getAssets().openFd("iattest.wav");
//                    LogUtil.d("assetFileDescriptor="+assetFileDescriptor+", "+assetFileDescriptor.getLength());
//                    mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                    mediaPlayer.setDataSource("http://192.168.1.71/file/mystream.ts");
                } catch (IOException e) {
//                    e.printStackTrace();
                    LogUtil.e(e);
                }
                mediaPlayer.prepareAsync();
                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mediaPlayer.start();
                    }
                });*/


                boolean isUserExoPlay = true;
                if (rg_play.getCheckedRadioButtonId() == R.id.rb_mp) {
                    isUserExoPlay = false;
                }
                LogUtil.d("isUserExoPlay="+isUserExoPlay);
                if (isUserExoPlay) {
                    exoPlay();
                } else {
                    audioPlayHelper.play(etPlayUrl.getText().toString());
                }
            }
        });
    }


    @Override
    public void onClick(View view) {
//        Log.d("yinxm", "view=" + view);
        switch (view.getId()) {
            case R.id.exo_play_btn_next:
                playNext();
                break;
            case R.id.exo_play_btn_info:
                playInfo();
                break;
        }

    }

    private void initExoPlayHelper() {
        if (exoAudioPlayer == null) {
            exoAudioPlayer = new ExoAudioPlayHelper(getApplicationContext());
            simpleExoPlayerView.setPlayer(exoAudioPlayer.getPlayer());
        }
    }

    private void exoPlay() {
        if (exoAudioPlayer == null) {
            initExoPlayHelper();
            exoAudioPlayer.play(etPlayUrl.getText().toString());
        } else {
            exoAudioPlayer.playToggle();
        }
        showControls();
        playInfo();
    }


    private void playNext() {
        Log.d("yinxm", "playNext exoAudioPlayer="+exoAudioPlayer);
        if (exoAudioPlayer == null) {
            initExoPlayHelper();
        }
        exoAudioPlayer.play("http://image.kaolafm.net/mz/filmchipsmp3_32/201609/7da02c7e-3ecb-4caa-bb0b-7739e262c920.mp3?appid=kfxmv5890&deviceid=100014712&audioid=1000002765356");

        showControls();
        playInfo();
    }

    private void playInfo() {
        if (exoAudioPlayer != null) {
            SimpleExoPlayer player = exoAudioPlayer.getPlayer();
            if (player == null) {
                return;
            }
            StringBuilder buider = new StringBuilder();
            buider.append("playWhenReady=" + player.getPlayWhenReady()).append(", ");
            buider.append("getPlaybackState=" + player.getPlaybackState()).append(", ");
            buider.append("isLoading=" + player.isLoading()).append(", ");
            buider.append("duration=" + player.getDuration()).append(", ");
            buider.append("getCurrentPosition=" + player.getCurrentPosition()).append(", ");
            buider.append("bufferPercent=" + player.getBufferedPercentage()).append(", ");
            LogUtil.d("playInfo=" + buider.toString());
            debug_text_view.setText(buider.toString());
        }
    }



    // Activity input

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // Show the controls on any key event.
        simpleExoPlayerView.showController();
        // If the event was not handled then see if the player view can handle it as a media key event.
        return super.dispatchKeyEvent(event) || simpleExoPlayerView.dispatchMediaKeyEvent(event);
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
        Log.d("yinxm", "onTimelineChanged timeline=" + timeline + ", manifest=" + manifest);

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
        Log.d("yinxm", "onTracksChanged trackGroups=" + trackGroups + ", trackSelections=" + trackSelections);
        updateButtonVisibilities();
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.d("yinxm", "onLoadingChanged isLoading=" + isLoading);

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        playInfo();
        if (playWhenReady == true && playbackState == ExoPlayer.STATE_READY) {
            isPlaying = true;
        } else {
            isPlaying = false;
        }
        Log.d("yinxm", "onPlayerStateChanged playWhenReady=" + playWhenReady + ", playbackState=" + playbackState + ", isPlaying=" + isPlaying);

        if (playbackState == ExoPlayer.STATE_ENDED) {
            showControls();
        }
        updateButtonVisibilities();
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Log.d("yinxm", "onPlayerError error=" + error);
        Log.e("yinxm", "onPlayerError", error);
        updateButtonVisibilities();
        showControls();
    }

    @Override
    public void onPositionDiscontinuity() {
        Log.d("yinxm", "onPositionDiscontinuity");

    }

    // User controls

    private void updateButtonVisibilities() {
        Log.d("yinxm", "updateButtonVisibilities");

        debugRootView.removeAllViews();

//        retryButton.setVisibility(playerNeedsSource ? View.VISIBLE : View.GONE);
//        debugRootView.addView(retryButton);

        if (player == null) {
            return;
        }

        MappingTrackSelector.MappedTrackInfo mappedTrackInfo = trackSelector.getCurrentMappedTrackInfo();
        if (mappedTrackInfo == null) {
            return;
        }

        for (int i = 0; i < mappedTrackInfo.length; i++) {
            TrackGroupArray trackGroups = mappedTrackInfo.getTrackGroups(i);
            if (trackGroups.length != 0) {
                Button button = new Button(this);
                int label;
                switch (player.getRendererType(i)) {
                    case C.TRACK_TYPE_AUDIO:
                        label = R.string.audio;
                        break;
                    case C.TRACK_TYPE_VIDEO:
                        label = R.string.video;
                        break;
                    case C.TRACK_TYPE_TEXT:
                        label = R.string.text;
                        break;
                    default:
                        continue;
                }
                LogUtil.d("label="+label);
                button.setText(label);
                button.setTag(i);
                button.setOnClickListener(this);
                debugRootView.addView(button, debugRootView.getChildCount() - 1);
            }
        }
    }

    private void showControls() {
        debugRootView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onVisibilityChange(int visibility) {
        debugRootView.setVisibility(visibility);
    }


}
