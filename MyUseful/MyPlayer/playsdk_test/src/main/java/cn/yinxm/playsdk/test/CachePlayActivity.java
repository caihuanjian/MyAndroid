package cn.yinxm.playsdk.test;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.app.media.player.IPlayHelper;
import com.app.media.player.OnPlayerCallback;
import com.app.media.player.exo.ExoAudioPlayHelper;

/**
 * 边下边播
 */
public class CachePlayActivity extends AppCompatActivity {

    IPlayHelper exoPlayHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cache_play);

        exoPlayHelper = new ExoAudioPlayHelper(getApplicationContext());

        findViewById(R.id.exo_cache_play).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cachePlayExoPlayer();
            }
        });
    }

    private void cachePlayExoPlayer() {
        exoPlayHelper.play(PlayUrlTest.getPlayUrl(), new OnPlayerCallback() {
            @Override
            public void onPlayerState(int state) {
                if (State.STATE_READY == state) {
                    exoPlayHelper.play();
                }else if (State.STATE_ENDED == state) {
                    LogUtil.d("播放完毕");
                }
            }

            @Override
            public void onPlayError(IPlayHelper playHelper, String playUrl, String errorMsg) {

            }
        });
    }
}
