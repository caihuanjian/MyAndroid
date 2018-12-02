package cn.yinxm.audiofocustest;

import android.app.Activity;
import android.content.ComponentName;
import android.media.AudioManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import cn.yinxm.media.receiver.MediaButtonReceiver;

public class MainActivity extends Activity {
    String TAG = "yinxm";

    AudioManager audioManager;
    ComponentName eventReceiver;

    View btn_get_duck, btn_release_duck ,btn_gain_focus, btn_release_focus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_get_duck = findViewById(R.id.btn_get_duck);

        btn_gain_focus = findViewById(R.id.btn_gain_focus);
        btn_release_focus = findViewById(R.id.btn_release_focus);


        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        eventReceiver = new ComponentName(getPackageName(), MediaButtonReceiver.class.getName());

        btn_get_duck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result =  audioManager.requestAudioFocus(listener, AudioManager.STREAM_SYSTEM,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);
                Log.d(TAG, "btn_get_duck demo result="+result);

            }
        });


        btn_gain_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result =  audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN);
                Log.d(TAG, "btn_gain_focus demo result="+result);
            }
        });

        findViewById(R.id.btn_gain_trans_focus).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result =  audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC,
                        AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
                Log.d(TAG, "btn_gain_trans_focus demo result="+result);
            }
        });
        btn_release_focus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int result =  audioManager.abandonAudioFocus(listener);
                Log.d(TAG, "btn_release_focus demo result="+result);
            }
        });

        initMediaBtn();

    }

    private void initMediaBtn() {
        //MediaButton接收事件原理：
//        1、普通应用，未在AudioManager中注册时，不会响应MediaButton按键
//        2、普通应用，注册后，响应MediaButton按键
//        3、系统应用，未注册，不响应MediaButton按键（Coolpad），由系统音乐播放器响应
//        4、系统应用，注册后，响应MediaButton按键（Coolpad）
//        5、系统应用，未注册，响应MediaButton按键(LX-1)
//        6、系统应用，注册后，响应MediaButton按键(LX-1)
        findViewById(R.id.btn_reg_mediaBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btn_reg_mediaBtn click ");
                audioManager.registerMediaButtonEventReceiver(eventReceiver);
            }
        });

        findViewById(R.id.btn_unreg_mediaBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "btn_unreg_mediaBtn click ");
                audioManager.unregisterMediaButtonEventReceiver(eventReceiver);

            }
        });
    }

    private AudioManager.OnAudioFocusChangeListener listener = new AudioManager.OnAudioFocusChangeListener(){

        @Override
        public void onAudioFocusChange(int focusChange) {
            Log.d(TAG, "onAudioFocusChange demo focusChange="+focusChange);
        }
    };
}
