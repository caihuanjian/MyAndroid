package cn.yinxm.iflytek;

import android.content.Context;
import android.widget.Toast;

import cn.yinxm.iflytek.interfaces.IatResultCallback;
import cn.yinxm.iflytek.interfaces.TtsFinishCallback;
import cn.yinxm.iflytek.interfaces.UnderstandResultCallback;

/**
 * Created by yinxm on 2017/1/1.
 * 功能: 讯飞语音功能管理器
 */

public class IflytekManager {
    private static final String TAG = "yinxm";

    private IflytekManager(){}
    public static IflytekManager getInstance() {
        return IflytekManagerFactory.instance;
    }
    private static class IflytekManagerFactory{
        private static IflytekManager instance = new IflytekManager();
    }

    private Context context;
    private Toast mToast;
    private IatMode iatMode;
    private TtsMode ttsMode;
    private UnderstanderMode understanderMode;

    public void init(Context context) {
        this.context = context;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
//        iatMode = new IatMode(context, mToast);
        ttsMode = new TtsMode(context, mToast);
        understanderMode = new UnderstanderMode(context, mToast);
    }

    /**
     * 开始录音听写iat
     * @param iatResultCallback
     */
    public void startRecord(IatResultCallback iatResultCallback) {
        if (iatMode != null) {
            iatMode.startRecord(iatResultCallback);
        }
    }

    /**
     * 开始语义理解
     * @param type 0:语音转语义；1:文字转语义
     * @param text
     * @param resultCallback
     */
    public void startUnderstand(int type, String text, UnderstandResultCallback resultCallback) {
        if (understanderMode != null) {
            understanderMode.startUnderstand(type, text, resultCallback);
        }
    }

    /**
     * 停止语义理解
     */
    public void stopUnderstand() {
        if (understanderMode != null) {
            understanderMode.stopUnderstand();
        }
    }


    /**
     * tts朗读
     * @param text 朗读的文字
     * @param callback 朗读完成回调
     */
    public void startTts(String text, TtsFinishCallback callback) {
        if(ttsMode != null) {
            ttsMode.startTts(text, callback);
        }
    }

    /**
     * 停止tts
     */
    public void stopTts() {
        if(ttsMode != null) {
            ttsMode.stopTts();
        }
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }
}
