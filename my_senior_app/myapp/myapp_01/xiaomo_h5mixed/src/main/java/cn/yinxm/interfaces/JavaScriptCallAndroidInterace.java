package cn.yinxm.interfaces;

import android.webkit.JavascriptInterface;

import java.io.Serializable;

/**
 * Created by yinxm on 2017/1/1.
 * 功能: Android暴露给JS调用的接口
 */

public interface JavaScriptCallAndroidInterace extends Serializable{

    /**
     * 开始听写 语音——》文字
     */
    @JavascriptInterface
    public void startIat();

    /**
     * 开始语音命令词识别：录音——》命令词
     */
    public void startVoiceUnderstand();

    /**
     * tts朗读
     * @param text 需要朗读的文字
     */
    @JavascriptInterface
    public void startTts(String text);

    /**
     * 停止朗读
     */
    @JavascriptInterface
    public void stopTts();
}
