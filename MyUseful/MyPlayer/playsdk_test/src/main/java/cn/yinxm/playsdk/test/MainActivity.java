package cn.yinxm.playsdk.test;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.media.bean.base.MediaBean;
import com.app.media.bean.base.MediaFromType;
import com.app.media.bean.base.MediaList;
import com.app.media.bean.base.MediaPlayListType;
import com.app.media.manager.MediaPlayServiceManger;
import com.app.media.manager.PlayControlManager;
import com.app.media.manager.PlayDataManager;
import com.app.media.manager.PlayInfoChangedCallbackManager;
import com.app.media.player.IPlayHelper;
import com.app.media.player.constant.PlayerType;
import com.app.media.sdk.IPlayData;
import com.app.media.sdk.MediaPlayMode;
import com.app.media.sdk.OnPlayInfoCallback;
import com.app.media.sdk.OnProgressCallback;
import com.app.media.service.MediaPlayService;
import com.app.media.ui.PlayBar;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";


    TextView playInfo, tvPlayState;

    Button exo_play, btn_play_vlc, btn_play_ijk;
    Button btn_pause_continue;
    EditText inputUrl;
    private RadioGroup selectPlayers;
    private ImageView img;

    PlayBar mPlayBar;


    IPlayHelper currentPlayer;
    String playUrl = PlayUrlTest.getPlayUrl();
    IPlayData mPlayData;

    /**
     * 用户正在拖动SeekBar
     */
    private boolean isUserTouchingSeekBar = false;

    OnPlayInfoCallback mPlayInfoCallback;
    OnProgressCallback mOnProgressCallback;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getPlayinfo();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playInfo = (TextView) findViewById(R.id.playInfo);
        tvPlayState = (TextView) findViewById(R.id.tvPlayState);
        inputUrl = (EditText) findViewById(R.id.inputUrl);
        selectPlayers = findViewById(R.id.select_players);
        img = findViewById(R.id.img);

        mPlayBar = findViewById(R.id.play_bar);

        mPlayData = PlayDataManager.getInstance();

        selectPlayers.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                PlayControlManager.getInstance().stop();
                MediaPlayService service = MediaPlayServiceManger.getInstance().getMediaPlayService();
                switch (checkedId) {
                    case R.id.rb_media:
                        service.setUserChoosePlayerType(PlayerType.MEDIA_PLAYER);
                        break;
                    case R.id.rb_exo:
                        service.setUserChoosePlayerType(PlayerType.DEFAULT_EXO);
                        break;
                    case R.id.rb_ijk:
                        break;
                    case R.id.rb_vlc:
                        break;
                }
            }
        });

        inputUrl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                playUrl = s.toString();
                setPlayData(playUrl);
            }
        });

        inputUrl.setText(playUrl);

        mPlayInfoCallback = new OnPlayInfoCallback() {
            @Override
            public void onPlayStateChanged(int state, boolean isPlaying) {
                LogUtil.d(TAG, "onPlayStateChanged state=" + state + ", isPlaying=" + isPlaying);
                updatePlayState(isPlaying);
                mPlayBar.onPlayStateChanged(isPlaying);
            }

            @Override
            public void onPlayBeanChanged(int position, MediaBean bean) {
                LogUtil.d(TAG, "onPlayBeanChanged position=" + position + ", bean=" + bean);
                updatePlayItem(position, bean);
                mPlayBar.onPlayBeanChanged(bean);
            }

            @Override
            public void onPlayListChanged(MediaPlayListType playListType, MediaList listBean) {
                LogUtil.d(TAG, "onPlayBeanChanged playListType=" + playListType + ", listBean=" + listBean);
                updatePlayList(playListType, listBean);

            }

            @Override
            public void onPlayModeChanged(MediaPlayMode mode) {
                LogUtil.d(TAG, "onPlayBeanChanged mode=" + mode + ", mode=" + mode);
                updatePlayMode(mode);
            }


        };

        mOnProgressCallback = new OnProgressCallback() {
            @Override
            public void onProgressChanged(long duration, long currentPlayPosition, long bufferedPosition) {
//        LogUtil.d(TAG, "onProgressChanged duration=" + duration + ", currentPlayPosition="
//                + currentPlayPosition + "，bufferedPosition=" + bufferedPosition);
                // 进度条拖动中，不要更新进度
                if (!isUserTouchingSeekBar) {
                    updateProgress(duration, currentPlayPosition, bufferedPosition);
                }
                mPlayBar.onProgressChanged(duration, currentPlayPosition, bufferedPosition);

            }
        };
    }

    void setPlayData(String playUrl) {
        MediaList mediaList = new MediaList();
        List<MediaBean> mediaBeanList = new ArrayList<>();
        MediaBean mediaBean = new MediaBean();
        mediaBean.setFromType(MediaFromType.MUSIC);
        mediaBean.setPlayUrl(playUrl);
        mediaBean.setTitle(playUrl);
        mediaBeanList.add(mediaBean);
        mediaList.setList(mediaBeanList);
        PlayDataManager.getInstance().setPlayList(mediaList);
        PlayDataManager.getInstance().setPlayListPosition(0);
    }


    @Override
    protected void onResume() {
        super.onResume();
        PlayInfoChangedCallbackManager.getInstance().registerPlayInfoCallback(mPlayInfoCallback);
        PlayInfoChangedCallbackManager.getInstance().registerProgressCallback(mOnProgressCallback);

        onResumeUpdatePlayInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        PlayInfoChangedCallbackManager.getInstance().unregisterPlayInfoCallback(mPlayInfoCallback);
        PlayInfoChangedCallbackManager.getInstance().unregisterProgressCallback(mOnProgressCallback);
    }

    // 每次界面展示的时候，刷新显示信息
    private void onResumeUpdatePlayInfo() {
        updatePlayList(mPlayData.getPlayListType(), mPlayData.getPlayList());
        updatePlayItem(mPlayData.getPlayListPosition(), mPlayData.getPlayMediaBean());
        updatePlayState(mPlayData.isPlaying());
        IPlayHelper playHelper = mPlayData.getPlayer();
        if (playHelper != null) {
            updateProgress(playHelper.getDuration(), playHelper.getCurrentPosition(),
                    playHelper.getBufferedPosition());
        }
        updatePlayMode(mPlayData.getPlayMode());
        mPlayBar.onResumeUpdatePlayInfo();
    }

    private void updatePlayList(MediaPlayListType playListType, MediaList playList) {
    }

    private void updatePlayItem(int playListPosition, MediaBean playMediaBean) {
        mPlayBar.onPlayBeanChanged(playMediaBean);

    }

    private void updatePlayState(boolean isPlaying) {
        mPlayBar.onPlayStateChanged(isPlaying);
    }

    private void updateProgress(long duration, long currentPosition, long bufferedPosition) {
    }

    private void updatePlayMode(MediaPlayMode playMode) {
    }


    @Override
    public void onClick(View view) {
        String input = inputUrl.getText().toString();
        if (!TextUtils.isEmpty(input)) {
            playUrl = input;
        }
//        switch (view.getId()) {
//            case R.id.btn_media_play:
//                mediaPlay();
//                break;
//            default:
//                break;
//        }
        handler.sendEmptyMessageDelayed(0, 500);

    }

    private void getPlayinfo() {
        if (currentPlayer != null) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("getDuration=").append(currentPlayer.getDuration()).append("\n");
            stringBuilder.append("getCurrentPosition=").append(currentPlayer.getCurrentPosition()).append("\n");
            stringBuilder.append("getBufferedPercentage=").append(currentPlayer.getBufferedPercentage()).append("\n");
            stringBuilder.append("getBufferedPosition=").append(currentPlayer.getBufferedPosition()).append("\n");
            LogUtil.d(stringBuilder.toString());
            playInfo.setText(stringBuilder.toString());
            if (currentPlayer.getDuration() == 0 || currentPlayer.getDuration() != currentPlayer.getCurrentPosition()) {
                handler.sendEmptyMessageDelayed(0, 500);
            }
        }
    }


    public void btPlay(View view) {
        startActivity(new Intent(MainActivity.this, BtPlayActivity.class));
    }

    public void readAudioFile(View view) {
        String filePath = "/sdcard/test.mp3";
        ReadMp3InfoTest.test(filePath);
        MediaBean mediaBean = ReadMp3InfoByMmr.getFileInfo(filePath);
        playInfo.setText("" + mediaBean.getArtist() + ", " + mediaBean.getTitle());
        img.setImageBitmap((Bitmap) mediaBean.getExtObject());

        try {
            ReadMp3InfoByMp3agic.test(filePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void cachePlayActivity(View view) {
        startActivity(new Intent(MainActivity.this, CachePlayActivity.class));
    }
}
